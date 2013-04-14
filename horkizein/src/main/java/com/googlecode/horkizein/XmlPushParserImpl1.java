/*
 ** Copyright 2013, Horkizein Open Source Android Library
 **
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at
 **
 **     http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ** See the License for the specific language governing permissions and
 ** limitations under the License.
 */
package com.googlecode.horkizein;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.SparseArray;

/**
 * Implementation of XmlPushParser interface (v1). The internal data structures are not thread-safe,
 * hence neither is this class.
 */
public final class XmlPushParserImpl1 implements XmlPushParser {
    /**
     * The parser you want to use.
     */
    private final XmlPullParser mParser;
    /**
     * Storage for registered XmlPushables. One root tag per multiple XmlPushable objects
     * to preserve the insertion order if more than one have the same root tag.
     */
    private final Map<String, XmlPushable<?>> mPrototypeMap;
    /**
     * Storage for accepted tags. The first one will always be the root tag, then additionalTags
     * and what is recursively found in eclosedPushables follows.
     */
    private final Map<String, Set<String>> mTagMap;
    /**
     * Handy map for Metadata tag discovery.
     */
    protected final SparseArray<String> mMetadataMap;
    /**
     * The result of the Xml parsing. Brand new objects.
     */
    protected Map<String, List<XmlPushable<?>>> mXmlPushableMap;
    
    /**
     * Constructor.
     * @param parser XmlPullParser instance.
     */
    public XmlPushParserImpl1(XmlPullParser parser) {
        mParser = parser;
        mPrototypeMap = new HashMap<String, XmlPushable<?>>();
        // TODO fill mTagMap here for the input pushableMap
        mTagMap = new HashMap<String, Set<String>>();
        // List of filled objects
        mXmlPushableMap = new HashMap<String, List<XmlPushable<?>>>();
        
        // building the Metadata lookup table
        mMetadataMap = new SparseArray<String>();
        mMetadataMap.put(XmlPullParser.CDSECT, CDSECT_TAG);
        mMetadataMap.put(XmlPullParser.COMMENT, COMMENT_TAG);
        mMetadataMap.put(XmlPullParser.DOCDECL, DOCDECL_TAG);
        mMetadataMap.put(XmlPullParser.PROCESSING_INSTRUCTION, PROCESSING_TAG);
    }

    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#setInput(java.io.InputStream, java.lang.String)
     */
    @Override
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
        mParser.setInput(inputStream, inputEncoding);
    }
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#setInput(java.io.InputStream)
     */
    @Override
    public void setInput(InputStream inputStream) throws XmlPullParserException {
        mParser.setInput(inputStream, null);
    }
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#setInput(java.io.Reader)
     */
    @Override
    public void setInput(Reader in) throws XmlPullParserException {
        mParser.setInput(in);
    }
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#parse()
     */
    @Override
    final public void parse() throws XmlPullParserException, IOException {
        if (mTagMap.isEmpty()) return;
        
        List<StringBuilder> textStack = new LinkedList<StringBuilder>();
        String currentTag = "";
        String startTag = "";
        XmlPushable<?> rootItem = null;
        Set<String> acceptedTags = null;
        StringBuilder textBuilder = null;
        
        int currentEvent = mParser.getEventType();
        
        while (currentEvent != XmlPullParser.END_DOCUMENT) {
            if(currentEvent == XmlPullParser.START_TAG) {
                currentTag = mParser.getName();
                ///////////////////////////////////////////////////////////
                if (rootItem == null) {
                    // Looking for a registered item and get the builder
                    XmlPushable<?> prototype = mPrototypeMap.get(currentTag);
                    if (prototype != null) {
                        rootItem = prototype.shallowClone();
                    }
                    // Adding new instance to the Filled Object map
                    mXmlPushableMap.get(currentTag).add(rootItem);
                    // Getting the accepted tag list.
                    acceptedTags = mTagMap.get(currentTag);
                    startTag = currentTag;
                }
                if (rootItem != null) {
                    // Pushing just if the currentTag is part of the "accepted" tags.
                    if (acceptedTags.contains(currentTag)) {
                        rootItem.startTag(currentTag);
                        for (int i = 0; i < mParser.getAttributeCount(); ++i) {
                            rootItem.attribute(currentTag, mParser.getAttributePrefix(i), mParser.getAttributeName(i), mParser.getAttributeValue(i));
                        }
                        textBuilder = new StringBuilder();
                        textStack.add(textBuilder); // tail push
                    }
                }
                ///////////////////////////////////////////////////////////
            } else if(currentEvent == XmlPullParser.TEXT) {
                if (acceptedTags.contains(currentTag)) {
                    textBuilder.append(mParser.getText());
                }
            } else if (mMetadataMap.indexOfKey(currentEvent) > -1) {
                currentTag = mMetadataMap.get(currentEvent); //...entry point for a registered Metadata tag
                ///////////////////////////////////////////////////////////
                if (rootItem == null) {
                    // Looking for a registered item and get the builder
                    XmlPushable<?> prototype = mPrototypeMap.get(currentTag);
                    if (prototype != null) {
                        rootItem = prototype.shallowClone();
                    }
                    // Adding new instance to the Filled Object map
                    mXmlPushableMap.get(currentTag).add(rootItem);
                    // Getting the accepted tag list.
                    acceptedTags = mTagMap.get(currentTag);
                    startTag = currentTag;
                }
                if (rootItem != null) {
                    // Pushing just if the currentTag is part of the "accepted" tags.
                    if (acceptedTags.contains(currentTag)) {
                        rootItem.startTag(currentTag);
                        rootItem.text(currentTag, mParser.getText());
                        rootItem.endTag(currentTag);
                        if (startTag.equals(currentTag)) {
                            rootItem = null;
                            acceptedTags = null;
                            startTag = "";
                        }
                    }
                }
            } else if(currentEvent == XmlPullParser.END_TAG) {
                currentTag = mParser.getName();

                if (rootItem != null) {
                    if (acceptedTags.contains(currentTag)) {
                        if (textBuilder.length() > 0) {
                            rootItem.text(currentTag, textBuilder.toString());
                        }
                        textBuilder = textStack.remove(textStack.size() - 1); // tail pop
                        rootItem.endTag(currentTag);
                    }
                }
                if (startTag.equals(currentTag)) {
                    rootItem = null;
                    acceptedTags = null;
                    startTag = "";
                }
            }
            currentEvent = mParser.nextToken();
        }
    }

    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#addNode(com.googlecode.horkizein.XmlPushable)
     */
    @Override
    public void addNode(XmlPushable<?> xmlPushable) {
        Class<?> clazz = xmlPushable.getClass();
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        // Get the root tag from annotations
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        // Just to need to check in one of the maps if the rootTag is already registered.
        if (mTagMap.get(rootTag) == null) {
            // Creates the tag set to insert in the tag map.
            Set<String> tagSet = new HashSet<String>();
            mTagMap.put(rootTag, tagSet);
            // Saves the builder.
            mPrototypeMap.put(rootTag, xmlPushable);
            // Creates the list of instances in the Filled Object map
            mXmlPushableMap.put(rootTag, new LinkedList<XmlPushable<?>>());

            // Starts to recursively collect the additional tags
            collectTags(clazz, tagSet);
        }
    }

    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#reset()
     */
    @Override
    public void reset() {
        mPrototypeMap.clear();
        mTagMap.clear();
        mXmlPushableMap.clear();
    }

    /**
     * Internal recursive method to collect the tags accepted by the XmlPushable object.
     * @param clazz The XmlPushable class object.
     * @param tagStack A stack of tags.
     */
    private void collectTags(Class<?> clazz, Set<String> tagStack) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        tagStack.add(rootTag);
        
        String[] additionalTags = xmlTag.additionalTags();
        for (int i = 0; i < additionalTags.length; ++i) {
            tagStack.add(additionalTags[i]);
        }
        
        Class<? extends XmlPushable<?>>[] enclosedPushables = xmlTag.enclosedPushables();
        for (int i = 0; i < enclosedPushables.length; ++i) {
            collectTags(enclosedPushables[i], tagStack);
        }
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#buildFirstOf(java.lang.Class)
     */
    @Override
    public <T, E extends XmlPushable<T>> T buildFirstOf(Class<E> clazz) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        T built = null;
        if (rootTag != null) {
            // Workaround for:
            //  XmlPushParser.java:[294,35] invalid inferred types for T,K; inferred type does not conform to declared bound(s)
            E pushable = firstOf(rootTag);
            if (pushable != null) {
                built = pushable.build();
            }
        }
        return built;
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#buildFirstOf(java.lang.String)
     */
    @Override
    public <T> T buildFirstOf(String tag) {
        List<XmlPushable<?>> pushableList = mXmlPushableMap.get(tag);
        T built = null;
        if (pushableList != null) {
            XmlPushable<T> pushable = (XmlPushable<T>) pushableList.get(0);
            if (pushable != null) {
                built = pushable.build();
            }
        }
        return built;
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#buildListOf(java.lang.Class)
     */
    @Override
    public <T, E extends XmlPushable<T>> List<T> buildListOf(Class<E> clazz) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        return this.<T>buildListOf(rootTag);
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#buildListOf(java.lang.String)
     */
    @Override
    public <T> List<T> buildListOf(String tag) {
        List<XmlPushable<?>> pushableList = mXmlPushableMap.get(tag);
        List<T> returnedInstances = new ArrayList<T>();
        if (pushableList != null) {
            for (XmlPushable<?> pushable : pushableList) {
                returnedInstances.add((T)pushable.build());
            }
        }
        return returnedInstances;
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#firstOf(java.lang.Class)
     */
    @Override
    public <E extends XmlPushable<?>> E firstOf(Class<E> clazz) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        if (rootTag != null) {
            // Workaround for:
            //  XmlPushParser.java:[294,35] invalid inferred types for T,K; inferred type does not conform to declared bound(s)
            return firstOf(rootTag);
        } else {
            return null;
        }
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#firstOf(java.lang.String)
     */
    @Override
    public <E extends XmlPushable<?>> E firstOf(String tag) {
        List<XmlPushable<?>> pushableList = mXmlPushableMap.get(tag);
        if (pushableList != null) {
            return (E) pushableList.get(0);
        }
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#listOf(java.lang.Class)
     */
    @Override
    public <E extends XmlPushable<?>> List<E> listOf(Class<E> clazz) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        return listOf(rootTag);
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.horkizein.XmlPushParser#listOf(java.lang.String)
     */
    @Override
    public <E extends XmlPushable<?>> List<E> listOf(String tag) {
        List<XmlPushable<?>> pushableList = mXmlPushableMap.get(tag);
        List<E> out = null;
        if (pushableList != null) {
            out = (List<E>) Collections.unmodifiableList(pushableList);
        } else {
            out = Collections.EMPTY_LIST;
        }
        return out;
    }
}
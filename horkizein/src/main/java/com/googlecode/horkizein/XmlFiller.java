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
import java.util.ArrayList;
import java.util.Collection;
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
 * This class performs the XML binding. You can pass a Collection of String in order to register
 * the tags that XmlFiller accepts as "pushable".
 * XmlFiller includes a buffer for multiple TEXT event pulled from the parser. For this reason,
 * pushText() is called exactly once for each tag (if there is text to push).<br>
 * Metadata content is handled the same way as standard tags: every time a Metadata event is pulled,
 * XmlFiller calls both pushStartTag() and pushEndTag(), plus pushText() if necessary (once).
 * To register a Metadata XmlPushable please use the static fields CDSECT_TAG, COMMENT_TAG, DOCDECL_TAG,
 * PROCESSING_TAG as tag in your implementation.
 * If some tag has been registered but not found in the source .xml file, it will still be considered as
 * "registered". Be sure to call reset() before starting over.
 * XmlFiller's internal data structures are not thread-safe.
 */
public final class XmlFiller {
    /**
     * CDATA tag string.
     */
    public static final String CDSECT_TAG = "CDATA";
    /**
     * COMMENT tag string.
     */
    public static final String COMMENT_TAG = "COMMENT";
    /**
     * DOCDECL tag string.
     */
    public static final String DOCDECL_TAG = "DOCDECL";
    /**
     * PROCESSING_INSTRUCTION tag string.
     */
    public static final String PROCESSING_TAG = "PROCESSING";
    /**
     * The parser you want to use.
     */
    private final XmlPullParser mParser;
    /**
     * Storage for registered XmlPushables. One root tag per multiple XmlPushable objects
     * to preserve the insertion order if more than one have the same root tag.
     */
    private final Map<String, XmlBuilder<XmlPushable>> mPushableMap;
    /**
     * Storage for accepted tags. The first one will always be the root tag, then additionalTags
     * and what is recursively found in eclosedPushables follows.
     */
    private final Map<String, Set<String>> mTagMap;
    /**
     * Storage for handled Metadata tags.
     */
    protected final SparseArray<String> mMetadataMap;
    /**
     * The result of the Xml parsing. Brand new objects.
     */
    protected Map<String, List<XmlPushable>> mFilledObjectMap;
    
    /**
     * Constructor.
     * @param parser Preferred XmlPullParser class.
     * @param pushableMap Registered objects Map.
     */
    public XmlFiller(XmlPullParser parser) {
        mParser = parser;
        mPushableMap = new HashMap<String, XmlBuilder<XmlPushable>>();
        // TODO fill mTagMap here for the input pushableMap
        mTagMap = new HashMap<String, Set<String>>();
        // List of filled objects
        mFilledObjectMap = new HashMap<String, List<XmlPushable>>();
        
        // building the Metadata lookup table
        mMetadataMap = new SparseArray<String>();
        mMetadataMap.put(XmlPullParser.CDSECT, CDSECT_TAG);
        mMetadataMap.put(XmlPullParser.COMMENT, COMMENT_TAG);
        mMetadataMap.put(XmlPullParser.DOCDECL, DOCDECL_TAG);
        mMetadataMap.put(XmlPullParser.PROCESSING_INSTRUCTION, PROCESSING_TAG);
    }

    /**
     * Starts the filling process of registered objects, pulling tag data from the XmlPullParser.
     * This function will always fill a registered object starting from the outermost start tag
     * found descending the Xml hierarchy.
     * Once this tag is found, no other registered tag is taken into consideration until an end tag
     * is found.
     * The filling process respect the insertion order. If two or more objects with the same tag
     * have been registered, they will be pushed in order (FIFP, first in first pushed).
     * This function doesn't reset the internal list of registered objects at the end, call @c reset() 
     * in order to do that.
     * @throws XmlPushableException Thrown by this class, mostly related to parser positioning.
     * @throws XmlPullParserException Thrown by the XmlPullParser directly.
     * @throws IOException Thrown by the XmlPullParser directly.
     */
    final public <E extends XmlPushable> void parse() throws XmlPushableException, XmlPullParserException, IOException {

        if (mParser == null) throw new XmlPushableException("The XmlPullParser has not been set");
        if (mPushableMap == null) throw new XmlPushableException("The Registered Items Map has not been set");
        if (mPushableMap.isEmpty()) return;

        int currentEvent = mParser.getEventType();
        
        List<StringBuilder> textStack = new LinkedList<StringBuilder>();
        String currentTag = "";
        String startTag = "";
        XmlPushable rootItem = null;
        Set<String> acceptedTags = null;
        StringBuilder textBuilder = null;
        
        while (currentEvent != XmlPullParser.END_DOCUMENT) {
            if(currentEvent == XmlPullParser.START_TAG) {
                currentTag = mParser.getName();
                ///////////////////////////////////////////////////////////
                if (rootItem == null) {
                    // Looking for a registered item and get the builder
                    XmlBuilder<XmlPushable> builder = mPushableMap.get(currentTag);
                    rootItem = builder.getInstance();
                    if (rootItem != null) {
                        // Adding new instance to the Filled Object map
                        mFilledObjectMap.get(currentTag).add(rootItem);
                        // Getting the accepted tag list.
                        acceptedTags = mTagMap.get(currentTag);
                        startTag = currentTag;
                    }
                }
                if (rootItem != null) {
                    // Pushing just if the currentTag is part of the "accepted" tags.
                    if (acceptedTags.contains(currentTag)) {
                        rootItem.pushStartTag(currentTag);
                        for (int i = 0; i < mParser.getAttributeCount(); ++i) {
                            rootItem.pushAttribute(currentTag, mParser.getAttributePrefix(i), mParser.getAttributeName(i), mParser.getAttributeValue(i));
                        }
                        textStack.add(textBuilder); // tail push
                        textBuilder = new StringBuilder();
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
                    XmlBuilder<XmlPushable> builder = mPushableMap.get(currentTag);
                    rootItem = builder.getInstance();
                    if (rootItem != null) {
                        // Adding new instance to the Filled Object map
                        mFilledObjectMap.get(currentTag).add(rootItem);
                        // Getting the accepted tag list.
                        acceptedTags = mTagMap.get(currentTag);
                        startTag = currentTag;
                    }
                }
                if (rootItem != null) {
                    // Pushing just if the currentTag is part of the "accepted" tags.
                    if (acceptedTags.contains(currentTag)) {
                        rootItem.pushStartTag(currentTag);
                        rootItem.pushText(currentTag, mParser.getText());
                        rootItem.pushEndTag(currentTag);
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
                            rootItem.pushText(currentTag, textBuilder.toString());
                        }
                        rootItem.pushEndTag(currentTag);
                        textBuilder = textStack.remove(textStack.size() - 1); // tail pop
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

    /**
     * Registers an XmlPushable type to fill (this method will use reflection). The class
     * needs to have an XmlTag annotation or the this methodr will throw a RuntimeException
     * (TODO, a parser to enforce the presence of the annotation at compile time).
     * @param clazz The XmlPushable type.
     * @param xmlBuilder The Builder class which will be used for object creation.
     */
    public void registerNode(Class<? extends XmlPushable> clazz, XmlBuilder<? extends XmlPushable> xmlBuilder) {
        // Gets the root annotation
        if (clazz.isAnnotationPresent(XmlTag.class)) {
            // Gets the root tag from annotations
            XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
            String rootTag = xmlTag.value();
            // Just to need to check in one of the maps if the rootTag is already registered.
            if (mTagMap.get(rootTag) == null) {
                // Creates the tag set to insert in the tag map.
                Set<String> tagSet = new HashSet<String>();
                mTagMap.put(rootTag, tagSet);
                // Saves the builder.
                mPushableMap.put(rootTag, (XmlBuilder<XmlPushable>) xmlBuilder);
                // Creates the list of instances in the Filled Object map
                mFilledObjectMap.put(rootTag, new ArrayList<XmlPushable>());
                
                // Starts to recursively collect the additional tags
                collectTags(clazz, tagSet);
            }
        } else {
            throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        }
    }

    /**
     * Resets the list of registered objects.
     */
    public void reset() {
        mPushableMap.clear();
        mTagMap.clear();
        mFilledObjectMap.clear();
    }
    
    /**
     * Gets an instance of the desired XmlPushable class, if at least one instance has been correctly
     * pulled out of the XmlPullParser.
     * @param clazz The XmlPushable class type.
     * @return A filled instance of the specified class, if any. Null if none was found.
     */
    public <T extends XmlPushable> T getFirstInstanceOf(Class<T> clazz) {
        String rootTag = "";
        // Gets the root annotation
        if (clazz.isAnnotationPresent(XmlTag.class)) {
            XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
            rootTag = xmlTag.value();
        }
        return getFirstInstanceOf(rootTag);
    }
    
    /**
     * Gets an instance of the desired XmlPushable class, if at least one instance has been correctly
     * pulled out of the XmlPullParser.
     * @param tagName The tag of a previously registered XmlPushable.
     * @return A filled instance of the specified tag, if any. Null if none was found.
     */
    public <T extends XmlPushable> T getFirstInstanceOf(String tagName) {
        T instance = (T) mFilledObjectMap.get(tagName).get(0);
        return instance;
    }
    
    /**
     * Internal recursive method to collect the tags accepted by the XmlPushable object.
     * @param clazz
     * @param tagStack
     */
    private <T extends XmlPushable> void collectTags(Class<T> clazz, Set<String> tagStack) {
        // Gets the root annotation
        if (clazz.isAnnotationPresent(XmlTag.class)) {
            XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
            String rootTag = xmlTag.value();
            tagStack.add(rootTag);
            
            String[] additionalTags = xmlTag.additionalTags();
            for (int i = 0; i < additionalTags.length; ++i) {
                tagStack.add(additionalTags[i]);
            }
            
            Class<? extends XmlPushable>[] enclosedPushables = xmlTag.enclosedPushables();
            for (int i = 0; i < enclosedPushables.length; ++i) {
                collectTags(enclosedPushables[i], tagStack);
            }
        } else {
            throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        }
    }
    
    /**
     * Gets a @c List containing the objects created after parsing the Xml file.
     * @param clazz The tag of the desired type (same as @c registerNode()).
     * @return A list, an empty one if the requested tag has not be found during the parsing. A @c null pointershould never be returned.
     */
    public <E extends XmlPushable> List<E> getInstanceListOf(Class<E> clazz) {
        String rootTag = "";
        // Gets the root annotation
        if (clazz.isAnnotationPresent(XmlTag.class)) {
            XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
            rootTag = xmlTag.value();
        }
        return getInstanceListOf(rootTag);
    }
    
    /**
     * Gets a @c List containing the objects created after parsing the Xml file.
     * @param tag The tag of the desired type (same as @c registerNode()).
     * @return A list, an empty one if the requested tag has not be found during the parsing.
     */
    public <E extends XmlPushable> List<E> getInstanceListOf(String tag) {
        List<XmlPushable> filledInstances = mFilledObjectMap.get(tag);
        
        List<E> returnedInstances = new ArrayList<E>();
        if (filledInstances != null) {
            returnedInstances.addAll((Collection<? extends E>) filledInstances);
        }
        return returnedInstances;
    }
    /**
     * Internal implementation of the Iterator interface.
     * @author Andrea Richiardi
     * @param <E>
     *//*
    private class XmlFillerIterator<E> implements Iterator<E> {

        int mInitialModCount;
        
        XmlFillerIterator() {
            mInitialModCount = mModCount;
        }
        
        final void checkForComodification() {
            if (mModCount != mInitialModCount)
                throw new ConcurrentModificationException();
        }

        
        @Override
        public boolean hasNext() {
            checkForComodification();
            
            return false;
        }

        @Override
        public E next() {
            checkForComodification();
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }*/
 /*   
    *//**
     * Internal implementation of the Iterator interface.
     * @author Andrea Richiardi
     * @param <E>
     *//*
    final class EmptyIterator<E> implements Iterator<E> {

        EmptyIterator() {  do nothing  }
        
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }*/
}
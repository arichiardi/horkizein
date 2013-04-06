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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.SparseArray;

/**
 * This class performs objects de-serialization from Xml files.
 * You can register classes in order for their tags to be accepted as "pushable" and then
 * retrieve their instances after the parsing ends.
 * An XmlPushable class requires the {@link XmlTag} annotation or a RuntimeException will be
 * raised.<br/>
 * XmlFiller includes a buffer for multiple TEXT events pulled from the parser. For this reason,
 * pushText() is called exactly once for each tag (if there is text to push).<br>
 * Metadata content is handled the same way as standard tags: every time a Metadata event is pulled,
 * XmlFiller calls both pushStartTag() and pushEndTag(), plus pushText() if necessary (once).
 * To register a metadata object please use the static field {@link XmlFiller#CDSECT_TAG},
 * {@link XmlFiller#COMMENT_TAG}, {@link XmlFiller#DOCDECL_TAG} or {@link XmlFiller#PROCESSING_TAG}
 * in your implementation.
 * XmlFiller's internal data structures are not thread-safe and synchronization must be implemented
 * externally.
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
    public XmlFiller(XmlPullParser parser) {
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

    /**
     * Set the input and resets the underlying parser. This method accepts null
     * in both fields. 
     * @param inputStream An input stream. If null it will stop parsing and reset the state, allowing the parser to free resources.
     * @param inputEncoding The source encoding. If null the parser will try to determine the encoding from the Xml preamble (1.0 specification).
     * @throws XmlPullParserException Thrown by the XmlPullParser instance.
     */
    public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
        mParser.setInput(inputStream, inputEncoding);
    }
    /**
     * Set the input and resets the underlying parser. The parser will try to determine the encoding
     * from the Xml preamble (1.0 specification). This method accepts a null parameter.
     * @param inputStream An input stream. If null it will stop parsing and reset the state, allowing the parser to free resources.
     * @throws XmlPullParserException Thrown by the XmlPullParser instance.
     */
    public void setInput(InputStream inputStream) throws XmlPullParserException {
        mParser.setInput(inputStream, null);
    }
    /**
     * Set the input and resets the underlying parser. The parser will try to determine the encoding
     * from the Xml preamble (1.0 specification). This method accepts a null parameter.
     * @param in An input reader. If null it will stop parsing and reset the state, allowing the parser to free resources.
     * @throws XmlPullParserException Thrown by the XmlPullParser instance.
     */
    public void setInput(Reader in) throws XmlPullParserException {
        mParser.setInput(in);
    }
    /**
     * Start parsing the Xml file, pulling tag data from the XmlPullParser instance.
     * This function will always fill a registered object starting from the outermost matching start tag.
     * Once this tag is found, no other registered tag is taken into consideration until an end tag
     * is found, pushing all the annotated {@link  XmlTag#enclosedPushables} and {@link XmlTag#additionalTags} tags.
     * The filling process respect the finding order. If two or more registered tags have been found in
     * the Xml file, the new instances of XmlPushable objects will reflect their order.
     * This method neither reset the internal list of registered objects nor the XmlPullParser.
     * @throws XmlPullParserException Thrown by the XmlPullParser instance.
     * @throws IOException Thrown by the XmlPullParser instance if some IO problem occurs.
     * @throws CloneNotSupportedException Thrown in case the implementation of XmlPushable doens't provide a valide clone() method.
     */
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
                        rootItem.pushStartTag(currentTag);
                        for (int i = 0; i < mParser.getAttributeCount(); ++i) {
                            rootItem.pushAttribute(currentTag, mParser.getAttributePrefix(i), mParser.getAttributeName(i), mParser.getAttributeValue(i));
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
                        textBuilder = textStack.remove(textStack.size() - 1); // tail pop
                        rootItem.pushEndTag(currentTag);
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
     * Register an XmlPushable (this method will use reflection). The class needs
     * {@link XmlTag} annotation or this method will throw a RuntimeException.
     * (TODO, a custom parser to enforce the presence of the annotation at compile time).
     * @param clazz The XmlPushable class object to register.
     * @param xmlPushable The class that will be used for instance creation.
     */
    public void registerNode(XmlPushable<?> xmlPushable) {
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

    /**
     * Reset the list of registered objects and other internal data structures.
     */
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
    
    /**
     * Build the first instance of the desired type, if at least one instance has been correctly
     * parsed from the Xml. A RuntimeException is thrown if the XmlPushable class has not been 
     * annotated with {@link XmlTag}, because XmlFiller cannot identify the Xml tag without it.
     * <p>
     * Note: The registration is always based on {@link XmlTag}'s <code>value</code> and this parameterized
     * method enforces the constraint between builder and built object. ClassCastException may occure in
     * case of wrong assignment of the return type (Java would infer the wrong type).
     * @param clazz An class object of a previously registered XmlPushable<T> type.
     * @return A filled instance of the specified class, null if none was found.
     */
    public <T, E extends XmlPushable<T>> T buildFirstOf(Class<E> clazz) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        T built = null;
        if (rootTag != null) {
            // Workaround for:
            //  XmlFiller.java:[294,35] invalid inferred types for T,K; inferred type does not conform to declared bound(s)
            E pushable = firstOf(rootTag);
            if (pushable != null) {
                built = pushable.build();
            }
        }
        return built;
    }
    
    /**
     * Build the first instance of the desired type, if at least one instance has been correctly
     * parsed from the Xml. A RuntimeException is thrown if the XmlPushable class has not been 
     * annotated with {@link XmlTag}, because XmlFiller cannot identify the Xml tag without it.
     * <p>
     * Note: The registration is always based on {@link XmlTag}'s <code>value</code> and this parameterized
     * method enforces the constraint between builder and built object. ClassCastException may occure in
     * case of wrong assignment of the return type (Java would infer the wrong type).
     * @param tag The tag of a previously registered XmlPushable.
     * @return A filled instance of the specified tag, null if none was found.
     */
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
    
    /**
     * Build a list of instances of the desired type. A RuntimeException is thrown if the XmlPushable
     * class has not been annotated with {@link XmlTag}, because XmlFiller cannot identify the Xml tag without it.
     * <p>
     * Note: The registration is always based on {@link XmlTag}'s <code>value</code> and this parameterized
     * method enforces the constraint between builder and built object. ClassCastException may occure in
     * case of wrong assignment of the return type (Java would infer the wrong type).
     * @param clazz The class object of a previously registered XmlPushable.
     * @return A List. An empty List (not null) if the requested Xml tag has not be found during the parsing.
     * The returned List implementation is ArrayList. The list is a copy of the internal list,
     * but the XmlPushable(s) in it are shared between the client and XmlFiller (XmlFiller
     * is not inherently thread-safe). Resetting XmlFiller will reset the internal data structure
     * and will let the client handle the references.
     */
    public <T, E extends XmlPushable<T>> List<T> buildListOf(Class<E> clazz) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        return this.<T>buildListOf(rootTag);
    }
    
    /**
     * Build a list of instances of the desired type.
     * <p>
     * Note: The registration is always based on {@link XmlTag}'s <code>value</code> and this parameterized
     * method enforces the constraint between builder and built object. ClassCastException may occure in
     * case of wrong assignment of the return type (Java would infer the wrong type).
     * @param clazz The class object of a previously registered XmlPushable.
     * @return A List. An empty List (not null) if the requested Xml tag has not be found during the parsing.
     * The returned List implementation is ArrayList. The list is a copy of the internal list,
     * but the XmlPushable(s) in it are shared between the client and XmlFiller (XmlFiller
     * is not inherently thread-safe). Resetting XmlFiller will reset the internal data structure
     * and will let the client handle the references.
     */
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
    
    /**
     * Get the first instance of the desired XmlPushable class, if at least one instance has been correctly
     * parsed from the Xml. A RuntimeException is thrown if the class has not been annotated with {@link XmlTag}.
     * Note: because of Java type erasure, the type T in XmlPushable<T> is not known at runtime. The registration
     * is always based on annotation's <code>value</code>.
     * @param clazz An class object of a previously registered XmlPushable<T> type.
     * @return A filled instance of the specified class, null if none was found.
     */
    public <E extends XmlPushable<?>> E firstOf(Class<E> clazz) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        if (rootTag != null) {
            // Workaround for:
            //  XmlFiller.java:[294,35] invalid inferred types for T,K; inferred type does not conform to declared bound(s)
            return firstOf(rootTag);
        } else {
            return null;
        }
    }
    
    /**
     * Get the first instance of the desired XmlPushable tag, if at least one instance has been correctly
     * parsed from the Xml.
     * @param tag The tag of a previously registered XmlPushable.
     * @return A filled instance of the specified tag, null if none was found.
     */
    public <E extends XmlPushable<?>> E firstOf(String tag) {
        List<XmlPushable<?>> pushableList = mXmlPushableMap.get(tag);
        if (pushableList != null) {
            return (E) pushableList.get(0);
        }
        return null;
    }
    
    /**
     * Get a List containing the XmlPushable(s) created after parsing the Xml file.
     * A RuntimeException is thrown if the class has not been annotated with {@link XmlTag}.
     * @param clazz The class object of a previously registered XmlPushable.
     * @return A List. An empty List (not null) if the requested tag has not be found during the parsing.
     * The returned List implementation is ArrayList. The list is a copy of the internal list,
     * but the XmlPushable(s) in it are shared between the client and XmlFiller (XmlFiller
     * is not inherently thread-safe). Resetting XmlFiller will reset the internal data structure
     * and will let the client handle the references.
     */
    public <E extends XmlPushable<?>> List<E> listOf(Class<E> clazz) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        return listOf(rootTag);
    }
    
    /**
     * Get a List containing the XmlPushable(s) created after parsing the Xml file.
     * @param tag The tag of a previously registered XmlPushable.
     * @return A List. An empty List if the requested tag has not be found during the parsing.
     * The returned List implementation is ArrayList. The list is a copy of the internal list,
     * but the XmlPushable(s) in it are shared between the client and XmlFiller (XmlFiller
     * is not inherently thread-safe). Resetting XmlFiller will reset the internal data structure
     * and will let the client handle the references.
     */
    public <E extends XmlPushable<?>> List<E> listOf(String tag) {
        List<XmlPushable<?>> pushableList = mXmlPushableMap.get(tag);
        List<E> returnedInstances = new ArrayList<E>();
        if (pushableList != null) {
            returnedInstances.addAll((Collection<? extends E>) pushableList);
        }
        return returnedInstances;
    }
    
    /**
     * Return an Iterator over the underlying parsed data. The iterator is fail-fast.
     * * A RuntimeException is thrown if the class has not been annotated with {@link XmlTag}.
     * @param clazz The class object of a previously registered XmlPushable.
     * @return An Iterator. An empty Iterator if the requested tag has not be found during the parsing.
     */
    public <E extends XmlPushable<?>> Iterator<E> iterateOver(Class<E> clazz) {
        // Is annotated?
        if (!clazz.isAnnotationPresent(XmlTag.class)) throw new RuntimeException(clazz.getCanonicalName() + " is declared XmlPushable but it doesn't have any @XmlTag annotation.");
        
        XmlTag xmlTag = clazz.getAnnotation(XmlTag.class);
        String rootTag = xmlTag.value();
        return iterateOver(rootTag);
    }
    
    /**
     * Return an Iterator over the underlying parsed data. The Iterator is fail-fast.
     * * A RuntimeException is thrown if the class has not been annotated with {@link XmlTag}.
     * @param tag The tag of a previously registered XmlPushable.
     * @return An Iterator. An empty Iterator if the requested tag has not be found during the parsing.
     */
    public <E extends XmlPushable<?>> Iterator<E> iterateOver(String tag) {
        List<XmlPushable<?>> pushableList = mXmlPushableMap.get(tag);
        if (pushableList != null) {
            return (Iterator<E>) pushableList.iterator();
        } else {
            return new EmptyIterator<E>();
        }
    }
    
    /**
     * Implementation of an Empty iterator to return to clients when the tag has not been found.
     * @param <E>
     */
    private class EmptyIterator<E> implements Iterator<E> {
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
            throw new UnsupportedOperationException("Doesn't support remove().");
        }
    }
}
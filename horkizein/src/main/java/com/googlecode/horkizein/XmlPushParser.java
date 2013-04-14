package com.googlecode.horkizein;

import java.io.IOException;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

/**
 * Interface for object de-serialization from Xml files.
 * You can register classes in order for their tags to be "pushed", then retrieve the created instance
 * at the end of the parsing.<br/>
 * Implementations of this class should include a buffer for multiple TEXT events pulled from the parser. The expected
 * behavior of XmlPushable.pushText() is that it needs to be called exactly once per tag (if there is text to push).<br>
 * Metadata content is handled the same way as standard tags: every time a Metadata event is pulled,
 * The implementation needs to call both pushStartTag() and pushEndTag(), plus pushText() if necessary (once).
 * To register a metadata object please use the static fields {@link XmlPushParserImpl1#CDSECT_TAG},
 * {@link XmlPushParserImpl1#COMMENT_TAG}, {@link XmlPushParserImpl1#DOCDECL_TAG} or {@link XmlPushParserImpl1#PROCESSING_TAG}
 * in your implementation.
 * <p>
 * Note: The XmlPushable class requires the {@link XmlTag} annotation or a RuntimeException will be
 * raised when the implementation will try to get the tag definition {@link XmlTag}.<br/>
 */
public interface XmlPushParser {

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
     * Set the input. This call will reset the underlying parser. This method accepts null
     * in both fields. 
     * @param inputStream An input stream. If null it will stop parsing and reset the state, allowing the parser to free resources.
     * @param inputEncoding The source encoding. If null the parser will try to determine the encoding from the Xml preamble (1.0 specification).
     * @throws XmlPullParserException Thrown by the XmlPullParser instance.
     */
    void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException;

    /**
     * Set the input. This call will reset the underlying parser. The parser will try to determine the encoding
     * from the Xml preamble (1.0 specification). This method accepts a null parameter.
     * @param inputStream An input stream. If null it will stop parsing and reset the state, allowing the parser to free resources.
     * @throws XmlPullParserException Thrown by the XmlPullParser instance.
     */
    void setInput(InputStream inputStream) throws XmlPullParserException;

    /**
     * Set the input. This call will reset the underlying parser. The parser will try to determine the encoding
     * from the Xml preamble (1.0 specification). This method accepts a null parameter.
     * @param in An input reader. If null it will stop parsing and reset the state, allowing the parser to free resources.
     * @throws XmlPullParserException Thrown by the XmlPullParser instance.
     */
    void setInput(Reader in) throws XmlPullParserException;

    /**
     * Start parsing the Xml file, pulling tag data from the underlying XmlPullParser instance.
     * This function should always fill a registered object starting from the outermost matching start tag.
     * Once this tag is found, the other registered tags ({@link  XmlTag#enclosedPushables} and {@link XmlTag#additionalTags})
     * are pushed into the corresponding XmlPushable, until an end tag is found.<br/>
     * The filling process should respect the finding order. If two or more registered tags have been found in
     * the Xml file, the new instances of XmlPushable objects will reflect their order.<br/>
     * This method neither resets the internal data nor the underlying XmlPullParser at the end of the execution.
     * @throws XmlPullParserException Thrown by the XmlPullParser instance.
     * @throws IOException Thrown by the XmlPullParser instance if some IO problem occurs.
     * @throws CloneNotSupportedException Thrown in case the implementation of XmlPushable doens't provide a valide clone() method.
     */
    void parse() throws XmlPullParserException, IOException;

    /**
     * Register an XmlPushable (this method will use reflection). The registered class needs
     * {@link XmlTag} annotation or this method will throw a RuntimeException.
     * @param clazz The XmlPushable class object to register.
     * @param xmlPushable The class that will be used for instance creation.
     */
    void addNode(XmlPushable<?> xmlPushable);

    /**
     * Reset the list of registered objects and other internal data structures.
     */
    void reset();

    /**
     * Build the first instance of the desired type, if at least one corresponding XmlPushable has been correctly
     * parsed from the Xml. A RuntimeException is thrown if the XmlPushable class has not been 
     * annotated with {@link XmlTag}, because XmlPushParser cannot identify the Xml tag without it.
     * <p>
     * Note: The registration is always based on the {@link XmlTag} <code>value</code> and this parameterized
     * method enforces the constraint between builder and built object. ClassCastException may occur in
     * case of mismatching return type (Java would infer the wrong one).
     * @param clazz An class object of a previously registered XmlPushable<T> type.
     * @return A filled instance of the specified class, null if none was found.
     */
    <T, E extends XmlPushable<T>> T buildFirstOf(Class<E> clazz);

    /**
     * Build the first instance of the desired type, if at least one corresponding XmlPushable has been correctly
     * parsed from the Xml.
     * <p>
     * Note: The registration is always based on the {@link XmlTag} <code>value</code> and this parameterized
     * method enforces the constraint between builder and built object. ClassCastException may occur in
     * case of mismatching return type (Java would infer the wrong one).
     * @param tag The tag of a previously registered XmlPushable.
     * @return A filled instance of the specified tag, null if none was found.
     */
    <T> T buildFirstOf(String tag);

    /**
     * Build a list of instances of the desired type. The returned list is a mutable ArrayList.
     * A RuntimeException is thrown if the XmlPushable<T> class has not been annotated with {@link XmlTag},
     * because XmlPushParser cannot identify the Xml tag without it.
     * <p>
     * Note: The registration is always based on the {@link XmlTag} <code>value</code> and this parameterized
     * method enforces the constraint between builder and built object. ClassCastException may occur in
     * case of mismatching return type (Java would infer the wrong one).
     * @param clazz The class object of a previously registered XmlPushable.
     * @return A List. An empty List (not null) if the requested Xml tag has not be found during the parsing.
     * The returned List implementation is ArrayList. The list is a copy of the internal list,
     * but the XmlPushable(s) in it are shared between the client and XmlPushParser (which
     * is not inherently thread-safe). Resetting XmlPushParser will reset the internal data structure
     * and will let the client handle the references.
     */
    <T, E extends XmlPushable<T>> List<T> buildListOf(Class<E> clazz);

    /**
     * Build a list of instances of the desired type. The returned list is a mutable ArrayList.
     * <p>
     * Note: The registration is always based on the {@link XmlTag} <code>value</code> and this parameterized
     * method enforces the constraint between builder and built object. ClassCastException may occur in
     * case of mismatching return type (Java would infer the wrong one).
     * @param tag The tag of a previously registered XmlPushable.
     * @return A List. An empty List (not null) if the requested Xml tag has not be found during the parsing.
     * The returned List implementation is ArrayList. The list is a copy of the internal list,
     * but the XmlPushable(s) in it are shared between the client and XmlPushParser (which
     * is not inherently thread-safe). Resetting XmlPushParser will reset the internal data structure
     * and will let the client handle the references.
     */
    <T> List<T> buildListOf(String tag);

    /**
     * Get the first instance of the desired XmlPushable class, if at least one instance has been correctly
     * parsed from the Xml. A RuntimeException is thrown if the class has not been annotated with {@link XmlTag}.
     * Note: because of Java type erasure, the type T in XmlPushable<T> is not known at runtime. The registration
     * is always based on annotation's <code>value</code>.
     * @param clazz An class object of a previously registered XmlPushable<T> type.
     * @return A filled instance of the specified class, null if none was found.
     */
    <E extends XmlPushable<?>> E firstOf(Class<E> clazz);

    /**
     * Get the first instance of the desired XmlPushable tag, if at least one instance
     * has been correctly parsed from the Xml.
     * @param tag The tag of a previously registered XmlPushable.
     * @return A filled instance of the specified tag, null if none was found.
     */
    <E extends XmlPushable<?>> E firstOf(String tag);

    /**
     * Get a List containing the XmlPushable(s) created after parsing the Xml file. The returned list
     * is immutable (obtained through Collections.unmodifiableList(list)).
     * A RuntimeException is thrown if the class has not been annotated with {@link XmlTag}.
     * @param clazz The class object of a previously registered XmlPushable.
     * @return A List. An empty List (not null) if the requested tag has not be found during the parsing.
     * The returned List implementation is ArrayList. The list is a copy of the internal list,
     * but the XmlPushable(s) in it are shared between the client and XmlPushParser (which
     * is not inherently thread-safe). Resetting XmlPushParser will reset the internal data structure
     * and will let the client handle the references.
     */
    <E extends XmlPushable<?>> List<E> listOf(Class<E> clazz);

    /**
     * Get a List containing the XmlPushable(s) created after parsing the Xml file. The returned list
     * is immutable (obtained through Collections.unmodifiableList(list)).
     * @param tag The tag of a previously registered XmlPushable.
     * @return A List. An empty List if the requested tag has not be found during the parsing.
     * The returned List implementation is ArrayList. The list is a copy of the internal list,
     * but the XmlPushable(s) in it are shared between the client and XmlPushParser (which
     * is not inherently thread-safe). Resetting XmlPushParser will reset the internal data structure
     * and will let the client handle the references.
     */
    <E extends XmlPushable<?>> List<E> listOf(String tag);

}

package com.googlecode.horkizein;

public interface XmlPrototype<T> {

    /**
     * Clone the object. The newly object is *not* supposed to deep copy the fields that will
     * be filled by XmlFiller, though this depends on the implementation and object structure.
     * For example, dependencies that need to be passed to the final object (the one created by
     * build(), which is preferably immutable) have to be copied with this method.<br/><br/>
     * <i>An example:</i>(TODO example)
     * <pre>
     * &#64;XmlTag 
     *   value = "helloWorld",
     *   additionalTags = { "c", "java", XmlFiller.CDSECT_TAG }
     * )
     * public class HelloWorldObject implements XmlPushable {
     * ...
     * </pre>
     * @return
     */
    XmlPushable<T> shallowClone();
}

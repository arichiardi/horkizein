package com.googlecode.horkizein;

/**
 * Object producer. Prototype pattern. 
 * @param <T> The object to clone.
 */
public interface XmlPrototype<T> {

    /**
     * Clone the object. The new object is *not* supposed to deep copy, because fields will
     * be filled by XmlPushParser, although this depends on the implementation and object structure.
     * Dependencies that need to be passed to the final object (the one created by build()) should
     * to be copied within this method.<br/><br/>
     * <i>An example:</i>
     * <pre>
     * &#64XmlTag(
     *     value = "flat_obj",
     *     additionalTags = { FlatObjectDAO.BOOLEAN_TAG, FlatObjectDAO.STRING_TAG, 
     *                        FlatObjectDAO.INTEGER_TAG, FlatObjectDAO.DOUBLE_TAG }
     * )
     * public class FlatObjectDAO implements XmlPushable<FlatObject> {
     *     // Dependency
     *     private final XmlSerializer mSerializer;
     *     ...
     *     
     *     &#64Override
     *     public XmlPushable<FlatObject> shallowClone() {
     *         return new FlatObjectDAO(mSerializer);
     *     }
     *     ...
     * </pre>
     * @return A copy of the current object.
     */
    XmlPushable<T> shallowClone();
}

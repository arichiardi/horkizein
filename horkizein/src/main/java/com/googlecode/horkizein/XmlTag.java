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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the entry point for the hierarchy of classes that needs to be filled with
 * the data parsed by XmlFiller. This annotated node acts as root, with children
 * pushed directly to it. The annotation is not &#64;Inherited, meaning that 
 * it is responsibility of the each and every class to declare its own &#64;XmlTag.
 * its parent.<br/><br/>
 * <i>Sample usage:</i>
 * <pre>
 * &#64;XmlTag (
 *   value = "helloWorld",
 *   additionalTags = { "c", "java", XmlFiller.CDSECT_TAG }
 * )
 * public class HelloWorldObject implements XmlPushable {
 * ...
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
    value = { ElementType.TYPE, ElementType.FIELD }
)
public @interface XmlTag {
    /**
     * Declares the root tag of this class. This tag will be used to identify
     * this class during the parsing and for instance retrieval.<br/><br/>
     * <i>Sample usage (short):</i>
     * <pre>
     * &#64;XmlTag("flat_obj")
     * public class FlatObject implements XmlPushable {
     * ...
     * </pre>
     * <i>Sample usage (long):</i>
     * <pre>
     * &#64;XmlTag(
     *  value = "flat_obj",
     *  ...
     * )
     * public class FlatObject implements XmlPushable {
     * ...
     * </pre>
     */
    String value() default "";
    
    /**
     * Declares the additional tag(s) this class accepts and handle when a push*() method is called.
     * The {@link  XmlTag#enclosedPushables} tags and {@link XmlTag#additionalTags} tags are merged.<br/><br/>
     * <i>Sample usage:</i>
     * <pre>
     * &#64;XmlTag (
     *  value = "flat_obj",
     *  additionalTags = { "boolean", "string", "integer", "double" } 
     * )
     * public class FlatObject implements XmlPushable {
     * ...
     * </pre>
     */
    String[] additionalTags() default { /* empty by default */ };
    /**
     * Declares XmlPushable sub-classes whose tags the XmlFiller has to push back to the annotated object.
     * These classes are inspected for {@link XmlTag} annotations and their tags recursively added to
     * the list of pushed tags for this object.
     * The {@link  XmlTag#enclosedPushables} tags and {@link XmlTag#additionalTags} tags are merged.<br/><br/>
     * <i>Sample usage:</i>
     * <pre>
     * &#64;XmlTag (
     *  value = "nested_obj1",
     *  enclosedPushables = FlatObject.class
     * )
     * public class NestedObject1 implements XmlPushable {
     * ...
     * </pre>
     */
    Class<? extends XmlPushable<?>>[] enclosedPushables() default { /* empty by default */ };
}

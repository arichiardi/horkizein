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
 * Annotation for a class that contributes to the Xml object graph.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XmlTag {
    /**
     * Declares the root tag this class accepts and handle when a push*() method is called.
     */
    String value();
    
    /**
     * Declares the additional tag(s) this class accepts and handle when a push*() method is called.
     * The {@code tags} tags and {@link #dependencies} discovered tags are merged.
     */
    String[] additionalTags() default { /* empty by default */ };
    /**
     * Declares HAS-A XmlPushable classes which the XmlFiller has to push back to this object
     * when parsing the Xml file. These classes are inspected for {@code XmlTag} annotations
     * and their tags recursively added to the list of pushed tags for this object.
     * The {@code tags} tags and {@link #dependencies} discovered tags are merged.
     * @return A list of classes that extend XmlPushable
     */
    Class<? extends XmlPushable>[] enclosedPushables() default { /* empty by default */ };
    /**
     * TODO XmlPushable Auto-discovery feature.
     */
    //boolean autodiscovery() default true;

}

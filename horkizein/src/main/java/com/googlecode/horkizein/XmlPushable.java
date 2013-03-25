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

/**
 * XmlPushable object contract. The classes implementing this interface are intended
 * to be used as mutable builders for the target value objects. The build() method is
 * never called by the framework, while clone() is necessary to build and store instances
 * internally.
 * The {@link XmlTag} annotation is necessary for recursively pushing the dependent objects.
 * @param <T> The (immutable) instance to build.
 */
public interface XmlPushable<T> extends XmlBuilder<T>, XmlPrototype<T> {
    /**
     * Pushes the opening tag event.
     * @param tag Tag name.
     */
    void pushStartTag(String tag);

    /**
     * Pushes attribute name/value to the current object. The prefix has not been tested yet. 
     * @param tag Tag name.
     * @param prefix Attribute prefix (ignored at the moment).
     * @param name Attribute name.
     * @param value Attribute value.
     */
    void pushAttribute(String tag, String prefix, String name, String value);

    /**
     * Pushes the tag text.
     * @param tag Tag name.
     * @param text Tag text.
     */
    void pushText(String tag, String text);

    /**
     * Pushes the closing tag event.
     * @param tag Tag name.
     */
    void pushEndTag(String tag);
}

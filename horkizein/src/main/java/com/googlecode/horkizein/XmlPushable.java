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
 * XmlPushable object contract. For a detailed explanation on how to specify the tags
 * that Horkizein is required to extract from the Xml file see {@link XmlTag}.
 */
public interface XmlPushable {
    /**
     * Pushes the opening tag event.
     * @param tag Tag name.
     */
    void pushStartTag(String tag);

    /**
     * Pushes attribute name/value to the current object.
     * @param tag    Tag name.
     * @param prefix Attribute prefix (ignored at the moment).
     * @param name    Attribute name.
     * @param value    Attribute value.
     */
    void pushAttribute(String tag, String prefix, String name, String value);

    /**
     * Pushes the tag text.
     * @param tag    Tag name.
     * @param text    Tag text.
     */
    void pushText(String tag, String text);

    /**
     * Pushes the closing tag event.
     * @param tag Tag name.
     */
    void pushEndTag(String tag);
}

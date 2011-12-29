/*
 ** Copyright 2011, Horkizein Open Source Android Library
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
package ar.android.horkizein;

/**
 * XmlPushable objects contract.
 */
public interface XmlPushable {

    /**
     * @brief Tag Getter
     * @return Tag name
     */
    String getTag();

    /**
     * Passes the current Start tag.
     * @param tag Tag name.
     */
    void pushStartTag(String tag);

    /**
     * Adds the attribute value of the given tag to the current object 
     * @param tag	Tag name
     * @param prefix Prefix of the attribute name, if any, otherwise null
     * @param name	Attribute name
     * @param value	Attribute value
     */
    void pushAttribute(String tag, String name, String value);

    /**
     * Sets the text related to the tag
     * @param tag	Tag name
     * @param text	Tag text
     */
    void pushText(String tag, String text);

    /**
     * Passes the current End tag.
     * @param tag Tag name.
     */
    void pushEndTag(String tag);

}

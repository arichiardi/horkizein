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
package com.googlecode.horkizein.obj;

/**
 * Implementation of XmlPushable which contains just text.
 */
public class TextObject {
    // This object tag
    public final static String TAG = "text_obj";

    private final String mText;
    
    /**
     * Ctor.
     * @param text Some text.
     */
    public TextObject(String text) { 
        mText = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;
        TextObject o = (TextObject)obj;
        return (mText == o.mText || (mText != null && mText.equals(o.mText)));
    }
}


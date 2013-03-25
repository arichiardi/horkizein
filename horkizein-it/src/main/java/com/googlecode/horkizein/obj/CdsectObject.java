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
 * Implementation of the CDSECT xml section as a XmlPushable metadata object.
 */
public class CdsectObject {
    // the text inside this xml section
    private final String mContent;
    private int mHashCode;

    /**
     * Ctor.
     * @param content Metadata content.
     */
    public CdsectObject(String content) {
        mContent = content;
        mHashCode = 7 + (mContent.hashCode() * 277);
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if((other == null) || (other.getClass() != this.getClass())) return false;

        CdsectObject o = (CdsectObject)other;
        return (mContent == o.mContent || (mContent != null && mContent.equals(o.mContent)));
    }

    public final String getContent() {
        return mContent;
    }
}


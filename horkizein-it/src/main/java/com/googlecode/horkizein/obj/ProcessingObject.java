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

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushParser;
import com.googlecode.horkizein.XmlWritable;

/**
 * Implementation of the COMMENT xml section as a XmlPushable metadata object.
 */
public class ProcessingObject implements XmlWritable {
    // This object tag
    public final static String TAG = XmlPushParser.COMMENT_TAG;

    // The text inside this xml section
    public final String mContent;
    private int mHashCode;

    /**
     * Ctor.
     * @param content Metadata content.
     */
    public ProcessingObject(String content) {
        mContent = content;
        mHashCode = 97 + (mContent.hashCode() * 653);
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        ProcessingObject o = (ProcessingObject)obj;
        return (mContent == o.mContent || (mContent != null && mContent.equals(o.mContent)));
    }
    
    public final String getContent() {
        return mContent;
    }

    @Override
    public void writeXml(XmlSerializer serializer) throws IOException, IllegalStateException, IllegalArgumentException {
        serializer.processingInstruction(mContent);
    }
}
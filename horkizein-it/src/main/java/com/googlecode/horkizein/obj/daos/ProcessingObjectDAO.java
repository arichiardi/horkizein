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
package com.googlecode.horkizein.obj.daos;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushParser;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.ProcessingObject;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Implementation of the PROCESSING xml section as a XmlPushable metadata object.
 */
@XmlTag(XmlPushParser.PROCESSING_TAG)
public class ProcessingObjectDAO implements XmlPushable<ProcessingObject>, XmlWriter {
    // This object tag
    public final static String TAG = XmlPushParser.PROCESSING_TAG;
 
    // Dependency
    private final XmlSerializer mSerializer;
    // watch dog
    private boolean mPushedStartTag;
    private boolean mPushedEndTag;

    // the text inside this xml section
    public String mProcessingContent;

    /**
     * Ctor.
     * @param serializer The serializer.
     */
    public ProcessingObjectDAO(XmlSerializer serializer) {
        mSerializer = serializer;
        mPushedEndTag = mPushedStartTag = false;
    }

    @Override
    public void attribute(String tag, String prefix, String name, String value) { /* do nothing */ }

    @Override
    public void startTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".startTag(" + tag + ")");
        if (tag.equals(TAG)) {
            mPushedStartTag = true;
        }
    }

    @Override
    public void text(String tag, String text) {
        if (tag.equals(TAG) && mPushedStartTag == true) {
            mProcessingContent = text;
            Log.d (Constants.PACKAGE_TAG_TEST, TAG + " pushed: " + text);
        } else {
            Log.d(Constants.PACKAGE_TAG_TEST, TAG + "NOT MINE");
        }
    }

    @Override
    public void endTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
        if (tag.equals(TAG) && mPushedStartTag == true) {
            mPushedEndTag = true;
        }
    }

    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
        return (mPushedStartTag && mPushedEndTag);
    }

    @Override
    public ProcessingObject build() {
        return new ProcessingObject(mProcessingContent);
    }

    @Override
    public XmlPushable<ProcessingObject> shallowClone() {
        return new ProcessingObjectDAO(mSerializer);
    }

    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mSerializer);
    }
}


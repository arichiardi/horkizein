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

import com.googlecode.horkizein.XmlFiller;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.CdsectObject;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Implementation of the CDSECT xml section as a XmlPushable metadata object.
 */
@XmlTag(XmlFiller.CDSECT_TAG)
public class CdsectObjectDAO implements XmlPushable<CdsectObject>, XmlWriter {

    // This object tag
    public final static String TAG = XmlFiller.CDSECT_TAG;

    // Dependency
    private final XmlSerializer mSerializer;
    
    // watch dog
    private boolean mPushedStartTag;
    private boolean mPushedEndTag;

    // the text inside this xml section
    public String mCdsectContent;

    /**
     * ctor
     * @param serializer The serializer.
     */
    public CdsectObjectDAO(XmlSerializer serializer) {
        mSerializer = serializer;
        mCdsectContent = "";
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) { /* do nothing */ }

    @Override
    public void pushStartTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag(" + tag + ")");
        if (tag.equals(TAG)) {
            mPushedStartTag = true;
        }
    }

    @Override
    public void pushText(String tag, String text) {
        if (tag.equals(TAG) && mPushedStartTag == true) {
            mCdsectContent = text;
            Log.d(Constants.PACKAGE_TAG_TEST, TAG + " pushed: " + text);
        } else {
            Log.d(Constants.PACKAGE_TAG_TEST, TAG + "NOT MINE");
        }
    }

    @Override
    public void pushEndTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
        if (tag.equals(TAG) && mPushedStartTag == true) {
            mPushedEndTag = true;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        CdsectObjectDAO o = (CdsectObjectDAO)obj;
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mCdsectContent: " + mCdsectContent + " - item.mCdsectContent: " + o.mCdsectContent);
        return (mCdsectContent == o.mCdsectContent || (mCdsectContent != null && mCdsectContent.equals(o.mCdsectContent)));
    }

    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
        return (mPushedStartTag && mPushedEndTag);
    }

    @Override
    public CdsectObject build() {
        return new CdsectObject(mCdsectContent);
    }
    
    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mSerializer);
    }

    @Override
    public XmlPushable<CdsectObject> shallowClone() {
        return new CdsectObjectDAO(mSerializer);
    }
}


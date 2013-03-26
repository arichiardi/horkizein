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
package com.googlecode.horkizein.obj.builders;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlFiller;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.obj.DocdeclObject;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Implementation of the DOCDECL xml section as a XmlPushable metadata object.
 */
@XmlTag(XmlFiller.DOCDECL_TAG)
public class DocdeclObjectDAO implements XmlPushable<DocdeclObject>, XmlWritable<DocdeclObject> {

    // This object tag
    public final static String TAG = XmlFiller.DOCDECL_TAG;

    // watch dog
    private boolean mPushedStartTag;
    private boolean mPushedEndTag;
    
    // the text inside this xml section
    private String mDocdeclContent;

    // Dependency
    private final XmlSerializer mSerializer;

    /**
     * ctor
     * @param serializer The serializer.
     */
    public DocdeclObjectDAO(XmlSerializer serializer) {
        mSerializer = serializer;
        mDocdeclContent = "";
        mPushedEndTag = mPushedStartTag = false;
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
            mDocdeclContent = text;
            Log.d (Constants.PACKAGE_TAG_TEST, TAG + " pushed: " + text);
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

    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
        return (mPushedStartTag && mPushedEndTag);
    }

    @Override
    public DocdeclObject build() {
        return new DocdeclObject(mDocdeclContent);
    }
    

    @Override
    public XmlPushable<DocdeclObject> shallowClone() {
        return new DocdeclObjectDAO(mSerializer);
    }


    @Override
    public void writeXml(DocdeclObject object) throws IOException, IllegalStateException, IllegalArgumentException {
        mSerializer.docdecl(object.getContent());
    }
}
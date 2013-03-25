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
import com.googlecode.horkizein.obj.NestedObject1;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Implementation of an XmlPushable which contains a FlatObject.
 */
@XmlTag (
    value = NestedObject1DAO.TAG,
    enclosedPushables = FlatObjectDAO.class
)
public class NestedObject1DAO implements XmlPushable<NestedObject1>, XmlWritable<NestedObject1> {

    // This object tag
    public final static String TAG = NestedObject1.TAG;

    // Dependency
    private final XmlSerializer mSerializer;
    
    // watch dog
    private boolean wdPushedStartTag;
    private boolean wdPushedEndTag;

    // child
    public final FlatObjectDAO mFlatObjectDAO;

    /**
     * Creates a NestedObject1 that contains a shallow copy of a FlatObject
     * @param serializer A serializer.
     */
    public NestedObject1DAO(XmlSerializer serializer) {
        mSerializer = serializer;
        mFlatObjectDAO = new FlatObjectDAO(serializer);
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushAttribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);
        if (wdPushedStartTag) {
            //if (wdFlatObjStartTag) {
            mFlatObjectDAO.pushAttribute(tag, prefix, name, value);
            //}
        }
    }


    @Override
    public void pushText(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);
        if (wdPushedStartTag) {
            //if (wdFlatObjStartTag) {
            mFlatObjectDAO.pushText(tag, text);
            //}
        }
    }


    @Override
    public void pushEndTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag() - TAG: " + tag);

        if(wdPushedStartTag) {
            if (mFlatObjectDAO != null) {
                mFlatObjectDAO.pushEndTag(tag);
            }
        }

        if (tag.equals(TAG) && wdPushedStartTag == true) {
            wdPushedStartTag = false;
            wdPushedEndTag = true;
        }
    }


    @Override
    public void pushStartTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag() - TAG: " + tag);
        if (tag.equals(TAG))
            wdPushedStartTag = true;

        if(wdPushedStartTag) {
            mFlatObjectDAO.pushStartTag(tag);
        }
    }

    @Override
    public void writeXml(NestedObject1 object) throws IOException, IllegalStateException, IllegalArgumentException {
        // Writing out
        mSerializer.startTag("", TAG);
        if(mFlatObjectDAO != null) {
            mFlatObjectDAO.writeXml(object.getFlatObject());
        }
        mSerializer.endTag("", TAG);
    }

    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
        return (mFlatObjectDAO.tagCheck() &&
                !wdPushedStartTag &&
                wdPushedEndTag);
    }

    @Override
    public NestedObject1 build() {
        return new NestedObject1(mFlatObjectDAO.build());
    }

    @Override
    public XmlPushable<NestedObject1> shallowClone() {
        return new NestedObject1DAO(mSerializer);
    }
}

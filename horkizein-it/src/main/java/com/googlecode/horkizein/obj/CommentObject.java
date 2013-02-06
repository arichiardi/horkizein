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
package com.googlecode.horkizein.obj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlFiller;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Implementation of the COMMENT xml section as a XmlPushable metadata object.
 */
public class CommentObject implements XmlPushable, XmlWritable {

    // This object tag
    public final static String TAG = XmlFiller.COMMENT_TAG;

    // watch dog
    private boolean mPushedStartTag;
    private boolean mPushedEndTag;

    // the text inside this xml section
    public String mCommentContent;

    /**
     * ctor
     * @param text Metadata content.
     */
    public CommentObject(String text) {
        mCommentContent = text;
        mPushedEndTag = mPushedStartTag = false;
    }

    /**
     * ctor
     */
    public CommentObject() {
        mCommentContent = "";
        mPushedEndTag = mPushedStartTag = false;
    }


    public void pushAttribute(String tag, String prefix, String name, String value) { /* do nothing */ }


    public void pushStartTag(String tag) {
        if (tag.equals(TAG)) {
            mPushedStartTag = true;
        }
    }


    public void pushText(String tag, String text) {
        if (tag.equals(TAG) && mPushedStartTag == true) {
            mCommentContent = text;
            Log.d (Constants.PACKAGE_TAG_TEST, TAG + " pushed: " + text);
            Log.d (Constants.PACKAGE_TAG_TEST, "---------------------");
        } else {
            Log.d(Constants.PACKAGE_TAG_TEST, TAG + "NOT MINE");
        }
    }


    public void pushEndTag(String tag) {
        if (tag.equals(TAG) && mPushedStartTag == true) {
            mPushedEndTag = true;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        CommentObject item = (CommentObject)obj;
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " 1: " + mCommentContent + "- 2: " + item.mCommentContent);
        return (mCommentContent.equals(item.mCommentContent));
    }


    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
        out.comment(mCommentContent);
    }

    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
        return (mPushedStartTag && mPushedEndTag);
    }

    /**

     */
    public Collection<String> pushableTags() {
        ArrayList<String> tags = new ArrayList<String>(1);
        tags.add(TAG);
        return tags;
    }

    /**

     */
    public String getTag() {
        return TAG;
    }
}
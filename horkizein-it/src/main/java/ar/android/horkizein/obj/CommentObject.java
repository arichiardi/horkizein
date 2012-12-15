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
package ar.android.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import ar.android.horkizein.XmlFiller;
import ar.android.horkizein.XmlPushable;
import ar.android.horkizein.XmlWritable;
import ar.android.horkizein.test.Constants;

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
    
    /**
     * @see ar.android.horkizein.xml.XmlPushable#getTag()
     */
    public String getTag() {
        return TAG;
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushAttribute(java.lang.String, java.lang.String, java.lang.String)
     */
    public void pushAttribute(String tag, String name, String value) { }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushText(java.lang.String, java.lang.String)
     */
    public void pushText(String tag, String text) {
        if (mPushedStartTag) {
            mCommentContent = text;
            Log.d (Constants.PACKAGE_TAG_TEST, TAG + " pushed: " + text);
            Log.d (Constants.PACKAGE_TAG_TEST, "---------------------");
        } else {
        	Log.d(Constants.PACKAGE_TAG_TEST, TAG + "NOT MINE");
        }
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushEndTag(java.lang.String)
     */
    public void pushEndTag(String tag) {
        if (tag.equals(TAG) && mPushedStartTag)
        	mPushedEndTag = true;
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushStartTag(java.lang.String)
     */
    public void pushStartTag(String tag) {
        if (tag.equals(TAG))
        	mPushedStartTag = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        CommentObject item = (CommentObject)obj;
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " 1: " + mCommentContent + "- 2: " + item.mCommentContent);
        return (mCommentContent.equals(item.mCommentContent));
    }

    /**
     * @see ar.android.horkizein.xml.XmlWritable#writeXml(org.xmlpull.v1.XmlSerializer)
     */
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
    	out.comment(mCommentContent);
    }
    
    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
    	return (mPushedStartTag || mPushedEndTag);
    }
}

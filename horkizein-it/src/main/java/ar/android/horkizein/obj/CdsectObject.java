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
import java.util.ArrayList;
import java.util.Collection;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import ar.android.horkizein.XmlFiller;
import ar.android.horkizein.XmlPushable;
import ar.android.horkizein.XmlWritable;
import ar.android.horkizein.test.Constants;

/**
 * Implementation of the CDSECT xml section as a XmlPushable metadata object.
 */
/**
 * @author kapitan
 *
 */
public class CdsectObject implements XmlPushable, XmlWritable {
    
	// This object tag
    public final static String TAG = XmlFiller.CDSECT_TAG;
    
    // watch dog
    private boolean mPushedStartTag;
    private boolean mPushedEndTag;
    
    // the text inside this xml section
    public String mCdsectContent;
    
    /**
     * Ctor.
     * @param text Metadata content.
     */
    public CdsectObject(String text) {
    	mCdsectContent = text;
    }
    
    /**
     * Ctor.
     */
    public CdsectObject() {
    	mCdsectContent = "";
    }

    /**
     * @see ar.android.horkizein.XmlPushable#pushAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void pushAttribute(String tag, String prefix, String name, String value) { /* do nothing */ }

    /**
     * @see ar.android.horkizein.XmlPushable#pushStartTag(java.lang.String)
     */
    public void pushStartTag(String tag) {
    	Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag(" + tag + ")");
        if (tag.equals(TAG)) {
        	mPushedStartTag = true;
        }
    }
    
    /**
     * @see ar.android.horkizein.XmlPushable#pushText(java.lang.String, java.lang.String)
     */
    public void pushText(String tag, String text) {
        if (tag.equals(TAG) && mPushedStartTag == true) {
            mCdsectContent = text;
            Log.d(Constants.PACKAGE_TAG_TEST, TAG + " pushed: " + text);
            Log.d(Constants.PACKAGE_TAG_TEST, "---------------------");
        } else {
        	Log.d(Constants.PACKAGE_TAG_TEST, TAG + "NOT MINE");
        }
    }

    /**
     * @see ar.android.horkizein.XmlPushable#pushEndTag(java.lang.String)
     */
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

        CdsectObject item = (CdsectObject)obj;
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mCdsectContent: " + mCdsectContent + " - item.mCdsectContent: " + item.mCdsectContent);
        return (mCdsectContent.equals(item.mCdsectContent));
    }

    /**
     * @see ar.android.horkizein.XmlWritable#writeXml(org.xmlpull.v1.XmlSerializer)
     */
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
    	out.cdsect(mCdsectContent);
    }
    
    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
    	return (mPushedStartTag && mPushedEndTag);
    }

    /**
	 * @see ar.android.horkizein.XmlPushable#pushableTags()
	 */
	public Collection<String> pushableTags() {
		ArrayList<String> tags = new ArrayList<String>(1);
    	tags.add(TAG);
        return tags;
	}

	/**
	 * @see ar.android.horkizein.Taggable#getTag()
	 */
	public String getTag() {
		return TAG;
	}
}


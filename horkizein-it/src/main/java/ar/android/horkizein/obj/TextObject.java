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
import ar.android.horkizein.XmlPushable;
import ar.android.horkizein.XmlWritable;
import ar.android.horkizein.test.Constants;

/**
 * Implementation of XmlPushable which contains just text.
 */
public class TextObject implements XmlPushable, XmlWritable {
    
	// This object tag
    public final static String TAG = "text_obj";
    // Tags
    public final static String TEXT_TAG = "text";
    // watch dog
    private boolean wdPushedStartTag;
    private boolean wdPushedEndTag;
    private boolean wdTextStartTag;
    private boolean wdTextEndTag;
    
    private boolean mIsText;
    private String mText;
    
    /**
     * Ctor.
     */
    public TextObject() { 
    	mText = new String("");
    	wdPushedEndTag = wdPushedStartTag = false;
    }
    /**
     * Ctor.
     * @param text Some text.
     */
    public TextObject(String text) {
    	mText = new String(text);
    	wdPushedEndTag = wdPushedStartTag = false;
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
    public void pushAttribute(String tag, String name, String value) {
    	/* empty */
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushText(java.lang.String, java.lang.String)
     */
    public void pushText(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);
        if (wdPushedStartTag) {
            if (tag.equals(TEXT_TAG) && mIsText) {
            	Log.d (Constants.PACKAGE_TAG_TEST, TAG + " pushed: " + text);
    	    	Log.d (Constants.PACKAGE_TAG_TEST, "---------------------");
                mText = new String(text);
            }
        }
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushEndTag(java.lang.String)
     */
    public void pushEndTag(String tag) {
    	Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
    	if (tag.equals(TEXT_TAG)) {
    		mIsText = false;
    	}
    	if (tag.equals(TEXT_TAG) && wdTextStartTag) {
    		wdTextEndTag = true;
    	}
    	if (tag.equals(TAG) && wdPushedStartTag)
        	wdPushedEndTag = true;
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushStartTag(java.lang.String)
     */
    public void pushStartTag(String tag) {
    	Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag(" + tag + ")");
        if (tag.equals(TAG))
        	wdPushedStartTag = true;
        
        if (tag.equals(TEXT_TAG)) {
        	wdTextStartTag = true;
        	mIsText = true;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;
        TextObject item = (TextObject)obj;
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " 1: " + mText + "- 2: " + item.mText);
        return (mText.equals(item.mText));
    }

    /**
     * @see ar.android.horkizein.xml.XmlWritable#writeXml(org.xmlpull.v1.XmlSerializer)
     */
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
        out.startTag("", TAG);
        out.startTag("", TEXT_TAG);
        out.text(mText);
        out.endTag("", TEXT_TAG);
        out.endTag("", TAG);
    }
    
    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
    	return (wdPushedStartTag ||
    			wdPushedEndTag ||
    			wdTextStartTag ||
    			wdTextEndTag);
    }
}


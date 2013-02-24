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

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Implementation of XmlPushable which contains just text.
 */
@XmlTag (
    value = "text_obj",
    additionalTags = "text"
)
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

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) { /* do nothing */ }

    @Override
    public void pushStartTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag(" + tag + ")");
        if (tag.equals(TAG)) {
            wdPushedStartTag = true;
        }

        if (wdPushedStartTag == true) {
            if (tag.equals(TEXT_TAG)) {
                wdTextStartTag = true;
            }
        }
    }

    @Override
    public void pushText(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);
        //if (tag.equals(TAG) {
        //    no text for text_obj
        //}
        if (wdPushedStartTag == true) {
            if (tag.equals(TEXT_TAG) && wdTextStartTag == true) {
                Log.d (Constants.PACKAGE_TAG_TEST, TAG + " pushed: " + text);
                
                mText = new String(text);
            }
        }
    }

    @Override
    public void pushEndTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
        if (wdPushedStartTag == true) {
            if (tag.equals(TEXT_TAG) && wdTextStartTag == true) {
                wdTextEndTag = true;
            }
        }
        if (tag.equals(TAG) && wdPushedStartTag == true) {
            wdPushedEndTag = true;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;
        TextObject o = (TextObject)obj;
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " 1: " + mText + "- 2: " + o.mText);
        return (mText == o.mText || (mText != null && mText.equals(o.mText)));
    }

    @Override
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
        return (wdPushedStartTag &&
                wdPushedEndTag &&
                wdTextStartTag &&
                wdTextEndTag);
    }
}


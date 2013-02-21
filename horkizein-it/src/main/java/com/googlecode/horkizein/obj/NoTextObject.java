package com.googlecode.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.test.Constants;

@XmlTag (
    value = NoTextObject.TAG,
    enclosedPushables = TextObject.class
)
public class NoTextObject implements XmlPushable, XmlWritable {

    public final static String TAG = "no_text_obj";
    private final static String TEST_TEXT = "s0me TeXt, 4 T3st1ng";
    
    private TextObject mTextObject;
    
    private String mNoText;
    private boolean wdPushedStartTag;
    private boolean wdPushedTextObjStartTag;
    private boolean wdPushedEndTag;
    private boolean wdPushedTextObjEndTag;
    
    public NoTextObject() { /* do nothing */ }
    
    public NoTextObject(TextObject textObj) {
        mNoText = TEST_TEXT;
        mTextObject = textObj;
    }
    
    @Override
    public void writeXml(XmlSerializer serializer) throws IOException, IllegalStateException, IllegalArgumentException {
        serializer.startTag("", TAG);
        mTextObject.writeXml(serializer);
        serializer.endTag("", TAG);
    }

    @Override
    public void pushStartTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag() - TAG: " + tag);
        if (tag.equals(TAG)) {
            wdPushedStartTag = true;
            mTextObject = new TextObject();
        }

        if (wdPushedStartTag == true && mTextObject != null && tag.equals(TextObject.TAG)) {
            mTextObject.pushStartTag(tag);
            wdPushedTextObjStartTag = true;
        }
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) {
        /* it doesn't matter */
    }

    @Override
    public void pushText(String tag, String text) {
        if (wdPushedStartTag) {
            if (tag.equals(TAG)) {
                mNoText = text;
            } else if (tag.equals(TextObject.TAG) && wdPushedTextObjStartTag == true) {
                mTextObject.pushText(tag, text);
            }
        }
    }

    @Override
    public void pushEndTag(String tag) {
        
        if (wdPushedStartTag == true && mTextObject != null && tag.equals(TextObject.TAG)) {
            mTextObject.pushEndTag(tag);
            wdPushedTextObjStartTag = false;
            wdPushedTextObjEndTag = true;
        }
        
        if (tag.equals(TAG)) {
            wdPushedStartTag = false;
            wdPushedEndTag = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if((o == null) || (o.getClass() != this.getClass())) return false;

        NoTextObject n = (NoTextObject)o;
        return (mNoText == n.mNoText || (mNoText != null && mNoText.equals(n.mNoText)) &&
                 mTextObject == n.mTextObject || (mTextObject != null && mTextObject.equals(n.mTextObject)));
    }
    
    public boolean tagCheck() {
        return (mNoText == TEST_TEXT &&
                !wdPushedStartTag &&
                wdPushedEndTag &&
                !wdPushedTextObjStartTag &&
                wdPushedTextObjEndTag &&
                mTextObject.tagCheck());
    }
}

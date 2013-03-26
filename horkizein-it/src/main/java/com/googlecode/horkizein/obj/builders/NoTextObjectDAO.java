package com.googlecode.horkizein.obj.builders;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.obj.NoTextObject;
import com.googlecode.horkizein.obj.TextObject;
import com.googlecode.horkizein.test.Constants;

@XmlTag (
    value = NoTextObjectDAO.TAG,
    enclosedPushables = TextObjectDAO.class
)
public class NoTextObjectDAO implements XmlPushable<NoTextObject>, XmlWritable<NoTextObject> {

    public final static String TAG = NoTextObject.TAG;
    
    // Dependency
    private final XmlSerializer mSerializer;
    
    private TextObjectDAO mTextObjectDAO;
    
    private String mSupposedlyNoText = "s0me D4o T3st1ng";
    
    private boolean wdPushedStartTag;
    private boolean wdPushedTextObjStartTag;
    private boolean wdPushedEndTag;
    private boolean wdPushedTextObjEndTag;
    
    public NoTextObjectDAO(XmlSerializer serializer) { 
        mSerializer = serializer;
        mTextObjectDAO = new TextObjectDAO(serializer);
    }
    
    @Override
    public void writeXml(NoTextObject object) throws IOException, IllegalStateException, IllegalArgumentException {
        mSerializer.startTag("", TAG);
        mTextObjectDAO.writeXml(object.getTextObject());
        mSerializer.endTag("", TAG);
    }

    @Override
    public void pushStartTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag() - TAG: " + tag);
        if (tag.equals(TextObjectDAO.TAG)) {
            wdPushedTextObjStartTag = true;
        }
        if (wdPushedTextObjStartTag == true) {
            mTextObjectDAO.pushStartTag(tag);
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
                mSupposedlyNoText = text;
            } else if (tag.equals(TextObject.TAG) && wdPushedTextObjStartTag == true) {
                mTextObjectDAO.pushText(tag, text);
            }
        }
    }

    @Override
    public void pushEndTag(String tag) {
        if (wdPushedStartTag == true) {
            mTextObjectDAO.pushEndTag(tag);
            if (tag.equals(TextObject.TAG)) {
                wdPushedTextObjStartTag = false;
                wdPushedTextObjEndTag = true;
            }
        }
        if (tag.equals(TAG)) {
            wdPushedStartTag = false;
            wdPushedEndTag = true;
        }
    }
    
    public boolean tagCheck() {
        return (!wdPushedStartTag &&
                wdPushedEndTag &&
                !wdPushedTextObjStartTag &&
                wdPushedTextObjEndTag &&
                mTextObjectDAO.tagCheck());
    }

    @Override
    public NoTextObject build() {
        return new NoTextObject(mSupposedlyNoText, mTextObjectDAO.build());
    }

    @Override
    public XmlPushable<NoTextObject> shallowClone() {
        return new NoTextObjectDAO(mSerializer);
    }
}

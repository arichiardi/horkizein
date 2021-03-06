package com.googlecode.horkizein.obj.daos;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.NoTextObject;
import com.googlecode.horkizein.obj.TextObject;

@XmlTag (
    value = NoTextObjectDAO.TAG,
    enclosedPushables = TextObjectDAO.class
)
public class NoTextObjectDAO implements XmlPushable<NoTextObject>, XmlWriter {

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
    public void startTag(String tag) {
        if (tag.equals(TAG)) {
            wdPushedStartTag = true;
        }
        if (wdPushedStartTag) {
            if (tag.equals(TextObjectDAO.TAG)) {
                wdPushedTextObjStartTag = true;
                mTextObjectDAO.startTag(tag);
            }
        }
    }

    @Override
    public void attribute(String tag, String prefix, String name, String value) {
        /* it doesn't matter */
    }

    @Override
    public void text(String tag, String text) {
        if (wdPushedStartTag) {
            if (tag.equals(TAG)) {
                mSupposedlyNoText = text;
            } else if (tag.equals(TextObject.TAG) && wdPushedTextObjStartTag == true) {
                mTextObjectDAO.text(tag, text);
            }
        }
    }

    @Override
    public void endTag(String tag) {
        if (wdPushedStartTag == true) {
            mTextObjectDAO.endTag(tag);
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

    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mSerializer);
    }
}

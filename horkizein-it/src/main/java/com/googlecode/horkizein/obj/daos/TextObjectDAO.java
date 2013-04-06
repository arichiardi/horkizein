package com.googlecode.horkizein.obj.daos;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.TextObject;

@XmlTag (TextObjectDAO.TAG)
public class TextObjectDAO implements XmlPushable<TextObject>, XmlWriter {

    public static final String TAG = TextObject.TAG;
    
    // Dependency
    private final XmlSerializer mSerializer;
    
    private String mText;
    private boolean wdPushedStartTag;
    private boolean wdPushedEndTag;
    
    public TextObjectDAO(XmlSerializer serializer) {
        mSerializer = serializer;
    }
    @Override
    public TextObject build() {
        return new TextObject(mText);
    }

    @Override
    public XmlPushable<TextObject> shallowClone() {
        return new TextObjectDAO(mSerializer);
    }

    @Override
    public void pushStartTag(String tag) {
        if (tag.equals(TextObjectDAO.TAG)) {
            wdPushedStartTag = true;
        }
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) {
        /* does nothing */
    }

    @Override
    public void pushText(String tag, String text) {
        if (wdPushedStartTag) {
            mText = text;
        }
    }

    @Override
    public void pushEndTag(String tag) {
        if (tag.equals(TAG)) {
            wdPushedStartTag = false;
            wdPushedEndTag = true;
        }
    }
    
    public boolean tagCheck() {
        return !wdPushedStartTag && wdPushedEndTag;
    }
    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mSerializer);
    }
}

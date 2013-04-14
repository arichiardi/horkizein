package com.googlecode.horkizein.obj.daos;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.TextObject;

/**
 * Class that contains and declares a class which is an XmlPushable without annotation.
 */
public class NoTagDAO implements XmlPushable<TextObject>, XmlWriter {
 
    // Dependency
    private final XmlSerializer mSerializer;
    
    public NoTagDAO(XmlSerializer serializer) {
        mSerializer = serializer;
    }
    
    @Override
    public void startTag(String tag) {
    }

    @Override
    public void attribute(String tag, String prefix, String name, String value) {
    }

    @Override
    public void text(String tag, String text) {
    }

    @Override
    public void endTag(String tag) {
    }


    @Override
    public TextObject build() {
        return null;
    }

    @Override
    public XmlPushable<TextObject> shallowClone() {
        return null;
    }

    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mSerializer);
    }
}

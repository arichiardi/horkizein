package com.googlecode.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlWritable;

/**
 * Class that doesn't have {ode XmlTag} annotation.
 */
public class UntaggedObject implements XmlPushable, XmlWritable {

    private static final String TAG = "untagged";

    @Override
    public void pushStartTag(String tag) {
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) {
    }

    @Override
    public void pushText(String tag, String text) {
    }

    @Override
    public void pushEndTag(String tag) {
    }
    
    @Override
    public void writeXml(XmlSerializer serializer) throws IOException, IllegalStateException, IllegalArgumentException {
     // Writing out
        serializer.startTag("", TAG);
        serializer.endTag("", TAG);
    }
}

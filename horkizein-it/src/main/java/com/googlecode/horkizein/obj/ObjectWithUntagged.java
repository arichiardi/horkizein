package com.googlecode.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
/**
 * Class that contains and declares a class which is an XmlPushable without annotation.
 */
@XmlTag(
        value = "class_with_untagged",
        enclosedPushables = UntaggedObject.class
        )
public class ObjectWithUntagged implements XmlPushable, XmlWritable {
    
    private static final String TAG = "class_with_untagged";
    private UntaggedObject mUntagged;
    
    public ObjectWithUntagged() {
        mUntagged = new UntaggedObject();
    }

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
        if(mUntagged != null) {
            mUntagged.writeXml(serializer);
        }
        serializer.endTag("", TAG);
        
    }
}

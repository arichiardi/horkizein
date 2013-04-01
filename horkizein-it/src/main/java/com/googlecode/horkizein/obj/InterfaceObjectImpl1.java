package com.googlecode.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

public class InterfaceObjectImpl1 implements InterfaceObject {

    public static final String TAG = "interface_impl1";

    public final String mText;
    
    public InterfaceObjectImpl1(String text) {
        mText = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;
        InterfaceObjectImpl1 o = (InterfaceObjectImpl1)obj;
        return (mText == o.mText || (mText != null && mText.equals(o.mText)));
    }
    
    @Override
    public void writeXml(XmlSerializer serializer) throws IOException, IllegalStateException, IllegalArgumentException {
        serializer.startTag("", TAG);
        serializer.text(mText);
        serializer.endTag("", TAG);
    }
}

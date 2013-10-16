package com.googlecode.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlWritable;

public class NoTextObject implements XmlWritable {

    public final static String TAG = "no_text_obj";
    
    private final String mMyText; // it is always empty
    private final TextObject mTextObject;
    
    public NoTextObject(String myText, TextObject textObject) { 
        mMyText = myText;
        mTextObject = textObject;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if((o == null) || (o.getClass() != this.getClass())) return false;

        NoTextObject n = (NoTextObject)o;
        return (mMyText == n.mMyText || (mMyText != null && mMyText.equals(n.mMyText)) &&
                 mTextObject == n.mTextObject || (mTextObject != null && mTextObject.equals(n.mTextObject)));
    }
    
    /**
     * Note: This method safely return a reference just because the 
     * returned object is immutable.
     * @return A TextObject.
     */
    public TextObject getTextObject() {
        return mTextObject;
    }

    @Override
    public void writeXml(XmlSerializer serializer) throws IOException, IllegalStateException, IllegalArgumentException {
        serializer.startTag("", TAG);
        mTextObject.writeXml(serializer);
        serializer.endTag("", TAG);
    }
}

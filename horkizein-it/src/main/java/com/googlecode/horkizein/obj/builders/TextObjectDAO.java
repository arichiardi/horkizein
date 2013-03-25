package com.googlecode.horkizein.obj.builders;

import java.io.IOException;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.obj.TextObject;

public class TextObjectDAO implements XmlPushable<TextObject>, XmlWritable<TextObject> {

    public static final Object TAG = TextObject.TAG;

    @Override
    public TextObject build() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public XmlPushable<TextObject> shallowClone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeXml(TextObject object) throws IOException, IllegalStateException, IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushStartTag(String tag) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushText(String tag, String text) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pushEndTag(String tag) {
        // TODO Auto-generated method stub
        
    }
    
    public boolean tagCheck() {
        return false;
    }
}

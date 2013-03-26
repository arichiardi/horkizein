package com.googlecode.horkizein.obj.builders;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.obj.AbstractObject;
import com.googlecode.horkizein.obj.AbstractObjectImpl1;
import com.googlecode.horkizein.test.Constants;

/**
 * Abstract class implementation. It still needs to declare, through XmlTag, which fields wants
 * to receive.
 */
@XmlTag(AbstractObjectImpl1.TAG)
public class AbstractObjectImpl1DAO extends AbstractObjectDAO {
    
    private static final String TAG = AbstractObjectImpl1.TAG;
    
    // Dependency
    private final XmlSerializer mSerializer;
    
    private int mInt;

    /**
     * ctor
     * @param serializer The serializer.
     */
    public AbstractObjectImpl1DAO(XmlSerializer serializer) {
        mSerializer = serializer;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new AbstractObjectImpl1DAO(mSerializer);
    }
    
    @Override
    public AbstractObjectImpl1 build() {
        return new AbstractObjectImpl1(mInt);
    }

    @Override
    public void pushStartTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag(" + tag + ")");
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushAttribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);
    }

    @Override
    public void pushText(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " pushText(" + text + ")");
        mInt = Integer.parseInt(text);
    }

    @Override
    public void pushEndTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
    }

    @Override
    public XmlPushable<AbstractObject> shallowClone() {
        return new AbstractObjectImpl1DAO(mSerializer);
    }
    
    @Override
    public void writeXml(AbstractObjectImpl1 object) throws IOException, IllegalStateException, IllegalArgumentException {
        mSerializer.startTag("", TAG);
        mSerializer.text(String.valueOf(object.getInt()));
        mSerializer.endTag("", TAG);
    }
}

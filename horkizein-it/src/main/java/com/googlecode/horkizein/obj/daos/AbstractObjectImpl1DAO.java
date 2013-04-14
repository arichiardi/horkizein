package com.googlecode.horkizein.obj.daos;

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
    public static final String INT_ATTRIBUTE = "integer";
    // Dependency
    private final XmlSerializer mSerializer;
    
    private int mInt;
    private String mString;
    
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
        return new AbstractObjectImpl1(mInt, mString);
    }

    @Override
    public void startTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".startTag(" + tag + ")");
    }

    @Override
    public void attribute(String tag, String prefix, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".attribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);
        if (tag.equals(TAG) && name.equals(INT_ATTRIBUTE)) {
            mInt = Integer.parseInt(value);
        }
    }

    @Override
    public void text(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " pushText(" + text + ")");
        if (tag.equals(TAG)) {
            mString = text;
        }
    }

    @Override
    public void endTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
    }

    @Override
    public XmlPushable<AbstractObject> shallowClone() {
        return new AbstractObjectImpl1DAO(mSerializer);
    }

    @Override
    public void write(XmlWritable object) throws IllegalStateException, IllegalArgumentException, IOException {
        object.writeXml(mSerializer);
    }
}

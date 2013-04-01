package com.googlecode.horkizein.obj.builders;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;

import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.InterfaceObject;
import com.googlecode.horkizein.obj.InterfaceObjectImpl1;
import com.googlecode.horkizein.test.Constants;

@XmlTag(InterfaceObjectImpl1DAO.TAG)
public class InterfaceObjectImpl1DAO implements InterfaceObjectDAO, XmlWriter {

    public static final String TAG = "interface_impl1";
    
    private String mText;

    // Dependency
    private final XmlSerializer mSerializer;
    
    /**
     * Creates an HelloWorldObject.
     * @param serializer The serializer.
     */
    public InterfaceObjectImpl1DAO(XmlSerializer serializer) {
        mSerializer = serializer;
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
        if (tag.equals(TAG)) {
            mText = text;
        }
    }

    @Override
    public void pushEndTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
    }

    @Override
    public InterfaceObject build() {
        return new InterfaceObjectImpl1(mText);
    }

    @Override
    public InterfaceObjectDAO shallowClone() {
        return new InterfaceObjectImpl1DAO(mSerializer);
    }
    

    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mSerializer);
    }
}

package com.googlecode.horkizein.obj.daos;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.AbstractObject;
import com.googlecode.horkizein.obj.DummyObject;
import com.googlecode.horkizein.obj.InterfaceObject;
import com.googlecode.horkizein.test.Constants;

@XmlTag(
    value = DummyDAO.TAG,
    enclosedPushables = { AbstractObjectDAO.class, InterfaceObjectDAO.class }
)
public class DummyDAO implements XmlPushable<DummyObject>, XmlWriter {

    public static final String TAG = DummyObject.TAG;
    
    // Dependency
    private final XmlSerializer mSerializer;

    private final XmlPushable<? extends AbstractObject> mAbstractDAO;
    private final XmlPushable<? extends InterfaceObject> mInterfaceDAO;
    
    public DummyDAO(XmlSerializer serializer) {
        mSerializer = serializer;
        mAbstractDAO = new AbstractObjectImpl1DAO(serializer);
        mInterfaceDAO = new InterfaceObjectImpl1DAO(serializer);
    }
    
    @Override
    public void startTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".startTag(" + tag + ")");
        mAbstractDAO.startTag(tag);
        mInterfaceDAO.startTag(tag);
    }

    @Override
    public void attribute(String tag, String prefix, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".attribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);
        mAbstractDAO.attribute(tag, prefix, name, value);
        mInterfaceDAO.attribute(tag, prefix, name, value);
    }

    @Override
    public void text(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " pushText(" + text + ")");
        mAbstractDAO.text(tag, text);
        mInterfaceDAO.text(tag, text);
    }

    @Override
    public void endTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
        mAbstractDAO.startTag(tag);
        mInterfaceDAO.startTag(tag);
    }

    @Override
    public DummyObject build() {
        return new DummyObject(mAbstractDAO.build(), mInterfaceDAO.build());
    }

    @Override
    public XmlPushable<DummyObject> shallowClone() {
        return new DummyDAO(mSerializer);
    }

    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mSerializer);
    }
}

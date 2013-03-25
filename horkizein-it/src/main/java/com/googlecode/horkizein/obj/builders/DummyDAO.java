package com.googlecode.horkizein.obj.builders;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.obj.DummyObject;
import com.googlecode.horkizein.test.Constants;

@XmlTag(
    value = DummyDAO.TAG,
    enclosedPushables = { AbstractObjectDAO.class, InterfaceObjectDAO.class }
)
public class DummyDAO implements XmlPushable<DummyObject>, XmlWritable<DummyObject> {

    public static final String TAG = DummyObject.TAG;
    
    // Dependency
    private final XmlSerializer mSerializer;

    private final AbstractObjectDAO mAbstract;
    private final InterfaceObjectDAO mInterface;
    
    public DummyDAO(XmlSerializer serializer) {
        mSerializer = serializer;
        mAbstract = new AbstractObjectImpl1DAO(serializer);
        mInterface = new InterfaceObjectImpl1DAO(serializer);
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
    }

    @Override
    public void pushEndTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
    }

    

    @Override
    public DummyObject build() {
        return new DummyObject(mAbstract.build(), mInterface.build());
    }

    @Override
    public XmlPushable<DummyObject> shallowClone() {
        return new DummyDAO(mSerializer);
    }

    @Override
    public void writeXml(DummyObject object) throws IOException, IllegalStateException, IllegalArgumentException {
        //TODO
        mSerializer.startTag("", TAG);
    }
}

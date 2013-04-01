package com.googlecode.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlWritable;

/**
 * Test children with hierarchies.
 */
public class DummyObject implements XmlWritable {
    
    public static final String TAG = "dummy";
    private final AbstractObject mAbstract;
    private final InterfaceObject mInterface;
    
    public DummyObject(AbstractObject abs, InterfaceObject inter) {
        mAbstract = abs;
        mInterface = inter;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;
        DummyObject o = (DummyObject)obj;
        return ((mAbstract == o.mAbstract || (mAbstract != null && mAbstract.equals(o.mAbstract)))
            && (mInterface == o.mInterface || (mInterface != null && mInterface.equals(o.mInterface))));
    }
    /**
     * Note: This method should return a copy because we cannot rely
     * on the fact that the implementation is immutable. But we are
     * testing different things here, therefore a reference is returned.
     * @return An instance of AbstractObject
     */
    public AbstractObject getAbstract() {
        return mAbstract;
    }
    /**
     * Note: This method should return a copy because we cannot rely
     * on the fact that the implementation is immutable. But we are
     * testing different things here, therefore a reference is returned.
     * @return An instance of InterfaceObject
     */
    public InterfaceObject getInterface() {
        return mInterface;
    }

    @Override
    public void writeXml(XmlSerializer serializer) throws IOException, IllegalStateException, IllegalArgumentException {
        serializer.startTag("", TAG);
        mAbstract.writeXml(serializer);
        mInterface.writeXml(serializer);
        serializer.endTag("", TAG);
    }
}

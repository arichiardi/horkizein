package com.googlecode.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.obj.daos.AbstractObjectImpl1DAO;

/**
 * An implementation of AbstractObject.
 */
public class AbstractObjectImpl1 extends AbstractObject {
    
    public static final String TAG = "abstract_impl1";

    private final String mString;
    
    public AbstractObjectImpl1(int intero, String stringa) {
        super(intero);
        mString = stringa;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;
        AbstractObjectImpl1 o = (AbstractObjectImpl1)obj;
        return ((mString == o.mString || (mString != null && mString.equals(o.mString)))
                    && mSuperInt == o.mSuperInt);
    }
    
    @Override
    public void writeXml(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag("", TAG);
        serializer.attribute("", AbstractObjectImpl1DAO.INT_ATTRIBUTE, String.valueOf(mSuperInt));
        serializer.text(mString);
        serializer.endTag("", TAG);
    }
}

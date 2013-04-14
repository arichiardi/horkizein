package com.googlecode.horkizein.obj;

import com.googlecode.horkizein.XmlWritable;

/**
 * An abstract class. Just a placeholder.
 */
public abstract class AbstractObject implements XmlWritable {
    
    public static final String TAG = "abstract";
    
    protected final int mSuperInt;
    
    public AbstractObject(int superIntero) {
        mSuperInt = superIntero;
    }
}

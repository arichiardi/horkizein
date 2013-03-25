package com.googlecode.horkizein.obj;

/**
 * An implementation of AbstractObject.
 */
public class AbstractObjectImpl1 extends AbstractObject {
    
    public static final String TAG = "abstract_impl1";

    private final int mInt;
    
    public AbstractObjectImpl1(int intero) {
        mInt = intero;
    }
    
    public final int getInt() {
        return mInt;
    }
}

package com.googlecode.horkizein.obj;

public class InterfaceObjectImpl1 implements InterfaceObject {

    public static final String TAG = "interface_impl1";

    public final String mText;
    
    public InterfaceObjectImpl1(String text) {
        mText = text;
    }
    
    public String getText() {
        return mText;
    }
}

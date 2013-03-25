package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.obj.DummyObject;

/**
 * Class that contains and declares a class which is an XmlPushable without annotation.
 */
public class UntaggedDAO implements XmlPushable<DummyObject> {
    
    @Override
    public void pushStartTag(String tag) {
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) {
    }

    @Override
    public void pushText(String tag, String text) {
    }

    @Override
    public void pushEndTag(String tag) {
    }


    @Override
    public DummyObject build() {
        return null;
    }

    @Override
    public XmlPushable<DummyObject> shallowClone() {
        return null;
    }
}

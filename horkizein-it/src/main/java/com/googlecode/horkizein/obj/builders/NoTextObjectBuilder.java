package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.NoTextObject;

public class NoTextObjectBuilder implements XmlBuilder<NoTextObject> {

    @Override
    public NoTextObject getInstance() {
        return new NoTextObject();
    }

}

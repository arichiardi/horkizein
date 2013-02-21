package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.HelloWorldObject;

public class HelloWorldObjectBuilder implements XmlBuilder<HelloWorldObject> {

    @Override
    public HelloWorldObject getInstance() {
        return new HelloWorldObject();
    }
}

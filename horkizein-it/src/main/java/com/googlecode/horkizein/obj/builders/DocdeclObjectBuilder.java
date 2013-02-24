package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.DocdeclObject;

public class DocdeclObjectBuilder implements XmlBuilder<DocdeclObject> {

    @Override
    public DocdeclObject getInstance() {
        return new DocdeclObject();
    }
}

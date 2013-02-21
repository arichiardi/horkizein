package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.NestedObject1;

public class NestedObject1Builder implements XmlBuilder<NestedObject1> {

    @Override
    public NestedObject1 getInstance() {
        return new NestedObject1();
    }

}

package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.ObjectWithUntagged;

public class ObjectWithUntaggedBuilder implements XmlBuilder<ObjectWithUntagged> {

    @Override
    public ObjectWithUntagged getInstance() {
        return new ObjectWithUntagged();
    }
}

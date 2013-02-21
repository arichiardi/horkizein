package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.ObjectWithList;

public class ObjectWithListBuilder implements XmlBuilder<ObjectWithList> {

    @Override
    public ObjectWithList getInstance() {
        return new ObjectWithList();
    }

}

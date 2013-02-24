package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.CdsectObject;

public class CdsectObjectBuilder implements XmlBuilder<CdsectObject> {

    @Override
    public CdsectObject getInstance() {
        return new CdsectObject();
    }
}


package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.ProcessingObject;

public class ProcessingObjectBuilder implements XmlBuilder<ProcessingObject> {

    @Override
    public ProcessingObject getInstance() {
        return new ProcessingObject();
    }
}

package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.TextObject;

public class TextObjectBuilder implements XmlBuilder<TextObject> {

    @Override
    public TextObject getInstance() {
        return new TextObject();
    }
    

}

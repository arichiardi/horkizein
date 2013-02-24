package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.CommentObject;

public class CommentObjectBuilder implements XmlBuilder<CommentObject> {

    @Override
    public CommentObject getInstance() {
        return new CommentObject();
    }
}

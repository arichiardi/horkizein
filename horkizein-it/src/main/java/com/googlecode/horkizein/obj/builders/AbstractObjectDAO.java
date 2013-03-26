package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.obj.AbstractObject;
import com.googlecode.horkizein.obj.AbstractObjectImpl1;

/**
 * Abstract class. This is a placeholder to specify XmlTag for AbstractObjectDAO's children.
 */
@XmlTag(
    value = "",
    additionalTags = AbstractObjectImpl1.TAG
)
public abstract class AbstractObjectDAO implements XmlPushable<AbstractObject>, XmlWritable<AbstractObjectImpl1> {
}

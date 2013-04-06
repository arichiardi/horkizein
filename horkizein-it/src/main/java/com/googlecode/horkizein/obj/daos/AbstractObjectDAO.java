package com.googlecode.horkizein.obj.daos;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.AbstractObject;
import com.googlecode.horkizein.obj.AbstractObjectImpl1;

/**
 * Abstract class. This is a placeholder to specify XmlTag for AbstractObjectDAO's children.
 */
@XmlTag(
    value = "",
    additionalTags = AbstractObjectImpl1.TAG
)
public abstract class AbstractObjectDAO implements XmlPushable<AbstractObject>, XmlWriter {
}

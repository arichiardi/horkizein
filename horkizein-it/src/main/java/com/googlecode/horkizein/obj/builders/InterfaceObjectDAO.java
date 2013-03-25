package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.obj.InterfaceObject;

/**
 * Interface. The type parameter is not specified because it's taken from the sub-classes.
 */
@XmlTag(
    value = "",
    enclosedPushables = InterfaceObjectImpl1DAO.class
)
public interface InterfaceObjectDAO extends XmlPushable<InterfaceObject> {
}

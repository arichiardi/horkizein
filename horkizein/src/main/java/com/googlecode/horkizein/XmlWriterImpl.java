package com.googlecode.horkizein;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

/**
 * Common code for potential DAO classes. The code is so simple that it shouldn't really be used unless
 * (prefer Interfaces over Abstract classes). It is provided in case it is needed for composition (prefer
 * Composition over Inheritance).
 */
public class XmlWriterImpl implements XmlWriter {
    
    private final XmlSerializer mXmlSerializer;
    
    public XmlWriterImpl(XmlSerializer serializer) {
        mXmlSerializer = serializer;
    }
    
    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mXmlSerializer);
    }
}

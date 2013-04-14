package com.googlecode.horkizein;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

/**
 * Common code for DAO classes. The code is so simple that it shouldn't really be used
 * (always prefer Interfaces over Abstract classes).
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

package com.googlecode.horkizein;

import java.io.IOException;

/**
 * Xml serializer.
 */
public interface XmlWriter {

    /**
     * Write the XmlWritable instance on file. Typically, the class implementing
     * this interface will also include XmlSerializer as dependency in order to pass it to the
     * XmlWritable object.
     * @param object The object to write.
     * @throws IOException Thrown by XmlSerializer.
     * @throws IllegalStateException Thrown by XmlSerializer.
     * @throws IllegalArgumentException Thrown by XmlSerializer.
     */
    void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException;
}

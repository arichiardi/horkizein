package com.googlecode.horkizein;

import java.io.IOException;

/**
 * Gives the object the ability to export all its properties (copies of the propertie if it is immutable) in one shot.
 */
public interface XmlWriter {

    /**
     * The Data Access Object method to write instances on file. Typically, the class implementing
     * this interface will also include XmlSerializer as dependency in order to pass it to the
     * XmlWritable object.
     * @param object The object to write.
     * @throws IOException Thrown by XmlSerializer.
     * @throws IllegalStateException Thrown by XmlSerializer.
     * @throws IllegalArgumentException Thrown by XmlSerializer.
     */
    void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException;
}

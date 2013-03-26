/*
 ** Copyright 2013, Horkizein Open Source Android Library
 **
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at
 **
 **     http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ** See the License for the specific language governing permissions and
 ** limitations under the License.
 */
package com.googlecode.horkizein;

import java.io.IOException;

/**
 * XmlWritable object contract.
 * @param <T> The object type to persist.
 */
public interface XmlWritable<T> {

    /**
     * Implement this method to write your object to Xml. Typically, the class implementing
     * this interface will also include XmlSerializer as dependency in order to write the Xml file.
     * @param object The object to persist.
     * @throws IOException Launched by XmlSerializer.
     * @throws IllegalStateException Launched by XmlSerializer.
     * @throws IllegalArgumentException Launched by XmlSerializer.
     */
    public void writeXml(T object) throws IOException, IllegalStateException, IllegalArgumentException; 
}

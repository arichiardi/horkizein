/*
 ** Copyright 2011, Horkizein Open Source Android Library
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
package ar.android.horkizein;

import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

/**
 * XmlWritable contract.
 */
public interface XmlWritable {

    /**
     * Method to implement for xml writing.
     * @param serializer The XmlSerializer.
     * @throws IOException Launched by XmlSerializer.
     * @throws IllegalStateException Launched by XmlSerializer.
     * @throws IllegalArgumentException Launched by XmlSerializer.
     */
    public void writeXml(XmlSerializer serializer) throws IOException, IllegalStateException, IllegalArgumentException; 
}

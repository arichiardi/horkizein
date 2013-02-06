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
package com.googlecode.horkizein.test.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlWritable;

import android.content.Context;

/**
 * Very simple class to simplify the Xml file building from XmlWritable objects. 
 */
public final class XmlDataCommitter {

    private XmlDataCommitter() {}

    /**
     * A useful Android specific method that writes XmlWritables on file.
     * @param context An Android Context.
     * @param filename The input filename.
     * @param object The list to write into the file.
     * @return True if successful, false if not.
     */
    public static void commitData(Context context, String filename, String encoding, XmlWritable object) throws IllegalArgumentException, IllegalStateException, IOException {

        BufferedOutputStream buf = new BufferedOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));

        XmlSerializer serializer = android.util.Xml.newSerializer();
        serializer.setOutput(buf, encoding);
        // do it
        object.writeXml(serializer);

        serializer.endDocument();
        serializer.flush();
        buf.close();
    }

    public static void commitData(Context context, String filename, String encoding, Collection<XmlWritable> objects) throws IllegalArgumentException, IllegalStateException, IOException {
        BufferedOutputStream buf = new BufferedOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));

        XmlSerializer serializer = android.util.Xml.newSerializer();
        serializer.setOutput(buf, encoding);
        // do it
        for (XmlWritable object : objects) {
            object.writeXml(serializer);
        }

        serializer.endDocument();
        serializer.flush();
        buf.close();
    }
}

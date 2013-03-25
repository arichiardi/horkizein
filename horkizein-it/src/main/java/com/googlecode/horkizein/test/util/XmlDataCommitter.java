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

    private final Context mContext;
    private final String mFileName;
    private final String mEncoding;
    private final XmlSerializer mSerializer;
    
    public XmlDataCommitter(Context context, XmlSerializer serializer, String filename, String encoding) {
        mContext = context;
        mSerializer = serializer;
        mFileName = filename;
        mEncoding = encoding;
    }
    /**
     * A useful Android specific method that writes one XmlWritable on file.
     * @param dao A data access object (implements XmlWritable interface).
     * @param object An object to write onto the file.
     * @throws IllegalArgumentException 
     * @throws IllegalStateException 
     * @throws IOException 
     */
    public <T> void commitData(XmlWritable<T> dao, T object) throws IllegalArgumentException, IllegalStateException, IOException {
        BufferedOutputStream buf = new BufferedOutputStream(mContext.openFileOutput(mFileName, Context.MODE_PRIVATE));
        mSerializer.setOutput(buf, mEncoding);
        mSerializer.startDocument(mEncoding, true);
        // do it
        dao.writeXml(object);
        mSerializer.endDocument();
        mSerializer.flush();
        buf.close();
    }

    /**
     * A useful Android specific method that writes XmlWritables on file.
     * @param dao A data access object (XmlWritable interface)
     * @param objects A collection to write onto a file.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     */
    public <T> void commitData(XmlWritable<T> dao, Collection<T> objects) throws IllegalArgumentException, IllegalStateException, IOException {
        BufferedOutputStream buf = new BufferedOutputStream(mContext.openFileOutput(mFileName, Context.MODE_PRIVATE));
        mSerializer.setOutput(buf, mEncoding);
        mSerializer.startDocument(mEncoding, true);
        // do it
        for (T object : objects) {
            dao.writeXml(object);
        }
        mSerializer.endDocument();
        mSerializer.flush();
        buf.close();
    }
}

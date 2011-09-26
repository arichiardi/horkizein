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
package ar.android.horkizein.test.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import ar.android.horkizein.XmlWritable;

/**
 * Very simple class to simplify the Xml file building from XmlWritable objects. 
 */
public final class XmlDataCommitter {

	public XmlDataCommitter() {	}

	/**
	 * Internal writer, given an OutputStream and a XmlWritable object.
	 * @param output A generic OutputStream.
	 * @param object The XmlWritable object to write.
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void commitData(OutputStream output, XmlWritable object) throws IllegalArgumentException, IllegalStateException, IOException {

	    XmlSerializer serializer = android.util.Xml.newSerializer();

	    serializer.setOutput(output, "UTF-8");
	    serializer.startDocument("UTF-8", true);
	    // write the file
	    object.writeXml(serializer);

	    serializer.endDocument();
	    serializer.flush();
	}

	/**
	 * A useful Android specific method that writes XmlWritables on file.
	 * @param context An Android Context.
	 * @param filename The input filename.
	 * @param object The list to write into the file.
	 * @return True if successful, false if not.
	 */
	public void commitData(Context context, String filename, XmlWritable object) throws IllegalArgumentException, IllegalStateException, IOException {

	    BufferedOutputStream buf = new BufferedOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
	    // do it
	    commitData(buf, object);
	    buf.close();
	}
}

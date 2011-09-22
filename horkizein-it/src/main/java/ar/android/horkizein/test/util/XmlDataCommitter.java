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

public final class XmlDataCommitter {

	public XmlDataCommitter() {	}

	private void commitData(OutputStream output, XmlWritable data) throws IllegalArgumentException, IllegalStateException, IOException {

	    XmlSerializer serializer = android.util.Xml.newSerializer();

	    serializer.setOutput(output, "UTF-8");
	    serializer.startDocument("UTF-8", true);
	    // write the file
	    data.writeXml(serializer);

	    serializer.endDocument();
	    serializer.flush();
	}

	/**
	 * A useful Android specific method base that use commitData.
	 * @param context An Android Context.
	 * @param inFilename The input filename.
	 * @param inObject The list to write into the file.
	 * @return True if successful, false if not.
	 */
	public void commitData(Context inContext, String inFilename, XmlWritable inObject) throws IllegalArgumentException, IllegalStateException, IOException {

	    BufferedOutputStream buf = new BufferedOutputStream(inContext.openFileOutput(inFilename, Context.MODE_PRIVATE));
	    // do it
	    commitData(buf, inObject);
	    buf.close();
	}
}

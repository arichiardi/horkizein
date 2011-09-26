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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import ar.android.horkizein.test.util.AndroidInternalFileInputStream;
import ar.android.horkizein.XmlFiller;
import ar.android.horkizein.XmlPushable;

/**
 * Very simple class to simplify the Xml to XmlPushable binding.  
 */
public class XmlDataReader {

	public XmlDataReader() {}

	/**
	 * Internal reader, given a XmlPushable object and a Reader.
	 * @param object The XmlPushable object to fill.
	 * @param stream A generic Reader.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void grabData(XmlPushable object, Reader stream) throws XmlPullParserException, IOException {

	    XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
	    xmlFactory.setNamespaceAware(true);
	    XmlPullParser xmlParser = xmlFactory.newPullParser();
	    XmlFiller filler = new XmlFiller(xmlParser, new HashMap<String, XmlPushable>() );

	    filler.registerNode(object);
	    xmlParser.setInput(stream);
	    // do it
	    filler.fill();
	}

	/**
	 * A useful Android specific method that fills XmlPushables.
	 * @param inContext A context.
	 * @param outObject The out object to fill.
	 * @param inFileName The input file name.
	 * @return True on success, false otherwise.
	 */
    public void grabData(Context inContext, XmlPushable outObject, String inFileName) throws FileNotFoundException, XmlPullParserException, IOException {

        // Opens the file, buffers it and starts!
        AndroidInternalFileInputStream inputStream = new AndroidInternalFileInputStream(inContext, inFileName);
        BufferedReader bufReader = new BufferedReader(inputStream);
        //do it
        grabData(outObject, bufReader);
        bufReader.close();
    }
}

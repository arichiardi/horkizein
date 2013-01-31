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
import java.util.Collection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import ar.android.horkizein.test.util.AndroidInternalFileInputStream;
import ar.android.horkizein.XmlFiller;
import ar.android.horkizein.XmlPushable;

/**
 * Very simple class to simplify the Xml to XmlPushable binding.  
 */
public class XmlDataReader {

	private XmlDataReader() {}

	/**
	 * A custom method that fills an XmlPushable.
	 * @param inParser The parser we want to use.
	 * @param inContext A context.
	 * @param outObject The XmlPushable object to fill.
	 * @param inFileName The input file name.
	 * @throws FileNotFoundException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static void grabDataOutmost(XmlPullParser inParser, Context inContext, XmlPushable outObject, String inFileName) throws FileNotFoundException, XmlPullParserException, IOException {
		grabDataOutmost(inParser, inContext, outObject, inFileName, false);
	}
	
	/**
	 * A custom method that fills a collection of XmlPushable.
	 * @param inParser The parser we want to use.
	 * @param inContext A context.
	 * @param outObjects Collection of XmlPushables objects to fill.
	 * @param inFileName The input file name.
	 * @throws FileNotFoundException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static void grabDataOutmost(XmlPullParser inParser, Context inContext, Collection<? extends XmlPushable> outObjects, String inFileName) throws FileNotFoundException, XmlPullParserException, IOException {
		grabDataOutmost(inParser, inContext, outObjects, inFileName, false);
	}
		
	/**
	 * A custom method that fills an XmlPushable..
	 * @param inParser The parser we want to use.
	 * @param inContext A context.
	 * @param outObject The XmlPushable object to fill.
	 * @param inFileName The input file name.
	 * @param includeMetadata True if you want to read XML Metadata as well.
	 * @throws FileNotFoundException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
    public static void grabDataOutmost(XmlPullParser inParser, Context inContext, XmlPushable outObject, String inFileName, boolean includeMetadata) throws FileNotFoundException, XmlPullParserException, IOException {
    	// Prints the file for debugging.
    	try {
        	AndroidInternalFileInputStream inputStream = new AndroidInternalFileInputStream(inContext, inFileName);
            BufferedReader bufReader = new BufferedReader(inputStream);
            
        	String inputLine;

        	while ((inputLine = bufReader.readLine()) != null) {
        		android.util.Log.i(ar.android.horkizein.test.Constants.PACKAGE_TAG_TEST, inputLine);
        	}
        	bufReader.close();
        } catch (IOException e) {
        	android.util.Log.i(ar.android.horkizein.test.Constants.PACKAGE_TAG_TEST, e.getMessage());
        }
    	
        // Opens the file, buffers it and starts!
        AndroidInternalFileInputStream inputStream = new AndroidInternalFileInputStream(inContext, inFileName);
        BufferedReader bufReader = new BufferedReader(inputStream);
	    XmlFiller filler = new XmlFiller(inParser);
	    // register
	    filler.registerNode(outObject);
	    inParser.setInput(bufReader);
	    // fill
	    filler.outmostFill();
        // close buffer
	    bufReader.close();
    }
    
    /**
	 * A custom method that fills a collection of XmlPushable.
     * @param inParser The parser we want to use.
     * @param inContext A context.
     * @param outObjects Collection of XmlPushables objects to fill.
     * @param inFileName The input file name.
     * @param includeMetadata True if you want to read XML Metadata as well.
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static void grabDataOutmost(XmlPullParser inParser, Context inContext, Collection<? extends XmlPushable> outObjects, String inFileName, boolean includeMetadata) throws FileNotFoundException, XmlPullParserException, IOException {
    	android.util.Log.i(ar.android.horkizein.test.Constants.PACKAGE_TAG_TEST, "List filling");
    	// Prints the file for debugging.
        try {
        	AndroidInternalFileInputStream inputStream = new AndroidInternalFileInputStream(inContext, inFileName);
            BufferedReader bufReader = new BufferedReader(inputStream);
            
        	String inputLine;

        	while ((inputLine = bufReader.readLine()) != null) {
        		android.util.Log.i(ar.android.horkizein.test.Constants.PACKAGE_TAG_TEST, inputLine);
        	}
        	bufReader.close();
        } catch (IOException e) {
        	android.util.Log.i(ar.android.horkizein.test.Constants.PACKAGE_TAG_TEST, e.getMessage());
        }
    	
        // Opens the file, buffers it and starts!
        AndroidInternalFileInputStream inputStream = new AndroidInternalFileInputStream(inContext, inFileName);
        BufferedReader bufReader = new BufferedReader(inputStream);
        
        XmlFiller filler = new XmlFiller(inParser);
	    // register
	    filler.registerNode(outObjects);
	    inParser.setInput(bufReader);
	    // fill
	    filler.outmostFill();
        // close buffer
	    bufReader.close();
    }
}

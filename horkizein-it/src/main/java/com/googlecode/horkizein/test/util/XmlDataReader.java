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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.XmlFiller;
import com.googlecode.horkizein.XmlPushable;

import android.content.Context;

/**
 * Very simple class to simplify the Xml to XmlPushable binding.  
 */
public class XmlDataReader {

    private XmlDataReader() {}

    /**
     * A custom method that fills an XmlPushable..
     * @param parser The parser we want to use.
     * @param context A context.
     * @param clazz The XmlPushable class.
     * @param builder The XmlPushable builder.
     * @param fileName The input file name.
     * @return An instance of the requested object.
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static <T extends XmlPushable> T read(XmlPullParser parser, Context context, Class<T> clazz, XmlBuilder<T> builder, String fileName) throws FileNotFoundException, XmlPullParserException, IOException {
        // Prints the file for debugging.
        /*try {
            InputStreamReader inputStream = new InputStreamReader(inContext.openFileInput(inFileName));
            BufferedReader bufReader = new BufferedReader(inputStream);

            String inputLine;

            while ((inputLine = bufReader.readLine()) != null) {
                android.util.Log.i(Constants.PACKAGE_TAG_TEST, inputLine);
            }
            bufReader.close();
        } catch (IOException e) {
            android.util.Log.i(Constants.PACKAGE_TAG_TEST, e.getMessage());
        }*/

        // Opens the file, buffers it and starts!
        InputStreamReader inputStream = new InputStreamReader(context.openFileInput(fileName));
        BufferedReader bufReader = new BufferedReader(inputStream);
        XmlFiller filler = new XmlFiller(parser);
        // register
        filler.registerNode(clazz, builder);
        parser.setInput(bufReader);
        // fill
        filler.parse();
        // close buffer
        bufReader.close();
        
        return filler.getFirstInstanceOf(clazz);
    }

    /**
     * A custom method that fills a collection of XmlPushable.
     * @param inParser The parser we want to use.
     * @param inContext A context.
     * @param clazzez List ofXmlPushables types.
     * @param builders List of XmlBuilder.
     * @param inFileName The input file name.
     * @param includeMetadata True if you want to read XML Metadata as well.
     * @return A list of XmlPushables.
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static List<XmlPushable> readMany(XmlPullParser inParser, Context inContext, List<Class<? extends XmlPushable>> clazzez, List<XmlBuilder<? extends XmlPushable>> builders, String inFileName) throws FileNotFoundException, XmlPullParserException, IOException {
        android.util.Log.i(com.googlecode.horkizein.test.Constants.PACKAGE_TAG_TEST, "List filling");
        // Prints the file for debugging.
        try {
            InputStreamReader inputStream = new InputStreamReader(inContext.openFileInput(inFileName));
            BufferedReader bufReader = new BufferedReader(inputStream);

            String inputLine;

            while ((inputLine = bufReader.readLine()) != null) {
                android.util.Log.i(com.googlecode.horkizein.test.Constants.PACKAGE_TAG_TEST, inputLine);
            }
            bufReader.close();
        } catch (IOException e) {
            android.util.Log.i(com.googlecode.horkizein.test.Constants.PACKAGE_TAG_TEST, e.getMessage());
        }
        
        // Opens the file, buffers it and starts!
        InputStreamReader inputStream = new InputStreamReader(inContext.openFileInput(inFileName));
        BufferedReader bufReader = new BufferedReader(inputStream);

        XmlFiller filler = new XmlFiller(inParser);
        // register
        for (int i = 0; i < clazzez.size(); i++) {
            filler.registerNode(clazzez.get(i), builders.get(i));
        }
        inParser.setInput(bufReader);
        // fill
        filler.parse();
        // close buffer
        bufReader.close();
        
        List<XmlPushable> result = new ArrayList<XmlPushable>();
        for (int i = 0; i < clazzez.size(); i++) {
            result.addAll(filler.getInstanceListOf(clazzez.get(i)));
        }
        return result;
    }

}

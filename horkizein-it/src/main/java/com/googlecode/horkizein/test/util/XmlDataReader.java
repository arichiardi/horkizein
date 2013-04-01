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
import java.util.Collection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.googlecode.horkizein.XmlFiller;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.test.Constants;

import android.content.Context;

/**
 * Very simple class to simplify the Xml to XmlPushable binding.  
 */
public class XmlDataReader {
    
    private final Context mContext;
    private final String mFileName;
    private final XmlFiller mFiller;
    
    public XmlDataReader(Context context, String filename, XmlFiller xmlFiller) {
        mContext = context;
        mFileName = filename;
        mFiller = xmlFiller;
    }

    /**
     * A custom method that fills an XmlPushable..
     * @param parser The parser we want to use.
     * @param daoInstance A data access instance (implements XmlPushable interface).
     * @param daoClass The class of the desired XmlPushable type (necessary because of Generics' type erasure).
     * @param context A context.
     * @return An instance of the requested object.
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    public <E extends XmlPushable<?>> E read(XmlPushable<?> daoInstance, Class<E> daoClass) throws FileNotFoundException, XmlPullParserException, IOException {
        // Prints the file for debugging.
        /*try {
            InputStreamReader inputStream = new InputStreamReader(mContext.openFileInput(fileName));
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
        InputStreamReader inputStream = new InputStreamReader(mContext.openFileInput(mFileName));
        BufferedReader bufReader = new BufferedReader(inputStream);
        // register
        mFiller.registerNode(daoInstance);
        mFiller.setInput(bufReader);
        // fill
        mFiller.parse();
        // close buffer
        bufReader.close();
        return mFiller.firstOf(daoClass);
    }

    /**
     * A custom method that fills a collection of XmlPushables.
     * @param pushables Collection of desired XmlPushable instances.
     * @return Kind of fluent interface here.
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    public XmlFiller readMany(Collection<XmlPushable<?>> pushables) throws FileNotFoundException, XmlPullParserException, IOException {
        // Prints the file for debugging.
        try {
            InputStreamReader inputStream = new InputStreamReader(mContext.openFileInput(mFileName));
            BufferedReader bufReader = new BufferedReader(inputStream);

            String inputLine;

            while ((inputLine = bufReader.readLine()) != null) {
                android.util.Log.i(Constants.PACKAGE_TAG_TEST, inputLine);
            }
            bufReader.close();
        } catch (IOException e) {
            android.util.Log.i(Constants.PACKAGE_TAG_TEST, e.getMessage());
        }
        // Opens the file, buffers it and starts!
        InputStreamReader inputStream = new InputStreamReader(mContext.openFileInput(mFileName));
        BufferedReader bufReader = new BufferedReader(inputStream);

        // register
        for (XmlPushable<?> pushable : pushables) {
            mFiller.registerNode(pushable);
        }
        mFiller.setInput(bufReader);
        // fill
        mFiller.parse();
        // close buffer
        bufReader.close();
        return mFiller;
    }
}

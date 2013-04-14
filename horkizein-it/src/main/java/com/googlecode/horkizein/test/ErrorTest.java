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
package com.googlecode.horkizein.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushParser;
import com.googlecode.horkizein.XmlPushParserImpl1;
import com.googlecode.horkizein.obj.TextObject;
import com.googlecode.horkizein.obj.daos.NoTagDAO;
import com.googlecode.horkizein.test.Constants;
import com.googlecode.horkizein.test.util.XmlDataCommitter;
import com.googlecode.horkizein.test.util.XmlDataReader;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

/**
 * This class tests the exact match of a written and then read XmlPushable.
 */
public class ErrorTest extends AndroidTestCase {

    private static final String TAG = "EqualityTest";

    private static final String TEMPORARY_FILE = "temporary.xml";

    private XmlPullParser mParser;
    private XmlPushParser mXmlPushParser;
    private XmlSerializer mSerializer;
    private XmlDataCommitter mDataCommitter;
    private XmlDataReader mDataReader;
    
    public ErrorTest() {}

    @Override
    protected void setUp() {
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() entering.");
        mSerializer = android.util.Xml.newSerializer();
        mDataCommitter = new XmlDataCommitter(getContext(), mSerializer, TEMPORARY_FILE, "UTF-8");
        
        try {
            XmlPullParserFactory f = XmlPullParserFactory.newInstance();
            mParser = f.newPullParser();
        } catch (XmlPullParserException e) {
            Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() exception " + e.getMessage());
            mParser = null;
        }
        assertNotNull(mParser);
        mXmlPushParser = new XmlPushParserImpl1(mParser);
        mDataReader = new XmlDataReader(getContext(), TEMPORARY_FILE, mXmlPushParser);
    }

    @MediumTest
    public void testDeclaredButNotAnnotatedError() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException, CloneNotSupportedException {

      Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testDeclaredButNotAnnotatedError] ---");
      boolean catched = false;
      try {
          NoTagDAO notagDAO = new NoTagDAO(mSerializer);
          TextObject dummy = new TextObject("untagged");
          mDataCommitter.commitData(notagDAO, dummy); // serializing
          mDataReader.read(notagDAO, NoTagDAO.class);
      } catch (RuntimeException expected) {
          Log.i(Constants.PACKAGE_TAG_TEST, "Expected: " + expected.getMessage());
          catched = true;
      }
      assert(catched);
    }
    
    @Override
    protected void tearDown() throws Exception {
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".tearDown() delete file: " + TEMPORARY_FILE);
        mContext.deleteFile(TEMPORARY_FILE);
        super.tearDown();
    }
}


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

import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.obj.FlatObject;
import com.googlecode.horkizein.obj.FlatObjectList;
import com.googlecode.horkizein.obj.NestedObject1;
import com.googlecode.horkizein.obj.ObjectWithList;
import com.googlecode.horkizein.obj.ObjectWithUntagged;
import com.googlecode.horkizein.obj.builders.FlatObjectListBuilder;
import com.googlecode.horkizein.obj.builders.ObjectWithUntaggedBuilder;
import com.googlecode.horkizein.test.Constants;
import com.googlecode.horkizein.test.util.XmlDataCommitter;
import com.googlecode.horkizein.test.util.XmlDataGrabber;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

/**
 * This class tests the exact match of a written and then read XmlPushable.
 */
public class ErrorTest extends AndroidTestCase {

    private static final String TAG = "EqualityTest";

    private static final String TEMPORARY_FILE = "temporary.xml";
    private static final String METADATA_FILE = "metadata.xml";

    private FlatObject mFlatSrc;

    private XmlPullParser mParser = null;

    public ErrorTest() {}

    @Override
    protected void setUp() {
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() entering.");

        try {
            XmlPullParserFactory f = XmlPullParserFactory.newInstance();
            mParser = f.newPullParser();
        } catch (XmlPullParserException e) {
            Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() exception " + e.getMessage());
            mParser = null;
        }
        assertNotNull(mParser);
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() parser: " + mParser.getClass().getName());

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() creating FlatObject src");
        mFlatSrc = new FlatObject();
        mFlatSrc.mBooleanAttr = false;
        mFlatSrc.mIntegerAttr = 666;
        mFlatSrc.mDoubleAttr = 0.666;
        mFlatSrc.mStringAttr = new String("666");

        mFlatSrc.mBooleanTag = true;
        mFlatSrc.mIntegerTag = 42;
        mFlatSrc.mDoubleTag = 0.42;
        mFlatSrc.mStringTag = new String("42");
    }

    /**
     * Tests if XmlFiller leaves as it is a registered object that has the same tag as one of
     * the children of an previously registered other object. Just the latter has to be filled.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    /*@MediumTest
    public void testNestingError() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testNestingError] ---");

        NestedObject1 mNested1Src = new NestedObject1(mFlatSrc);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testNestingError() open Android file: " + TEMPORARY_FILE);
        XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", mNested1Src); // marshalling

        FlatObject mFlatErr = new FlatObject(); // already embedded in the Nested Object
        mFlatErr.mBooleanTag = true;
        mFlatErr.mIntegerAttr = 5;
        mFlatErr.mDoubleTag = 0.5;
        mFlatErr.mStringTag = new String("5");

        NestedObject1 mNested1Dst = new NestedObject1();

        List<XmlPushable> dstList = new ArrayList<XmlPushable>();
        dstList.add(mNested1Dst);
        dstList.add(mFlatErr); // we add the wrong object to the list

        XmlDataGrabber.grabDataOutmost(mParser, getContext(), dstList, TEMPORARY_FILE); // unmarshalling

        // Filling check
        assertTrue(mNested1Dst.equals(mNested1Src));
        assertTrue(mNested1Dst.tagCheck());

        // Everything has to be left as it was
        assertTrue(mFlatErr.mBooleanTag == true && mFlatErr.mIntegerAttr == 5 &&
                mFlatErr.mDoubleTag == 0.5 && mFlatErr.mStringTag.equals("5") && mFlatErr.tagCheck() == false);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testNestingError() equals test");
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }*/

//    /**
//     * Tests the unmarshalling of multiple comments within the XML file.
//     * This test doesn't work at the moment, I need to implement a way to have multiple
//       * metadata object registered in the XmlPushable list.    
//     * @throws IllegalArgumentException
//     * @throws IllegalStateException
//     * @throws FileNotFoundException
//     * @throws XmlPullParserException
//     * @throws IOException
//     */
//    @MediumTest
//    public void testNestingMetadataError() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {
//
//        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testNestingError] ---");
//
//        CdsectObject mCdataSrc1 = new CdsectObject("1) Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque malesuada condimentum suscipit. Fusce in sapien.");
//        HelloWorldObject mHelloWorldSrc = new HelloWorldObject();
//        CdsectObject mCdataSrc2 = new CdsectObject("2) Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id.");
//        
//        // source list containing xml objects
//        ArrayList<XmlWritable> srcList = new ArrayList<XmlWritable>();
//        srcList.add(mCdataSrc1);
//        srcList.add(mHelloWorldSrc);
//        srcList.add(mCdataSrc2);
//        
//        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testNestingError() open Android file: " + TEMPORARY_FILE);
//        XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", srcList); // marshalling
//
//        CdsectObject mCdataDst1 = new CdsectObject();
//        HelloWorldObject mHelloWorldDst = new HelloWorldObject();
//        CdsectObject mCdataDst2 = new CdsectObject();
//        
//        List<XmlPushable> dstList = new ArrayList<XmlPushable>();
//        dstList.add(mCdataDst1);
//        dstList.add(mHelloWorldDst);
//        dstList.add(mCdataDst2);
//
//        XmlDataReader.grabData(mParser, getContext(), dstList, TEMPORARY_FILE, true); // unmarshalling
//
//        // Filling check
//        assertTrue(mHelloWorldSrc.equals(mHelloWorldDst));
//        assertTrue(mHelloWorldDst.tagCheck());
//        assertTrue(mCdataSrc1.equals(mCdataDst1));
//        assertTrue(mCdataDst1.tagCheck());
//        assertTrue(mCdataSrc2.equals(mCdataDst2));
//        assertTrue(mCdataDst2.tagCheck());
//        
//        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testNestingError() equals test");
//        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
//    }

    @MediumTest
    public void testDeclaredButNotAnnotatedError() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

      Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testDeclaredButNotAnnotatedError] ---");
      boolean catched = false;
      try {
          ObjectWithUntagged obj = new ObjectWithUntagged();
          XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", obj); // marshalling
          
          XmlDataGrabber dataGrabber = new XmlDataGrabber();
          // don't care about the builder
          dataGrabber.grab(mParser, getContext(), ObjectWithUntagged.class, new ObjectWithUntaggedBuilder(), TEMPORARY_FILE);
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
        mContext.deleteFile(METADATA_FILE);
        super.tearDown();
    }
}


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
package ar.android.horkizein.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;
import ar.android.horkizein.obj.FlatObject;
import ar.android.horkizein.obj.FlatObjectCreator;
import ar.android.horkizein.obj.NestedObject1;
import ar.android.horkizein.test.Constants;
import ar.android.horkizein.test.util.XmlDataCommitter;
import ar.android.horkizein.test.util.XmlDataReader;

/**
 * Testing the exact match of a written and then read XmlPushable without children (just tags and attributes)
 */
public class EqualityTest extends AndroidTestCase {

	private static final String TAG = "EqualityTest";

	private static final String TEMPORARY_FILE = "equality.xml";

	private FlatObject mFlatSrc;
	private FlatObject mFlatDst;

	private NestedObject1 mNested1Src;
	private NestedObject1 mNested1Dst;
	
    public EqualityTest() {}

	@Override
    protected void setUp() {

    	Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() entering.");

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

	@MediumTest
    public void testFlatObjEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testFlatObjEquality] ---");

    	Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".run() open Android file: " + TEMPORARY_FILE);
    	XmlDataCommitter committer = new XmlDataCommitter();
    	committer.commitData(getContext(), TEMPORARY_FILE, mFlatSrc);

        mFlatDst = new FlatObject();
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".run() fill FlatObject dst");
    	// save the object

        XmlDataReader reader = new XmlDataReader();
        reader.grabData(getContext(), mFlatDst, TEMPORARY_FILE);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".run() equals test");
        assertTrue(mFlatDst.equals(mFlatSrc));
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

	@MediumTest
    public void testFilledNestedObj1Equality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testNestedObj1Equality] ---");

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() creating NestedObject1 src");
        mNested1Src = new NestedObject1(new FlatObjectCreator(mFlatSrc));
        
    	Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".run() open Android file: " + TEMPORARY_FILE);
    	XmlDataCommitter committer = new XmlDataCommitter();
    	committer.commitData(getContext(), TEMPORARY_FILE, mNested1Src);

    	mNested1Dst = new NestedObject1();
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".run() fill NestedObject1 dst");
    	// save the object

        XmlDataReader reader = new XmlDataReader();
        reader.grabData(getContext(), mNested1Dst, TEMPORARY_FILE);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".run() equals test");
        assertTrue(mNested1Dst.equals(mNested1Src));
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }
	
	
    @Override
    protected void tearDown() throws Exception {
    	Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".tearDown() delete file: " + TEMPORARY_FILE);
    	mContext.deleteFile(TEMPORARY_FILE);
        super.tearDown();
    }

}


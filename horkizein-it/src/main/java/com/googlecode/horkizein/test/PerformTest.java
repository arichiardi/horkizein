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
package com.googlecode.horkizein.test;

import java.io.BufferedOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.obj.FlatObject;
import com.googlecode.horkizein.obj.NestedObject1;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.PerformanceTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

/**
 * This class tests performance of read/write Horkizein functions.
 */
public class PerformTest extends AndroidTestCase implements PerformanceTestCase {

    private static final String TEMPORARY_FILE = "performance.xml";

    private static final String TAG = "PerformTest";

    private FlatObject mFlat;
    private NestedObject1 mNested1;

    @Override
    public int startPerformance(Intermediates intermediates) {
        return 0;
    }

    @Override
    public boolean isPerformanceOnly() {
        return false;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() entering.");

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() creating FlatObject src");
        mFlat = new FlatObject();
        mFlat.mBooleanAttr = false;
        mFlat.mIntegerAttr = 666;
        mFlat.mDoubleAttr = 0.666;
        mFlat.mStringAttr = new String("666");

        mFlat.mBooleanTag = true;
        mFlat.mIntegerTag = 42;
        mFlat.mDoubleTag = 0.42;
        mFlat.mStringTag = new String("42");

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".setUp() creating NestedObject1 src");
        mNested1 = new NestedObject1(mFlat);
    }

    /**
     * Read performance of a FlatObject.
     */
    @MediumTest
    public void runFlatObjectRead() throws IllegalArgumentException, IllegalStateException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".perfPlainObjectRead] ---");
        long start = System.currentTimeMillis();

        XmlSerializer serializer = android.util.Xml.newSerializer();

        BufferedOutputStream buf = new BufferedOutputStream( getContext().openFileOutput(TEMPORARY_FILE, Context.MODE_PRIVATE));
        serializer.setOutput(buf, "UTF-8");
        serializer.startDocument("UTF-8", true);

        // write the file
        mFlat.writeXml(serializer);

        serializer.endDocument();
        long elapsed = System.currentTimeMillis() - start;
        Log.i(Constants.PACKAGE_TAG_TEST, "Time elapsed: " + elapsed);

        serializer.flush();
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    /**
     * Read performance of a NestedObject1.
     */
    @MediumTest
    public void runNestedObject1Read() throws IllegalArgumentException, IllegalStateException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".perfPlainObjectRead] ---");
        long start = System.currentTimeMillis();

        XmlSerializer serializer = android.util.Xml.newSerializer();

        BufferedOutputStream buf = new BufferedOutputStream( getContext().openFileOutput(TEMPORARY_FILE, Context.MODE_PRIVATE));
        serializer.setOutput(buf, "UTF-8");
        serializer.startDocument("UTF-8", true);

        // write the file
        mNested1.writeXml(serializer);

        serializer.endDocument();
        long elapsed = System.currentTimeMillis() - start;
        Log.i(Constants.PACKAGE_TAG_TEST, "Time elapsed: " + elapsed);

        serializer.flush();
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

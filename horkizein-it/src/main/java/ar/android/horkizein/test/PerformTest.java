package ar.android.horkizein.test;

import java.io.BufferedOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.PerformanceTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;
import ar.android.horkizein.obj.FlatObject;
import ar.android.horkizein.obj.FlatObjectCreator;
import ar.android.horkizein.obj.NestedObject1;

public class PerformTest extends AndroidTestCase implements PerformanceTestCase {

    private static final String TEMPORARY_FILE = "performance.xml";

    private static final String TAG = "PerformTest";

    private FlatObject mFlat;
    private NestedObject1 mNested1;

    @Override
    public int startPerformance(Intermediates intermediates) {
        return 1;
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
        mNested1 = new NestedObject1(new FlatObjectCreator(mFlat));
    }

    /**
     * Read performance on a plain object
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
     * Read performance on a nested object (1)
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

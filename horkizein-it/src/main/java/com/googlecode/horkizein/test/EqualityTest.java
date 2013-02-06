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

import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.obj.CdsectObject;
import com.googlecode.horkizein.obj.CommentObject;
import com.googlecode.horkizein.obj.DocdeclObject;
import com.googlecode.horkizein.obj.FlatObject;
import com.googlecode.horkizein.obj.FlatObjectList;
import com.googlecode.horkizein.obj.HelloWorldObject;
import com.googlecode.horkizein.obj.NestedObject1;
import com.googlecode.horkizein.obj.ObjectWithList;
import com.googlecode.horkizein.obj.ProcessingObject;
import com.googlecode.horkizein.obj.TextObject;
import com.googlecode.horkizein.test.Constants;
import com.googlecode.horkizein.test.util.XmlDataCommitter;
import com.googlecode.horkizein.test.util.XmlDataReader;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

/**
 * This class tests the exact match of a written and then read XmlPushable.
 */
public class EqualityTest extends AndroidTestCase {

    private static final String TAG = "EqualityTest";

    private static final String TEMPORARY_FILE = "temporary.xml";
    private static final String METADATA_FILE = "metadata.xml";

    private FlatObject mFlatSrc;
    private List<FlatObject> mFlatList;

    private XmlPullParser mParser = null;

    public EqualityTest() {}

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

        buildFlatObject();
        buildFlatObjectList();
    }

    private void buildFlatObject() {
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

    private void buildFlatObjectList() {
        mFlatList = new ArrayList<FlatObject>();
        FlatObject fo = new FlatObject();
        fo.mBooleanAttr = false;
        fo.mIntegerAttr = 666;
        fo.mDoubleAttr = 0.666;
        fo.mStringAttr = new String("666");
        fo.mBooleanTag = true;
        fo.mIntegerTag = 42;
        fo.mDoubleTag = 0.42;
        fo.mStringTag = new String("42");
        mFlatList.add(fo);

        fo = new FlatObject();
        fo.mBooleanAttr = false;
        fo.mIntegerAttr = 667;
        fo.mDoubleAttr = 0.667;
        fo.mStringAttr = new String("667");
        fo.mBooleanTag = true;
        fo.mIntegerTag = 43;
        fo.mDoubleTag = 0.43;
        fo.mStringTag = new String("43");
        mFlatList.add(fo);

        fo = new FlatObject();
        fo.mBooleanAttr = false;
        fo.mIntegerAttr = 668;
        fo.mDoubleAttr = 0.668;
        fo.mStringAttr = new String("668");
        fo.mBooleanTag = true;
        fo.mIntegerTag = 44;
        fo.mDoubleTag = 0.44;
        fo.mStringTag = new String("44");
        mFlatList.add(fo);
    }

    /**
     * Tests the equality of a FlatObject.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testFlatObjEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {
        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testFlatObjEquality] ---");

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testFlatObjEquality() open Android file: " + TEMPORARY_FILE);
        XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", mFlatSrc); // marshalling

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testFlatObjEquality() fill FlatObject dst");

        FlatObject mFlatDst = new FlatObject();
        XmlDataReader.grabDataOutmost(mParser, getContext(), mFlatDst, TEMPORARY_FILE); // unmarshalling

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testFlatObjEquality() equals test");
        assertTrue(mFlatDst.equals(mFlatSrc));
        assertTrue(mFlatDst.tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of a NestedObject1.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testNestedObj1Equality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {
        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testNestedObj1Equality] ---");

        NestedObject1 mNested1Src = new NestedObject1(mFlatSrc);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testNestedObj1Equality() open Android file: " + TEMPORARY_FILE);
        XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", mNested1Src); // marshalling

        NestedObject1 mNested1Dst = new NestedObject1();
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testNestedObj1Equality() fill NestedObject1 dst");

        XmlDataReader.grabDataOutmost(mParser, getContext(), mNested1Dst, TEMPORARY_FILE); // unmarshalling

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testNestedObj1Equality() equals test");
        assertTrue(mNested1Dst.equals(mNested1Src));
        assertTrue(mNested1Dst.tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of a TextObject with a very long text.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testTextObjEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {
        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testTextObjEquality] ---");

        TextObject txtObjSrc = new TextObject("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ut mi tellus." +
                " Nam eu nunc mi, id auctor mauris. Quisque sit amet est in leo mattis ultricies. Praesent ultrices elementum odio sed faucibus. Vivamus vulputate leo at orci sodales venenatis." +
                " Sed vehicula quam ac tortor facilisis elementum. Morbi tristique massa vel arcu tempus imperdiet commodo magna condimentum." +
                " Cras ultricies justo mattis nunc accumsan hendrerit. In venenatis placerat rhoncus. Nunc et dignissim lorem.");

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testTextObjEquality() open Android file: " + TEMPORARY_FILE);
        XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", txtObjSrc); // marshalling

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testTextObjEquality() fill FlatObject dst");

        TextObject txtObjDst = new TextObject();

        XmlDataReader.grabDataOutmost(mParser, getContext(), txtObjDst, TEMPORARY_FILE); // unmarshalling

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testTextObjEquality() equals test");
        assertTrue(txtObjDst.equals(txtObjSrc));
        assertTrue(txtObjDst.tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of metadata objects.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testMetadataObjsEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testMetadataObjsEquality] ---");

        DocdeclObject docdecl = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        ProcessingObject processing = new ProcessingObject("foo bar");
        CommentObject comment = new CommentObject("this is a comment");
        CdsectObject cdsect = new CdsectObject("public String foo() { return \"bar\" }; ");

        // source list containing xml objects
        ArrayList<XmlWritable> srcList = new ArrayList<XmlWritable>();
        srcList.add(docdecl);
        srcList.add(processing);
        srcList.add(comment);
        srcList.add(cdsect);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testMetadataObjsEquality() open Android file: " + METADATA_FILE);
        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", srcList); // marshalling

        List<XmlPushable> dstList = new ArrayList<XmlPushable>();
        dstList.add(new DocdeclObject());
        dstList.add(new ProcessingObject());
        dstList.add(new CommentObject());
        dstList.add(new CdsectObject());

        XmlDataReader.grabDataOutmost(mParser, getContext(), dstList, METADATA_FILE, true); // unmarshalling

        assertTrue(docdecl.equals(dstList.get(0)));
        assertTrue(processing.equals(dstList.get(1)));
        assertTrue(comment.equals(dstList.get(2)));
        assertTrue(cdsect.equals(dstList.get(3)));

        assertTrue(((DocdeclObject)dstList.get(0)).tagCheck());
        assertTrue(((ProcessingObject)dstList.get(1)).tagCheck());
        assertTrue(((CommentObject)dstList.get(2)).tagCheck());
        assertTrue(((CdsectObject)dstList.get(3)).tagCheck());

        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of metadata objects, standard.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testMetadataSplittedText1() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testMetadataSplittedTextEquality1] ---");

        DocdeclObject docdecl = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        CommentObject commentMultiple32 = new CommentObject("This is a comment. I am not sure the library is " +
                "going to work with long text, but finger crossed and let's hope that the parser" +
                "split this very long text.\n                                            " +
                "Otherwise I don't know how to test this. No the parser is" +
                "not splitting, so I am going to add more text and see if this time I have a splitted" +
                "text and therefore XmlPullParser.TEXT events.");

        // source list containing xml objects
        ArrayList<XmlWritable> srcList = new ArrayList<XmlWritable>();
        srcList.add(docdecl);
        srcList.add(commentMultiple32);

        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", srcList); // marshalling

        List<XmlPushable> dstList = new ArrayList<XmlPushable>();
        dstList.add(new DocdeclObject());
        dstList.add(new CommentObject());

        XmlDataReader.grabDataOutmost(mParser, getContext(), dstList, METADATA_FILE, true); // unmarshalling

        assertTrue(docdecl.equals(dstList.get(0)));
        assertTrue(commentMultiple32.equals(dstList.get(1)));
        assertTrue(((DocdeclObject)dstList.get(0)).tagCheck());
        assertTrue(((CommentObject)dstList.get(1)).tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of metadata objects, with long text inside the COMMENT meta object.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testMetadataSplittedText2() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testMetadataSplittedTextEquality2] ---");

        DocdeclObject docdecl = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        CommentObject commentNotMultiple32 = new CommentObject("Qui desiderat pacem, bellum praeparat; nemo provocare ne offendere audet quem intelliget superiorem esse pugnaturem.");

        // source list containing xml objects
        ArrayList<XmlWritable> srcList = new ArrayList<XmlWritable>();
        srcList.add(docdecl);
        srcList.add(commentNotMultiple32);

        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", srcList); // marshalling

        List<XmlPushable> dstList = new ArrayList<XmlPushable>();
        dstList.add(new DocdeclObject());
        dstList.add(new CommentObject());

        XmlDataReader.grabDataOutmost(mParser, getContext(), dstList, METADATA_FILE, true); // unmarshalling

        assertTrue(docdecl.equals(dstList.get(0)));
        assertTrue(commentNotMultiple32.equals(dstList.get(1)));
        assertTrue(((DocdeclObject)dstList.get(0)).tagCheck());
        assertTrue(((CommentObject)dstList.get(1)).tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of metadata objects mixed with tags (CustomKXmlParser)
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testMetadataAndTags1() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testMetadataLongTextEquality2] ---");

        DocdeclObject docdecl = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        CommentObject commentNotMultiple32 = new CommentObject("This is a comment. This kind of comment has to be" +
                " more than 32 char but with a length not multiple of 32 so as to test the XmlFiller merging alghoritm.");

        // source list containing xml objects
        ArrayList<XmlWritable> srcList = new ArrayList<XmlWritable>();
        srcList.add(docdecl);
        srcList.add(mFlatSrc);
        srcList.add(commentNotMultiple32);

        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", srcList); // marshalling

        FlatObject flatDst = new FlatObject();

        List<XmlPushable> dstList = new ArrayList<XmlPushable>();
        dstList.add(new DocdeclObject());
        dstList.add(flatDst);
        dstList.add(new CommentObject());

        XmlDataReader.grabDataOutmost(mParser, getContext(), dstList, METADATA_FILE, true); // unmarshalling

        assertTrue(docdecl.equals(dstList.get(0)));
        assertTrue(mFlatSrc.equals(dstList.get(1)));
        assertTrue(commentNotMultiple32.equals(dstList.get(2)));
        assertTrue(((DocdeclObject)dstList.get(0)).tagCheck());
        assertTrue(((FlatObject)dstList.get(1)).tagCheck());
        assertTrue(((CommentObject)dstList.get(2)).tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of a the HelloWorldObject.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testHelloWorldEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testHelloWorldEquality] ---");

        HelloWorldObject mHelloWorldSrc = new HelloWorldObject();

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testHelloWorldEquality() open Android file: " + METADATA_FILE);
        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", mHelloWorldSrc); // marshalling

        HelloWorldObject mHelloWorldDst = new HelloWorldObject();
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testHelloWorldEquality() fill HelloWorldObject dst");

        XmlDataReader.grabDataOutmost(mParser, getContext(), mHelloWorldDst, METADATA_FILE, true); // unmarshalling

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testHelloWorldEquality() equals test");
        assertTrue(mHelloWorldDst.equals(mHelloWorldSrc));
        assertTrue(mHelloWorldDst.tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of a duplicate metadata object (among others).
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testDuplicateMetadataObjsEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testDuplicateMetadataObjsEquality] ---");

        DocdeclObject docdecl = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        DocdeclObject docdecl1 = new DocdeclObject("version=\"2.0\" encoding=\"UTF-16\"");
        ProcessingObject processing = new ProcessingObject("foo bar");
        CommentObject comment = new CommentObject("this is a comment");
        CdsectObject cdsect = new CdsectObject("public String foo() { return \"bar\" }; ");

        // source list containing xml objects
        ArrayList<XmlWritable> srcList = new ArrayList<XmlWritable>();
        srcList.add(docdecl);
        srcList.add(docdecl1);
        srcList.add(processing);
        srcList.add(comment);
        srcList.add(cdsect);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testMetadataObjsEquality() open Android file: " + METADATA_FILE);
        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", srcList); // marshalling

        List<XmlPushable> dstList = new ArrayList<XmlPushable>();
        dstList.add(new DocdeclObject());
        dstList.add(new DocdeclObject());
        dstList.add(new ProcessingObject());
        dstList.add(new CommentObject());
        dstList.add(new CdsectObject());

        XmlDataReader.grabDataOutmost(mParser, getContext(), dstList, METADATA_FILE, true); // unmarshalling

        assertTrue(docdecl.equals(dstList.get(0)));
        assertTrue(docdecl1.equals(dstList.get(1)));
        assertTrue(processing.equals(dstList.get(2)));
        assertTrue(comment.equals(dstList.get(3)));
        assertTrue(cdsect.equals(dstList.get(4)));

        assertTrue(((DocdeclObject)dstList.get(0)).tagCheck());
        assertTrue(((DocdeclObject)dstList.get(1)).tagCheck());
        assertTrue(((ProcessingObject)dstList.get(2)).tagCheck());
        assertTrue(((CommentObject)dstList.get(3)).tagCheck());
        assertTrue(((CdsectObject)dstList.get(4)).tagCheck());

        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of a multiple duplicate metadata object (among others).
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testMultiDupMetadataObjsEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testMultiDupMetadataObjsEquality] ---");

        DocdeclObject docdecl = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        DocdeclObject docdecl1 = new DocdeclObject("version=\"2.0\" encoding=\"UTF-16\"");
        ProcessingObject processing = new ProcessingObject("foo bar");
        ProcessingObject processing1 = new ProcessingObject("bar no foo");
        CommentObject comment = new CommentObject("this is a comment");
        CdsectObject cdsect = new CdsectObject("public String foo() { return \"bar\" }; ");

        // source list containing xml objects
        ArrayList<XmlWritable> srcList = new ArrayList<XmlWritable>();
        srcList.add(docdecl);
        srcList.add(docdecl1);
        srcList.add(processing);
        srcList.add(processing1);
        srcList.add(comment);
        srcList.add(cdsect);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testMetadataObjsEquality() open Android file: " + METADATA_FILE);
        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", srcList); // marshalling

        List<XmlPushable> dstList = new ArrayList<XmlPushable>();
        dstList.add(new DocdeclObject());
        dstList.add(new DocdeclObject());
        dstList.add(new ProcessingObject());
        dstList.add(new ProcessingObject());
        dstList.add(new CommentObject());
        dstList.add(new CdsectObject());

        XmlDataReader.grabDataOutmost(mParser, getContext(), dstList, METADATA_FILE, true); // unmarshalling

        assertTrue(docdecl.equals(dstList.get(0)));
        assertTrue(docdecl1.equals(dstList.get(1)));
        assertTrue(processing.equals(dstList.get(2)));
        assertTrue(processing1.equals(dstList.get(3)));
        assertTrue(comment.equals(dstList.get(4)));
        assertTrue(cdsect.equals(dstList.get(5)));

        assertTrue(((DocdeclObject)dstList.get(0)).tagCheck());
        assertTrue(((DocdeclObject)dstList.get(1)).tagCheck());
        assertTrue(((ProcessingObject)dstList.get(2)).tagCheck());
        assertTrue(((ProcessingObject)dstList.get(3)).tagCheck());
        assertTrue(((CommentObject)dstList.get(4)).tagCheck());
        assertTrue(((CdsectObject)dstList.get(5)).tagCheck());

        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of an object (ObjectWithList) which HAS-A List (FlatList).
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
     public void testObjectWithListEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

         Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testObjectWithListEquality] ---");

         ObjectWithList mObjectWithListSrc = new ObjectWithList(mFlatList);

         Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testObjectWithListEquality() open Android file: " + TEMPORARY_FILE);
         XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", mObjectWithListSrc); // marshalling

         ObjectWithList mObjectWithListDst = new ObjectWithList();
         Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testObjectWithListEquality() fill ObjectWithList dst");

         XmlDataReader.grabDataOutmost(mParser, getContext(), mObjectWithListDst, TEMPORARY_FILE); // unmarshalling

         Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testObjectWithListEquality() equals test");
         assertTrue(mObjectWithListDst.equals(mObjectWithListSrc));
         assertTrue(mObjectWithListDst.tagCheck());
         Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
     }*/

     /**
     * Tests the equality of a the FlatObjectList.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
     @MediumTest
     public void testFlatObjectListEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

         Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testFlatObjectListEquality] ---");
         FlatObjectList mFlatObjectListSrc = new FlatObjectList(mFlatList);

         Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testFlatObjectListEquality() open Android file: " + TEMPORARY_FILE);
         XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", mFlatObjectListSrc); // marshalling

         FlatObjectList mFlatObjectListDst = new FlatObjectList();
         Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testFlatObjectListEquality() fill FlatObjectList dst");

         XmlDataReader.grabDataOutmost(mParser, getContext(), (XmlPushable) mFlatObjectListDst, TEMPORARY_FILE); // unmarshalling

         Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testFlatObjectListEquality() equals test");
         assertTrue(mFlatObjectListDst.equals(mFlatObjectListSrc));
         assertTrue(mFlatObjectListDst.tagCheck());
         Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
     }

    @Override
    protected void tearDown() throws Exception {
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".tearDown() delete file: " + TEMPORARY_FILE);
        mContext.deleteFile(TEMPORARY_FILE);
        mContext.deleteFile(METADATA_FILE);
        super.tearDown();
    }
}

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.XmlFiller;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.AbstractObject;
import com.googlecode.horkizein.obj.AbstractObjectImpl1;
import com.googlecode.horkizein.obj.CdsectObject;
import com.googlecode.horkizein.obj.CommentObject;
import com.googlecode.horkizein.obj.DocdeclObject;
import com.googlecode.horkizein.obj.DummyObject;
import com.googlecode.horkizein.obj.FlatObject;
import com.googlecode.horkizein.obj.HelloWorldObject;
import com.googlecode.horkizein.obj.InterfaceObjectImpl1;
import com.googlecode.horkizein.obj.NoTextObject;
import com.googlecode.horkizein.obj.ProcessingObject;
import com.googlecode.horkizein.obj.TextObject;
import com.googlecode.horkizein.obj.builders.CdsectObjectDAO;
import com.googlecode.horkizein.obj.builders.CommentObjectDAO;
import com.googlecode.horkizein.obj.builders.DocdeclObjectDAO;
import com.googlecode.horkizein.obj.builders.DummyDAO;
import com.googlecode.horkizein.obj.builders.FlatObjectDAO;
import com.googlecode.horkizein.obj.builders.ProcessingObjectDAO;
import com.googlecode.horkizein.obj.builders.TextObjectDAO;
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

    private XmlFiller mFiller;
    private XmlPullParser mParser;
    private XmlSerializer mSerializer;
    private XmlDataCommitter mDataCommitter;
    private XmlDataReader mDataReader;
    
    private DocdeclObjectDAO mDocdeclDAO;
    private CommentObjectDAO mCommentDAO;
    private CdsectObjectDAO mCdsectDAO;
    private ProcessingObjectDAO mProcessingDAO;
    private FlatObjectDAO mFlatDAO;
    
    @Before
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
        mFiller = new XmlFiller(mParser);
        mDataReader = new XmlDataReader(getContext(), TEMPORARY_FILE, mFiller);
        // Reusable DAOs
        mDocdeclDAO = new DocdeclObjectDAO(mSerializer);
        mCommentDAO = new CommentObjectDAO(mSerializer);
        mCdsectDAO = new CdsectObjectDAO(mSerializer);
        mProcessingDAO = new ProcessingObjectDAO(mSerializer);
        mFlatDAO = new FlatObjectDAO(mSerializer);
        
        buildFlatObject();
        buildFlatObjectList();
    }

    private void buildFlatObject() {
        mFlatSrc = new FlatObject(true, 42, 0.42, new String("42"), false, 666, 0.666, new String("666"));
    }

    private void buildFlatObjectList() {
        mFlatList = new ArrayList<FlatObject>();
        FlatObject fo = new FlatObject(true, 42, 0.42, new String("42"), false, 666, 0.666, new String("666"));
        mFlatList.add(fo);
        fo = new FlatObject(true, 43, 0.43, new String("43"), false, 667, 0.667, new String("667"));
        mFlatList.add(fo);
        fo = new FlatObject(true, 44, 0.44, new String("44"), false, 668, 0.668, new String("668"));
        mFlatList.add(fo);
    }

    /**
     * Tests the equality of a FlatObject.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    @MediumTest
    public void testFlatObjEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {
        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testFlatObjEquality] ---");
        mDataCommitter.commitData(mFlatDAO, mFlatSrc); // serializing
        mFlatDAO = mDataReader.read(mFlatDAO, FlatObjectDAO.class);
        assertTrue(mFlatDAO.tagCheck());
        assertTrue(mFlatSrc.equals(mFlatDAO.build()));
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    /**
     * Tests the equality of a TextObject with a very long text.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    @MediumTest
    public void testTextObjEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {
        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testTextObjEquality] ---");

        TextObject txtObjSrc = new TextObject("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ut mi tellus." +
                " Nam eu nunc mi, id auctor mauris. Quisque sit amet est in leo mattis ultricies. Praesent ultrices elementum odio sed faucibus. Vivamus vulputate leo at orci sodales venenatis." +
                " Sed vehicula quam ac tortor facilisis elementum. Morbi tristique massa vel arcu tempus imperdiet commodo magna condimentum." +
                " Cras ultricies justo mattis nunc accumsan hendrerit. In venenatis placerat rhoncus. Nunc et dignissim lorem.");

        TextObjectDAO textDAO = new TextObjectDAO(mSerializer);
        mDataCommitter.commitData(textDAO, txtObjSrc); // serializing
        textDAO = mDataReader.read(textDAO, TextObjectDAO.class);
        assertTrue(txtObjSrc.equals(textDAO.build()));
        assertTrue(textDAO.tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    /**
     * Tests the equality of a TextObject with a very long text.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    @MediumTest
    public void testInheritance() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {
        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testInheritance] ---");
        DummyObject dummySrc = new DummyObject(new AbstractObjectImpl1(42, "foo"), new InterfaceObjectImpl1("bar"));
        DummyDAO dummyDAO = new DummyDAO(mSerializer);
        mDataCommitter.commitData(dummyDAO, dummySrc); // serializing
        dummyDAO = mDataReader.read(dummyDAO, DummyDAO.class);
        assertTrue(dummySrc.equals(dummyDAO.build()));
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }
    
    /**
     * Tests the equality of metadata objects.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    @MediumTest
    public void testMetadataObjsEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testMetadataObjsEquality] ---");

        DocdeclObject docdeclSrc = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        mDataCommitter.commitData(mDocdeclDAO, docdeclSrc); // serializing
        mDocdeclDAO = mDataReader.read(mDocdeclDAO, DocdeclObjectDAO.class);
        assertTrue(docdeclSrc.equals(mDocdeclDAO.build()));
        assertTrue(mDocdeclDAO.tagCheck());
        
        ProcessingObject processingSrc = new ProcessingObject("foo bar");
        mDataCommitter.commitData(mProcessingDAO, processingSrc); // serializing
        mProcessingDAO = mDataReader.read(mProcessingDAO, ProcessingObjectDAO.class);
        assertTrue(processingSrc.equals(mProcessingDAO.build()));
        assertTrue(mProcessingDAO.tagCheck());
        
        CommentObject commentSrc = new CommentObject("this is a comment");
        mDataCommitter.commitData(mCommentDAO, commentSrc); // serializing
        mCommentDAO = mDataReader.read(mCommentDAO, CommentObjectDAO.class);
        assertTrue(commentSrc.equals(mCommentDAO.build()));
        assertTrue(mCommentDAO.tagCheck());
        
        CdsectObject cdsectSrc = new CdsectObject("public String foo() { return \"bar\" }; ");
        mDataCommitter.commitData(mCdsectDAO, cdsectSrc); // serializing
        mCdsectDAO = mDataReader.read(mCdsectDAO, CdsectObjectDAO.class);
        assertTrue(cdsectSrc.equals(mCdsectDAO.build()));
        assertTrue(mCdsectDAO.tagCheck());
        
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    /**
     * Tests the equality of metadata objects mixed with tags (CustomKXmlParser)
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    @MediumTest
    public void testMetadataAndTags1() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testMetadataLongTextEquality2] ---");

        DocdeclObject docdecl = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        CommentObject commentNotMultiple32 = new CommentObject("This is a comment. This kind of comment has to be" +
                " more than 32 char but with a length not multiple of 32 so as to test the XmlFiller merging alghoritm.");

        // XmlWriter DAO list
        Map<XmlWritable, XmlWriter> writeMap = new HashMap<XmlWritable, XmlWriter>();
        writeMap.put(docdecl, mDocdeclDAO);
        writeMap.put(mFlatSrc, mFlatDAO);
        writeMap.put(commentNotMultiple32, mCommentDAO);
        
        mDataCommitter.commitData(writeMap); // serializing

        List<XmlPushable<?>> readMap = new ArrayList<XmlPushable<?>>();
        readMap.add(mDocdeclDAO);
        readMap.add(mFlatDAO);
        readMap.add(mCommentDAO);
        
        List<Class<XmlPushable<?>>> classesMap = new ArrayList<Class<XmlPushable<?>>>();
        classesMap.add(DocdeclObjectDAO.class);
        classesMap.add(FlatObjectDAO.class);
        classesMap.add(CommentObjectDAO.class);
        
        mDataReader.readMany(readMap).mapOf(readMap);
        
        mFiller..get(DocdeclObjectDAO.class)
        
        assertTrue(docdecl.equals());
        assertTrue(mFlatSrc.equals(dstList.get(1)));
        assertTrue(commentNotMultiple32.equals(dstList.get(2)));
        assertTrue(((DocdeclObject)dstList.get(0)).tagCheck());
        assertTrue(((FlatObject)dstList.get(1)).tagCheck());
        assertTrue(((CommentObject)dstList.get(2)).tagCheck());*/
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    /**
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
        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", mHelloWorldSrc); // serializing

        HelloWorldObject mHelloWorldDst = XmlDataReader.read(mParser, getContext(), HelloWorldObject.class, new HelloWorldObjectBuilder(), METADATA_FILE);

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
        ProcessingObjectDAO processing = new ProcessingObjectDAO("foo bar");
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
        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", srcList); // serializing

        List<Class<? extends XmlPushable>> classList = new ArrayList<Class<? extends XmlPushable>>();
        classList.add(DocdeclObject.class);
        classList.add(ProcessingObjectDAO.class);
        classList.add(CommentObject.class);
        classList.add(CdsectObject.class);
        
        List<XmlBuilder<? extends XmlPushable>> builderList = new ArrayList<XmlBuilder<? extends XmlPushable>>();
        builderList.add(new DocdeclObjectBuilder());
        builderList.add(new ProcessingObjectBuilder());
        builderList.add(new CommentObjectBuilder());
        builderList.add(new CdsectObjectBuilder());
        
        List<XmlPushable> dstList = XmlDataReader.readMany(mParser, getContext(), classList, builderList, METADATA_FILE);

        assertTrue(docdecl.equals(dstList.get(0)));
        assertTrue(docdecl1.equals(dstList.get(1)));
        assertTrue(processing.equals(dstList.get(2)));
        assertTrue(comment.equals(dstList.get(3)));
        assertTrue(cdsect.equals(dstList.get(4)));

        assertTrue(((DocdeclObject)dstList.get(0)).tagCheck());
        assertTrue(((DocdeclObject)dstList.get(1)).tagCheck());
        assertTrue(((ProcessingObjectDAO)dstList.get(2)).tagCheck());
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
        ProcessingObjectDAO processing = new ProcessingObjectDAO("foo bar");
        ProcessingObjectDAO processing1 = new ProcessingObjectDAO("bar no foo");
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
        XmlDataCommitter.commitData(getContext(), METADATA_FILE, "UTF-8", srcList); // serializing

        List<Class<? extends XmlPushable>> classList = new ArrayList<Class<? extends XmlPushable>>();
        classList.add(DocdeclObject.class);
        classList.add(ProcessingObjectDAO.class);
        classList.add(CommentObject.class);
        classList.add(CdsectObject.class);
        
        List<XmlBuilder<? extends XmlPushable>> builderList = new ArrayList<XmlBuilder<? extends XmlPushable>>();
        builderList.add(new DocdeclObjectBuilder());
        builderList.add(new ProcessingObjectBuilder());
        builderList.add(new CommentObjectBuilder());
        builderList.add(new CdsectObjectBuilder());
        
        List<XmlPushable> dstList = XmlDataReader.readMany(mParser, getContext(), classList, builderList, METADATA_FILE);

        assertTrue(docdecl.equals(dstList.get(0)));
        assertTrue(docdecl1.equals(dstList.get(1)));
        assertTrue(processing.equals(dstList.get(2)));
        assertTrue(processing1.equals(dstList.get(3)));
        assertTrue(comment.equals(dstList.get(4)));
        assertTrue(cdsect.equals(dstList.get(5)));

        assertTrue(((DocdeclObject)dstList.get(0)).tagCheck());
        assertTrue(((DocdeclObject)dstList.get(1)).tagCheck());
        assertTrue(((ProcessingObjectDAO)dstList.get(2)).tagCheck());
        assertTrue(((ProcessingObjectDAO)dstList.get(3)).tagCheck());
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
        XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", mObjectWithListSrc); // serializing

        ObjectWithList mObjectWithListDst = XmlDataReader.read(mParser, getContext(), ObjectWithList.class, new ObjectWithListBuilder(), TEMPORARY_FILE);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testObjectWithListEquality() equals test");
        assertTrue(mObjectWithListDst.equals(mObjectWithListSrc));
        assertTrue(mObjectWithListDst.tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    *//**
     * Tests the equality of NoTextObject. In particular, the outer object (NoTextObject)
     * never gets text (mNoText must not change).
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     *//*
    @MediumTest
    public void testNoTextObjectEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {
        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".tesNoTextObjectEquality] ---");
        NoTextObject mNoTextObjectSrc = new NoTextObject(new TextObject("Ciao."));

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".testNoTextObjectEquality() open Android file: " + TEMPORARY_FILE);
        XmlDataCommitter.commitData(getContext(), TEMPORARY_FILE, "UTF-8", mNoTextObjectSrc); // serializing

        NoTextObject mNoTextObjectDst = XmlDataReader.read(mParser, getContext(), NoTextObject.class, new NoTextObjectBuilder(), TEMPORARY_FILE);

        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".tesNoTextObjectEquality() equals test");
        assertTrue(mNoTextObjectDst.equals(mNoTextObjectSrc));
        assertTrue(mNoTextObjectDst.tagCheck());
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }*/
    
    @Override
    protected void tearDown() throws Exception {
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".tearDown() delete file: " + TEMPORARY_FILE);
        mContext.deleteFile(TEMPORARY_FILE);
        mContext.deleteFile(METADATA_FILE);
        super.tearDown();
    }
}


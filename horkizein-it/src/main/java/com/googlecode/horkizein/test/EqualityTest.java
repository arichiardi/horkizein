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
import java.util.LinkedList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushParser;
import com.googlecode.horkizein.XmlPushParserImpl1;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.AbstractObjectImpl1;
import com.googlecode.horkizein.obj.CdsectObject;
import com.googlecode.horkizein.obj.CommentObject;
import com.googlecode.horkizein.obj.DocdeclObject;
import com.googlecode.horkizein.obj.DummyObject;
import com.googlecode.horkizein.obj.FlatObject;
import com.googlecode.horkizein.obj.HelloWorldObject;
import com.googlecode.horkizein.obj.HelloWorldObject.Favorite;
import com.googlecode.horkizein.obj.InterfaceObjectImpl1;
import com.googlecode.horkizein.obj.NoTextObject;
import com.googlecode.horkizein.obj.ProcessingObject;
import com.googlecode.horkizein.obj.TextObject;
import com.googlecode.horkizein.obj.daos.CdsectObjectDAO;
import com.googlecode.horkizein.obj.daos.CommentObjectDAO;
import com.googlecode.horkizein.obj.daos.DocdeclObjectDAO;
import com.googlecode.horkizein.obj.daos.DummyDAO;
import com.googlecode.horkizein.obj.daos.FlatObjectDAO;
import com.googlecode.horkizein.obj.daos.HelloWorldDAO;
import com.googlecode.horkizein.obj.daos.NoTextObjectDAO;
import com.googlecode.horkizein.obj.daos.ProcessingObjectDAO;
import com.googlecode.horkizein.obj.daos.TextObjectDAO;
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

    private FlatObject mFlatSrc;
    private List<FlatObject> mFlatList;

    private XmlPushParser mXmlPushParser;
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
        mXmlPushParser = new XmlPushParserImpl1(mParser);
        mDataReader = new XmlDataReader(getContext(), TEMPORARY_FILE, mXmlPushParser);
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
        FlatObjectDAO mFlatDAORead = mDataReader.read(mFlatDAO, FlatObjectDAO.class);
        assertNotSame(mFlatDAO, mFlatDAORead);
        assertTrue(mFlatDAORead.tagCheck());
        assertTrue(mFlatSrc.equals(mFlatDAORead.build()));
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
        TextObjectDAO textDAORead = mDataReader.read(textDAO, TextObjectDAO.class);
        assertNotSame(textDAO, textDAORead);
        assertTrue(textDAORead.tagCheck());
        assertTrue(txtObjSrc.equals(textDAORead.build()));
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
        DummyDAO dummyDAORead = mDataReader.read(dummyDAO, DummyDAO.class);
        assertNotSame(dummyDAO, dummyDAORead);
        assertTrue(dummySrc.equals(dummyDAORead.build()));
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
        DocdeclObjectDAO docdeclDAORead = mDataReader.read(mDocdeclDAO, DocdeclObjectDAO.class);
        assertNotSame(mDocdeclDAO, docdeclDAORead);
        assertTrue(docdeclDAORead.tagCheck());
        assertTrue(docdeclSrc.equals(docdeclDAORead.build()));
        
        ProcessingObject processingSrc = new ProcessingObject("foo bar");
        mDataCommitter.commitData(mProcessingDAO, processingSrc); // serializing
        ProcessingObjectDAO processingDAORead = mDataReader.read(mProcessingDAO, ProcessingObjectDAO.class);
        assertNotSame(mProcessingDAO, processingDAORead);
        assertTrue(processingDAORead.tagCheck());
        assertTrue(processingSrc.equals(processingDAORead.build()));
        
        CommentObject commentSrc = new CommentObject("this is a comment");
        mDataCommitter.commitData(mCommentDAO, commentSrc); // serializing
        CommentObjectDAO commentDAO = mDataReader.read(mCommentDAO, CommentObjectDAO.class);
        assertNotSame(mCommentDAO, commentDAO);
        assertTrue(commentDAO.tagCheck());
        assertTrue(commentSrc.equals(commentDAO.build()));
        
        CdsectObject cdsectSrc = new CdsectObject("public String foo() { return \"bar\" }; ");
        mDataCommitter.commitData(mCdsectDAO, cdsectSrc); // serializing
        CdsectObjectDAO cdsectDAO = mDataReader.read(mCdsectDAO, CdsectObjectDAO.class);
        assertNotSame(mCdsectDAO, cdsectDAO);
        assertTrue(cdsectDAO.tagCheck());
        assertTrue(cdsectSrc.equals(cdsectDAO.build()));
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

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testMetadataAndTags1] ---");

        DocdeclObject docdecl = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        CommentObject commentNotMultiple32 = new CommentObject("This is a comment. This kind of comment has to be" +
                " more than 32 char but with a length not multiple of 32 so as to test the XmlPushParser merging alghoritm.");

        // XmlWriter DAO list
        List<XmlWritable> writableList = new LinkedList<XmlWritable>();
        writableList.add(docdecl);
        writableList.add(mFlatSrc);
        writableList.add(commentNotMultiple32);
        
        List<XmlWriter> writerList = new LinkedList<XmlWriter>();
        writerList.add(mDocdeclDAO);
        writerList.add(mFlatDAO);
        writerList.add(mCommentDAO);
        
        mDataCommitter.commitData(writerList, writableList); // serializing

        List<XmlPushable<?>> readMap = new ArrayList<XmlPushable<?>>();
        readMap.add(mDocdeclDAO);
        readMap.add(mFlatDAO);
        readMap.add(mCommentDAO);
        
        mDataReader.readMany(readMap); // deserializing
        
        DocdeclObject docDst = mXmlPushParser.buildFirstOf(DocdeclObjectDAO.class);
        FlatObject flatDst = mXmlPushParser.buildFirstOf(FlatObjectDAO.TAG);
        CommentObject commDst = mXmlPushParser.buildFirstOf(CommentObjectDAO.class);
        
        assertTrue(docdecl.equals(docDst));
        assertTrue(mFlatSrc.equals(flatDst));
        assertTrue(commentNotMultiple32.equals(commDst));
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    /**
     * Tests the equality of a the HelloWorldObject.
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    @MediumTest
    public void testHelloWorldEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testHelloWorldEquality] ---");
        String cHelloWorld = "main() {" +
                                "extrn a, b, c;" +
                                "putchar(a); putchar(b); putchar(c); putchar('!*n');" +
                             "}" +
                             "a \'hell\';" +
                             "b \'o, w\';" +
                             "c \'orld\';";

        String javaHelloWorld = "class HelloWorldApp {" +
                                    "public static void main(String[] args) {" + 
                                        "System.out.println(\"Hello World!\"); " +
                                    "}" +
                                "}";
        
        HelloWorldObject mHelloWorldSrc = new HelloWorldObject(Favorite.C, 
                "The C implementation rocks!", "The Java implementation is...Java.",
                new CdsectObject(cHelloWorld), new CdsectObject(javaHelloWorld));

        HelloWorldDAO helloWorldDAO = new HelloWorldDAO(mSerializer);
        mDataCommitter.commitData(helloWorldDAO, mHelloWorldSrc); // serializing

        HelloWorldDAO helloWorldReadDAO = mDataReader.read(helloWorldDAO, HelloWorldDAO.class);
        assertNotSame(helloWorldDAO, helloWorldReadDAO);
        assertTrue(helloWorldReadDAO.tagCheck());
        assertTrue(mHelloWorldSrc.equals(helloWorldReadDAO.build()));
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    /**
    * Tests the equality of a multiple duplicate metadata object (among others).
    * @throws IllegalArgumentException
    * @throws IllegalStateException
    * @throws FileNotFoundException
    * @throws XmlPullParserException
    * @throws IOException
    */
    @MediumTest
    public void testOrderedMetadataObjsEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {

        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".testOrderedMetadataObjsEquality] ---");

        DocdeclObject docdecl = new DocdeclObject("version=\"1.0\" encoding=\"UTF-8\"");
        ProcessingObject processing = new ProcessingObject("foo bar");
        ProcessingObject processing1 = new ProcessingObject("bar but not foo");
        CommentObject comment = new CommentObject("this is a comment");

        // source list containing xml objects
        List<XmlWritable> writableList = new LinkedList<XmlWritable>();
        writableList.add(docdecl);
        writableList.add(processing);
        writableList.add(comment);
        writableList.add(mFlatList.get(0));
        writableList.add(processing1);
        writableList.add(mFlatList.get(2));
        writableList.add(mFlatList.get(1));
        
        List<XmlWriter> writerList = new LinkedList<XmlWriter>();
        writerList.add(mDocdeclDAO);
        writerList.add(mProcessingDAO);
        writerList.add(mCommentDAO);
        writerList.add(mFlatDAO);
        writerList.add(mProcessingDAO);
        writerList.add(mFlatDAO);
        writerList.add(mFlatDAO);
        
        mDataCommitter.commitData(writerList, writableList); // serializing
        
        List<XmlPushable<?>> readMap = new ArrayList<XmlPushable<?>>();
        readMap.add(mDocdeclDAO);
        readMap.add(mFlatDAO);
        readMap.add(mProcessingDAO);
        readMap.add(mCommentDAO);
        mDataReader.readMany(readMap); // deserializing
        
        List<DocdeclObject> docList = mXmlPushParser.buildListOf(DocdeclObjectDAO.class);
        assertTrue(docList.size() == 1);
        assertEquals(docList.get(0), docdecl);
        
        List<CommentObject> commList = mXmlPushParser.buildListOf(CommentObjectDAO.TAG);
        assertTrue(commList.size() == 1);
        assertEquals(commList.get(0), comment);
        
        List<FlatObject> flatList = mXmlPushParser.buildListOf(FlatObjectDAO.TAG);
        assertTrue(flatList.size() == 3);
        assertEquals(flatList.get(0), mFlatList.get(0));
        assertEquals(flatList.get(1), mFlatList.get(2));
        assertEquals(flatList.get(2), mFlatList.get(1));
        
        List<ProcessingObject> procList = mXmlPushParser.buildListOf(ProcessingObjectDAO.class);
        assertTrue(procList.size() == 2);
        assertEquals(procList.get(0), processing);
        assertEquals(procList.get(1), processing1);
        
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }

    /**
     * Tests the equality of NoTextObject. In particular, the outer object (NoTextObject)
     * never gets text (mNoText must not change).
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     * @throws XmlPullParserException
     * @throws IOException
     */
    @MediumTest
    public void testNoTextObjectEquality() throws IllegalArgumentException, IllegalStateException, FileNotFoundException, XmlPullParserException, IOException {
        Log.i(Constants.PACKAGE_TAG_TEST, "--- [" + TAG + ".tesNoTextObjectEquality] ---");
        
        NoTextObject noTextObjectSrc = new NoTextObject("", new TextObject("Ciao."));
        NoTextObjectDAO noTextObjDAO = new NoTextObjectDAO(mSerializer);
        
        mDataCommitter.commitData(noTextObjDAO, noTextObjectSrc); // serializing
        
        NoTextObjectDAO noTextObjDAORead = mDataReader.read(noTextObjDAO, NoTextObjectDAO.class);
        assertNotSame(noTextObjDAO, noTextObjDAORead);
        assertTrue(noTextObjDAORead.tagCheck());
        assertTrue(noTextObjectSrc.equals(noTextObjDAORead.build()));
        
        Log.i(Constants.PACKAGE_TAG_TEST, "-----------------------------------------");
    }
    
    @Override
    protected void tearDown() throws Exception {
        Log.i(Constants.PACKAGE_TAG_TEST, TAG + ".tearDown() delete file: " + TEMPORARY_FILE);
        mContext.deleteFile(TEMPORARY_FILE);
        super.tearDown();
    }
}


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
package com.googlecode.horkizein.obj.daos;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushParser;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.HelloWorldObject;
import com.googlecode.horkizein.obj.HelloWorldObject.Favorite;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Horkizein implementation of "Hello World!". The class contains two tags, one for the C and one for the Java
 * implementation. The code is enclosed in a CDATA section. The attribute "favourite" specifies which one is
 * preferred by a fictitious application.
 */
@XmlTag (
    value = HelloWorldDAO.TAG,
    additionalTags = { HelloWorldDAO.C_TAG,  HelloWorldDAO.JAVA_TAG, XmlPushParser.CDSECT_TAG }
)
public class HelloWorldDAO implements XmlPushable<HelloWorldObject>, XmlWriter {

    // Dependency
    private final XmlSerializer mSerializer;
    
    public static final String TAG = "helloWorld";
    public static final String C_TAG = "c";
    public static final String JAVA_TAG = "java";
    public static final String FAVOURITE_ATTR = "favourite";

    // Init value
    private static final String C_TEXT = "This is the C implementation.";
    private static final String JAVA_TEXT =  "This is the Java implementation.";

    // watch dog
    private boolean wdPushedStartTag;
    private boolean wdPushedEndTag;
    private boolean wdIsCStartTag;
    private boolean wdIsCEndTag;
    private boolean wdIsJavaStartTag;
    private boolean wdIsJavaEndTag;

    private Favorite mFavoriteLanguage;

    private String mCText;
    private String mJavaText;

    // Children
    private CdsectObjectDAO mHelloWorld_c;
    private CdsectObjectDAO mHelloWorld_java;
    

    /**
     * Creates an HelloWorldObject.
     * @param serializer The serializer.
     */
    public HelloWorldDAO(XmlSerializer serializer) {
        mSerializer = serializer;
        reset();
    }

    @Override
    public void attribute(String tag, String prefix, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".attribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);
        if (wdPushedStartTag) {
            if (tag.equals(TAG) && name.equals(FAVOURITE_ATTR)) {
                // ugly
                if (value.equals(HelloWorldObject.JAVA_FAV_STRING) == true) {
                    mFavoriteLanguage = Favorite.JAVA;
                } else if (value.equals(HelloWorldObject.C_FAV_STRING) == true) {
                    mFavoriteLanguage = Favorite.C;
                }
            }
        }
    }

    @Override
    public void startTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".startTag() - TAG: " + tag);
        if (tag.equals(TAG)) {
            wdPushedStartTag = true;
        }
        if (wdPushedStartTag) {
            if (tag.equals(C_TAG)) {
                wdIsCStartTag = true;
            }
            if (tag.equals(JAVA_TAG)) {
                wdIsJavaStartTag = true;
            }

            if (wdIsCStartTag == true) {
                mHelloWorld_c.startTag(tag);
            }
            if (wdIsJavaStartTag == true) {
                mHelloWorld_java.startTag(tag);
            }
        }
    }

    @Override
    public void text(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);
        if (wdPushedStartTag) {
            //if (tag.equals(TAG)) {
            // No text for the helloWorld tag
            //}
            if (wdIsCStartTag == true) {
                if (tag.equals(C_TAG)) {
                    mCText = text;
                }
                mHelloWorld_c.text(tag, text);
            }
            if (wdIsJavaStartTag == true) {
                if (tag.equals(JAVA_TAG)) {
                    mJavaText = text;
                }
                mHelloWorld_java.text(tag, text);
            }
        }
    }

    @Override
    public void endTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag() - TAG: " + tag);
        if(wdPushedStartTag) {
            if (wdIsCStartTag == true) {
                mHelloWorld_c.endTag(tag);
                // watch dog
                if (tag.equals(C_TAG)) {
                    wdIsCEndTag = true;
                    wdIsCStartTag = false;
                }
            }
            if (wdIsJavaStartTag == true) {
                mHelloWorld_java.endTag(tag);
                // watch dog
                if (tag.equals(JAVA_TAG)) {
                    wdIsJavaEndTag = true;
                    wdIsJavaStartTag = false;
                }
            }
            if (tag.equals(TAG)) {
                wdPushedEndTag = true;
                wdPushedStartTag = false;
            }
        }
    }

    void reset() {
        mFavoriteLanguage = Favorite.JAVA;
        mCText = C_TEXT;
        mJavaText = JAVA_TEXT;
        mHelloWorld_c = new CdsectObjectDAO(mSerializer);
        mHelloWorld_java = new CdsectObjectDAO(mSerializer);
    }
    
    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
        return (mHelloWorld_c.tagCheck() &&
                mHelloWorld_java.tagCheck() &&
                !wdIsCStartTag &&
                wdIsCEndTag &&
                !wdIsJavaStartTag &&
                wdIsJavaEndTag &&
                !wdPushedStartTag &&
                wdPushedEndTag);
    }

    @Override
    public HelloWorldObject build() {
        HelloWorldObject instance = new HelloWorldObject(mFavoriteLanguage, mCText, mJavaText,
                mHelloWorld_c.build(),mHelloWorld_java.build());
        reset();
        return instance;
    }

    @Override
    public XmlPushable<HelloWorldObject> shallowClone() {
        return new HelloWorldDAO(mSerializer);
    }

    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mSerializer);
    }
}


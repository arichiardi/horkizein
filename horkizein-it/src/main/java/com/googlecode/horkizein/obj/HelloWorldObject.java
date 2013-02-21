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
package com.googlecode.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlFiller;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Horkizein implementation of "Hello World!". The class contains two tags, one for the C and one for the Java
 * implementation. The code is enclosed in a CDATA section. The attribute "favourite" specifies which one is
 * preferred by a fictitious application.
 */
@XmlTag (
    value = "helloWorld",
    additionalTags = { "c", "java", XmlFiller.CDSECT_TAG }
)

public class HelloWorldObject implements XmlPushable, XmlWritable {

    private static final String TAG = "helloWorld";
    private static final String C_TAG = "c";
    private static final String JAVA_TAG = "java";
    private static final String FAVOURITE_ATTR = "favourite";
    private static final String C_TEXT = "This is the C implementation.";
    private static final String JAVA_TEXT =  "This is the Java implementation.";
    private static final String NULL = "null";

    // watch dog
    private boolean wdPushedStartTag;
    private boolean wdPushedEndTag;
    private boolean wdIsCStartTag;
    private boolean wdIsCEndTag;
    private boolean wdIsJavaStartTag;
    private boolean wdIsJavaEndTag;

    //private boolean mIsC;
    //private boolean mIsJava;

    private String mFavouriteLanguage;

    private String mCText;
    private String mJavaText;

    // Children
    private CdsectObject mHelloWorld_c;
    private CdsectObject mHelloWorld_java;

    /**
     * Creates an HelloWorldObject.
     */
    public HelloWorldObject() {
        mFavouriteLanguage = JAVA_TAG;
        mCText = C_TEXT;
        mJavaText = JAVA_TEXT;

        mHelloWorld_c = new CdsectObject("main() {" +
                "extrn a, b, c;" +
                "putchar(a); putchar(b); putchar(c); putchar('!*n');" +
                "}" +
                "a \'hell\';" +
                "b \'o, w\';" +
                "c \'orld\';");

        mHelloWorld_java = new CdsectObject("class HelloWorldApp {" +
                "public static void main(String[] args) {" + 
                "System.out.println(\"Hello World!\"); " +
                "}" +
                "}");
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushAttribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);
        if (wdPushedStartTag) {
            if (tag.equals(TAG) && name.equals(FAVOURITE_ATTR)) {
                mFavouriteLanguage = value;
            }
        }
    }

    @Override
    public void pushStartTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag() - TAG: " + tag);
        if (tag.equals(TAG)) {
            wdPushedStartTag = true;
            mFavouriteLanguage = NULL;
            mCText = NULL;
            mJavaText = NULL;
        }
        if (wdPushedStartTag) {
            if (tag.equals(C_TAG)) {
                wdIsCStartTag = true;
            }
            if (tag.equals(JAVA_TAG)) {
                wdIsJavaStartTag = true;
            }

            if (wdIsCStartTag == true) {
                mHelloWorld_c.pushStartTag(tag);
            }
            if (wdIsJavaStartTag == true) {
                mHelloWorld_java.pushStartTag(tag);
            }
        }
    }

    @Override
    public void pushText(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);
        if (wdPushedStartTag) {
            //if (tag.equals(TAG)) {
            // No text for the helloWorld tag
            //}
            if (wdIsCStartTag == true) {
                if (tag.equals(C_TAG)) {
                    mCText = text;
                }
                mHelloWorld_c.pushText(tag, text);
            }
            if (wdIsJavaStartTag == true) {
                if (tag.equals(JAVA_TAG)) {
                    mJavaText = text;
                }
                mHelloWorld_java.pushText(tag, text);
            }
        }
    }


    @Override
    public void pushEndTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag() - TAG: " + tag);
        if(wdPushedStartTag) {
            if (wdIsCStartTag == true) {
                mHelloWorld_c.pushEndTag(tag);
                // watch dog
                if (tag.equals(C_TAG)) {
                    wdIsCEndTag = true;
                    wdIsCStartTag = false;
                }
            }
            if (wdIsJavaStartTag == true) {
                mHelloWorld_java.pushEndTag(tag);
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        HelloWorldObject o = (HelloWorldObject)obj;

        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mFavouriteLanguage: " + mFavouriteLanguage + " - o.mFavouriteLanguage: "  + o.mFavouriteLanguage);
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mCText: " + mCText + " - o.mCText: "  + o.mCText);
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mJavaText: " + mJavaText + " - o.mJavaText: "  + o.mJavaText);

        return (mFavouriteLanguage.equals(o.mFavouriteLanguage) &&
                mCText.equals(o.mCText) &&
                mJavaText.equals(o.mJavaText) &&
                (mHelloWorld_java == o.mHelloWorld_java || (mHelloWorld_java != null && mHelloWorld_java.equals(o.mHelloWorld_java))) &&
                (mHelloWorld_c == o.mHelloWorld_c || (mHelloWorld_c != null && mHelloWorld_c.equals(o.mHelloWorld_c))));
    }

    @Override
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
        out.startTag("", TAG);
        out.attribute("", FAVOURITE_ATTR, mFavouriteLanguage);

        out.startTag("", C_TAG);
        out.text(mCText);
        if(mHelloWorld_c != null)
            mHelloWorld_c.writeXml(out);
        out.endTag("", C_TAG);

        out.startTag("", JAVA_TAG);
        out.text(mJavaText);
        if(mHelloWorld_java != null)
            mHelloWorld_java.writeXml(out);
        out.endTag("", JAVA_TAG);

        out.endTag("", TAG);
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
}


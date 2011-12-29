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
package ar.android.horkizein.obj;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import ar.android.horkizein.XmlPushable;
import ar.android.horkizein.XmlWritable;
import ar.android.horkizein.test.Constants;

/**
 * Horkizein implementation of "Hello World!". The class contains two tags, one for the C and one for the Java
 * implementation. The code is enclosed in a CDATA section. The attribute "favourite" specifies which one is
 * preferred by a fictitious application.
 */
public class HelloWorldObject implements XmlPushable, XmlWritable {
	
	private static final String TAG = "helloWorld";
	private static final String C_TAG = "c";
	private static final String JAVA_TAG = "java";
	private static final String FAVOURITE_ATTR = "favourite";

    private final static String C_TEXT = "This is Hello World! written in C";
    private final static String JAVA_TEXT = "This is Hello World! written in JAVA";
	
    // watch dog
    private boolean wdPushedStartTag;
    private boolean wdPushedEndTag;
    private boolean wdIsCStartTag;
    private boolean wdIsCEndTag;
    private boolean wdIsJavaStartTag;
    private boolean wdIsJavaEndTag;
    
    private boolean mIsC;
    private boolean mIsJava;
    
    private String mFavouriteLanguage;

    // Children
    public CdsectObject mHelloWorld_c;
    public CdsectObject mHelloWorld_java;

    /**
     * Creates an HelloWorldObject.
     */
    public HelloWorldObject() {
    	mFavouriteLanguage = C_TAG;
    	
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

    /**
     * @see ar.android.horkizein.xml.XmlPushable#getTag()
     */
    public String getTag() {
        return TAG;
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushAttribute(java.lang.String, java.lang.String, java.lang.String)
     */
    public void pushAttribute(String tag, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushAttribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);
        if (wdPushedStartTag) {
        	if (tag.equals(TAG) && name.equals(FAVOURITE_ATTR)) {
        		mFavouriteLanguage = value;
        	}
        }
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushText(java.lang.String, java.lang.String)
     */
    public void pushText(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);
        if (wdPushedStartTag) {
        	if (mIsC == true) {
        		if (tag.equals(CdsectObject.TAG)) {
        			mHelloWorld_c.pushText(tag, text);
        		}
        	}
        	if (mIsJava == true) {
        		if (tag.equals(CdsectObject.TAG)) {
        			mHelloWorld_java.pushText(tag, text);
        		}
        	}
        }
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushEndTag(java.lang.String)
     */
    public void pushEndTag(String tag) {
    	Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag() - TAG: " + tag);
        if(wdPushedStartTag) {
        	if (tag.equals(C_TAG)) {
        		mIsC = false;
        	}
        	// watch dog
        	if (tag.equals(C_TAG) && wdIsCStartTag) {
        		wdIsCEndTag = true;
        	}
        	
        	if (tag.equals(JAVA_TAG)) {
        		mIsJava = false;
        	}
        	// watch dog
        	if (tag.equals(C_TAG) && wdIsJavaStartTag) {
        		wdIsJavaEndTag = true;
        	}
        }
        
        if (tag.equals(TAG) && wdPushedStartTag)
        	wdPushedEndTag = true;
    }

    /**
     * @see ar.android.horkizein.xml.XmlPushable#pushStartTag(java.lang.String)
     */
    public void pushStartTag(String tag) {
    	Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag() - TAG: " + tag);
    	if (tag.equals(TAG)) {
        	wdPushedStartTag = true;
    	}
    	
        if (wdPushedStartTag) {
        	if (tag.equals(C_TAG)) {
        		wdIsCStartTag = true;
        		mIsC = true;
        	}
        	if (tag.equals(JAVA_TAG)) {
        		wdIsJavaStartTag = true;
        		mIsJava = true;
        	}
        	
        	if (mIsC == true) {
        		if (tag.equals(CdsectObject.TAG)) {
        			mHelloWorld_c.pushStartTag(tag);
        		}
        	}
        	if (mIsJava == true) {
        		if (tag.equals(CdsectObject.TAG)) {
        			mHelloWorld_java.pushStartTag(tag);
        		}
        	}
        }
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        HelloWorldObject o = (HelloWorldObject)obj;
        return ((mHelloWorld_java == o.mHelloWorld_java || (mHelloWorld_java != null && mHelloWorld_java.equals(o.mHelloWorld_java))) &&
        		(mHelloWorld_c == o.mHelloWorld_c || (mHelloWorld_c != null && mHelloWorld_c.equals(o.mHelloWorld_c))));
    }

    /**
     * @see ar.android.horkizein.xml.XmlWritable#writeXml(org.xmlpull.v1.XmlSerializer)
     */
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {

        out.startTag("", TAG);
        out.attribute("", FAVOURITE_ATTR, mFavouriteLanguage);
        
        out.startTag("", C_TAG);
        out.text(C_TEXT);
        if(mHelloWorld_c != null)
        	mHelloWorld_c.writeXml(out);
        out.endTag("", C_TAG);
        
        out.startTag("", JAVA_TAG);
        out.text(JAVA_TEXT);
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
    	return (mHelloWorld_c.tagCheck() ||
    			mHelloWorld_java.tagCheck() ||
    			wdIsCStartTag ||
    			wdIsCEndTag ||
    			wdIsJavaStartTag ||
    			wdIsJavaEndTag ||
    			wdPushedStartTag ||
    			wdPushedEndTag);
    }
}


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

import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.obj.daos.HelloWorldDAO;

/**
 * Horkizein implementation of "Hello World!". The class contains two tags, one for the C and one for the Java
 * implementation. The code is enclosed in a CDATA section. The attribute "favourite" specifies which one is
 * preferred by a fictitious application.
 */
public class HelloWorldObject implements XmlWritable {

    public static final String TAG = "helloWorld";
    public final static String JAVA_FAV_STRING = "java";
    public final static String C_FAV_STRING = "c";
    
    public static enum Favorite {
        JAVA, C
    }
    
    private final Favorite mFavoriteLanguage;
    private final String mCText;
    private final String mJavaText;
    
    // Code samples
    private final CdsectObject mHelloWorld_c;
    private final CdsectObject mHelloWorld_java;

    public HelloWorldObject(Favorite favouriteLanguage, String cText, String javaText, CdsectObject c, CdsectObject java) {
        mFavoriteLanguage = favouriteLanguage;
        mCText = cText;
        mJavaText = javaText;
        mHelloWorld_c = c;
        mHelloWorld_java = java;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        HelloWorldObject o = (HelloWorldObject)obj;

        return (mFavoriteLanguage.equals(o.mFavoriteLanguage) &&
                mCText.equals(o.mCText) &&
                mJavaText.equals(o.mJavaText) &&
                (mHelloWorld_java == o.mHelloWorld_java || (mHelloWorld_java != null && mHelloWorld_java.equals(o.mHelloWorld_java))) &&
                (mHelloWorld_c == o.mHelloWorld_c || (mHelloWorld_c != null && mHelloWorld_c.equals(o.mHelloWorld_c))));
    }

    public final Favorite getFavouriteLanguage() {
        return mFavoriteLanguage;
    }

    public final String getCText() {
        return mCText;
    }

    public final String getJavaText() {
        return mJavaText;
    }
    /**
     * Note: This method safely return a reference just because the 
     * returned object is immutable.
     * @return A Cdsect Object.
     */
    public final CdsectObject getHelloWorld_c() {
        return mHelloWorld_c;
    }
    /**
     * Note: This method safely return a reference just because the 
     * returned object is immutable.
     * @return A Cdsect Object.
     */
    public final CdsectObject getHelloWorld_java() {
        return mHelloWorld_java;
    }

    @Override
    public void writeXml(XmlSerializer serializer) throws IOException, IllegalStateException, IllegalArgumentException {
        serializer.startTag("", TAG);
        switch (mFavoriteLanguage) {
        case JAVA:
            serializer.attribute("", HelloWorldDAO.FAVOURITE_ATTR, JAVA_FAV_STRING);
            break;
        case C:
            serializer.attribute("", HelloWorldDAO.FAVOURITE_ATTR, C_FAV_STRING);
            break;
        }
        
        serializer.startTag("", HelloWorldDAO.C_TAG);
        serializer.text(mCText);
        mHelloWorld_c.writeXml(serializer);
        serializer.endTag("", HelloWorldDAO.C_TAG);
        serializer.startTag("", HelloWorldDAO.JAVA_TAG);
        serializer.text(mJavaText);
        mHelloWorld_java.writeXml(serializer);
        serializer.endTag("", HelloWorldDAO.JAVA_TAG);
        serializer.endTag("", TAG);
    }
}


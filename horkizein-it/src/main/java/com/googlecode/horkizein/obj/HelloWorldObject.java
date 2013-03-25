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

/**
 * Horkizein implementation of "Hello World!". The class contains two tags, one for the C and one for the Java
 * implementation. The code is enclosed in a CDATA section. The attribute "favourite" specifies which one is
 * preferred by a fictitious application.
 */
public class HelloWorldObject {

    public static final String TAG = "helloWorld";
    public static final String C_TAG = "c";
    public static final String JAVA_TAG = "java";
    public static final String FAVOURITE_ATTR = "favourite";

    private final String mFavouriteLanguage;
    private final String mCText;
    private final String mJavaText;
    
    // Code samples
    private final CdsectObject mHelloWorld_c;
    private final CdsectObject mHelloWorld_java;

    public HelloWorldObject(String favouriteLanguage, String cText, String javaText, CdsectObject c, CdsectObject java) {
        mFavouriteLanguage = favouriteLanguage;
        mCText = cText;
        mJavaText = javaText;
        mHelloWorld_c = c;
        mHelloWorld_java = java;
        
       /* mHelloWorld_c = new CdsectObject("main() {" +
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
                "}");*/
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        HelloWorldObject o = (HelloWorldObject)obj;

        return (mFavouriteLanguage.equals(o.mFavouriteLanguage) &&
                mCText.equals(o.mCText) &&
                mJavaText.equals(o.mJavaText) &&
                (mHelloWorld_java == o.mHelloWorld_java || (mHelloWorld_java != null && mHelloWorld_java.equals(o.mHelloWorld_java))) &&
                (mHelloWorld_c == o.mHelloWorld_c || (mHelloWorld_c != null && mHelloWorld_c.equals(o.mHelloWorld_c))));
    }

    public final String getFavouriteLanguage() {
        return mFavouriteLanguage;
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
     * @return 
     */
    public final CdsectObject getHelloWorld_c() {
        return mHelloWorld_c;
    }
    /**
     * Note: This method safely return a reference just because the 
     * returned object is immutable.
     * @return 
     */
    public final CdsectObject getHelloWorld_java() {
        return mHelloWorld_java;
    }
}


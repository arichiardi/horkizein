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
package com.googlecode.horkizein.obj;

import com.googlecode.horkizein.XmlPushableCreator;

/**
 * Creator implementation for building FlatObjects.
 */
public class FlatObjectCreator implements XmlPushableCreator<FlatObject> {

    // This object tag
    public final static String TAG = "flat_obj_c";

    // Shallow copy source
    private FlatObject mCopySrc;

    /**
     * This constructor accept a FlatObject and store its reference. Just for testing purpose.
     * @param src
     */
    public FlatObjectCreator(FlatObject src) {
        mCopySrc = src;
    }

    /**
     * Ctor.
     */
    public FlatObjectCreator() {}

    /**

     */
    public String getTag() {
        return TAG;
    }


    @Override
    public FlatObject create() {
        FlatObject tmp;

        if (mCopySrc != null)
            // Just for test, passing the reference
            tmp = mCopySrc;
        else
            // new empty object otherwise
            tmp = new FlatObject();

        return tmp;
    }
}

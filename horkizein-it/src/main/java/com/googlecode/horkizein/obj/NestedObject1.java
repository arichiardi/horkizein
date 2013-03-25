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
 * Implementation of an XmlPushable which contains a FlatObject.
 */
public class NestedObject1 {
    // This object tag
    public final static String TAG = "nested_obj1";
    // child
    public final FlatObject mFlatObject;

    /**
     * Creates a NestedObject1 that contains a shallow copy of a FlatObject
     * @param flatSrc The child.
     */
    public NestedObject1(FlatObject flatSrc) {
        mFlatObject = flatSrc; // test purpose
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        NestedObject1 o = (NestedObject1)obj;
        return (mFlatObject == o.mFlatObject || (mFlatObject != null && mFlatObject.equals(o.mFlatObject)));
    }
    
    /**
     * Note: This method safely return a reference just because the 
     * returned object is immutable.
     * @return 
     */
    public FlatObject getFlatObject() {
        return mFlatObject;
    }
}

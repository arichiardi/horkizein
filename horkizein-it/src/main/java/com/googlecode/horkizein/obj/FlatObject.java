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
 * Implementation of an object without any XmlPushable child. It contains four attributes and four tags.
 */

public class FlatObject {
    // This object tag
    public final static String TAG = "flat_obj";

    private final boolean mBooleanTag;
    private final int mIntegerTag;
    private final double mDoubleTag;
    private final String mStringTag;

    private final boolean mBooleanAttr;
    private final int mIntegerAttr;
    private final double mDoubleAttr;
    private final String mStringAttr;

    /**
     * Ctor.
     * @param booleanTag
     * @param integerTag
     * @param doubleTag
     * @param stringTag
     * @param booleanAttr
     * @param integerAttr
     * @param doubleAttr
     * @param stringAttr
     */
    public FlatObject(boolean booleanTag, int integerTag, double doubleTag, String stringTag,
                        boolean booleanAttr, int integerAttr, double doubleAttr, String stringAttr) {
        mBooleanTag = booleanTag;
        mIntegerTag = integerTag;
        mDoubleTag = doubleTag;
        mStringTag = stringTag;
        mBooleanAttr = booleanAttr;
        mIntegerAttr = integerAttr;
        mDoubleAttr = doubleAttr;
        mStringAttr = stringAttr;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        FlatObject item = (FlatObject)obj;

        return (mBooleanAttr == item.mBooleanAttr &&
                mIntegerAttr == item.mIntegerAttr &&
                mDoubleAttr == item.mDoubleAttr &&
                (mStringAttr == item.mStringAttr || (mStringAttr != null &&  mStringAttr.equals(item.mStringAttr))) &&
                mBooleanTag == item.mBooleanTag &&
                mIntegerTag == item.mIntegerTag &&
                mDoubleTag == item.mDoubleTag &&
                (mStringTag == item.mStringTag || (mStringTag != null && mStringTag.equals(item.mStringTag))));
    }

    public final boolean getBooleanTag() {
        return mBooleanTag;
    }

    public final int getIntegerTag() {
        return mIntegerTag;
    }

    public final double getDoubleTag() {
        return mDoubleTag;
    }

    public final String getStringTag() {
        return mStringTag;
    }

    public final boolean getBooleanAttr() {
        return mBooleanAttr;
    }

    public final int getIntegerAttr() {
        return mIntegerAttr;
    }

    public final double getDoubleAttr() {
        return mDoubleAttr;
    }

    public final String getStringAttr() {
        return mStringAttr;
    }
}


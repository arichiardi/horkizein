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

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.XmlWriter;
import com.googlecode.horkizein.obj.FlatObject;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Implementation of an object without any XmlPushable child. It contains four attributes and four tags.
 */
@XmlTag(
    value = "flat_obj",
    additionalTags = { FlatObjectDAO.BOOLEAN_TAG, FlatObjectDAO.STRING_TAG, 
                       FlatObjectDAO.INTEGER_TAG, FlatObjectDAO.DOUBLE_TAG }
)
public class FlatObjectDAO implements XmlPushable<FlatObject>, XmlWriter {
    // This object tag
    public final static String TAG = "flat_obj";
    
    // Dependency
    private final XmlSerializer mSerializer;
    
    // Constants
    public final static String XML_TRUE = "true";
    public final static String XML_FALSE = "false";
    // Tags
    public final static String BOOLEAN_TAG = "boolean";
    public final static String INTEGER_TAG = "integer";
    public final static String DOUBLE_TAG = "double";
    public final static String STRING_TAG = "string";
    // Attrs
    public final static String BOOLEAN_ATTR = "boolean";
    public final static String INTEGER_ATTR = "integer";
    public final static String DOUBLE_ATTR = "double";
    public final static String STRING_ATTR = "string";

    // watch dog
    private boolean wdPushedStartTag;
    private boolean wdPushedEndTag;
    private boolean wdBooleanStartTag;
    private boolean wdBooleanEndTag;
    private boolean wdIntegerStartTag;
    private boolean wdIntegerEndTag;
    private boolean wdDoubleStartTag;
    private boolean wdDoubleEndTag;
    private boolean wdStringStartTag;
    private boolean wdStringEndTag;

    public boolean mBooleanTag;
    public int mIntegerTag;
    public double mDoubleTag;
    public String mStringTag;

    public boolean mBooleanAttr;
    public int mIntegerAttr;
    public double mDoubleAttr;
    public String mStringAttr;
   

    
    public FlatObjectDAO(XmlSerializer serializer) {
        mSerializer = serializer;
    }

    @Override
    public void attribute(String tag, String prefix, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".attribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);

        if (tag.equals(TAG)) {
            if (name.equals(BOOLEAN_ATTR)) {
                if (value.equals(XML_TRUE))
                    mBooleanAttr = true;
                else
                    mBooleanAttr = false;
            } else if (name.equals(INTEGER_ATTR)) {
                mIntegerAttr = Integer.valueOf(value);
            } else if (name.equals(DOUBLE_ATTR)) {
                mDoubleAttr = Double.valueOf(value);
            } else if (name.equals(STRING_ATTR)) {
                mStringAttr = new String(value);
            }
        }
    }


    @Override
    public void text(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);

        if (wdPushedStartTag) {
            if (tag.equals(BOOLEAN_TAG) && wdBooleanStartTag == true) {
                if (text.equals(XML_TRUE))
                    mBooleanTag = true;
                else
                    mBooleanTag = false;
            } else if (tag.equals(INTEGER_TAG) && wdIntegerStartTag == true) {
                mIntegerTag = Integer.valueOf(text);
            } else if (tag.equals(DOUBLE_TAG) && wdDoubleStartTag == true) {
                mDoubleTag = Double.valueOf(text);
            } else if (tag.equals(STRING_TAG) && wdStringStartTag == true) {
                mStringTag = new String(text);
            }
        }
    }


    @Override
    public void endTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");

        if (tag.equals(BOOLEAN_TAG) && wdBooleanStartTag == true) {
            wdBooleanEndTag = true;
        } else if (tag.equals(INTEGER_TAG) && wdIntegerStartTag == true) {
            wdIntegerEndTag = true;
        } else if (tag.equals(DOUBLE_TAG) && wdDoubleStartTag == true) {
            wdDoubleEndTag = true;
        } else if (tag.equals(STRING_TAG) && wdStringStartTag == true) {
            wdStringEndTag = true;
        }

        if (tag.equals(TAG) && wdPushedStartTag && wdBooleanEndTag
                && wdIntegerEndTag && wdDoubleEndTag && wdStringEndTag)
            wdPushedEndTag = true;
    }


    @Override
    public void startTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".startTag(" + tag + ")");

        if (wdPushedStartTag) {
            if (tag.equals(BOOLEAN_TAG)) {
                wdBooleanStartTag = true;
            }
            if (tag.equals(INTEGER_TAG)) {
                wdIntegerStartTag = true;
            }
            if (tag.equals(DOUBLE_TAG)) {
                wdDoubleStartTag = true;
            }
            if (tag.equals(STRING_TAG)) {
                wdStringStartTag = true;
            }
        }

        if (tag.equals(TAG)) {
            wdPushedStartTag = true;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG + " mBooleanAttr: " + mBooleanAttr + "\n");
        sb.append(TAG + " mIntegerAttr: " + mIntegerAttr + "\n");
        sb.append(TAG + " mDoubleAttr: " + mDoubleAttr + "\n");
        sb.append(TAG + " mStringAttr: " + mStringAttr + "\n");
        sb.append(TAG + " mIntegerTag: " + mIntegerTag + "\n");
        sb.append(TAG + " mDoubleTag: " + mDoubleTag + "\n");
        sb.append(TAG + " mStringTag: " + mStringTag + "\n");
        return sb.toString();
    }
    
    public boolean tagCheck() {
        return (wdPushedStartTag &&
                wdPushedEndTag &&
                wdBooleanStartTag &&
                wdBooleanEndTag &&
                wdIntegerStartTag &&
                wdIntegerEndTag &&
                wdDoubleStartTag &&
                wdDoubleEndTag &&
                wdStringStartTag &&
                wdStringEndTag);
    }

    @Override
    public FlatObject build() {
        return new FlatObject(mBooleanTag, mIntegerTag, mDoubleTag, mStringTag,
                               mBooleanAttr, mIntegerAttr, mDoubleAttr, mStringAttr);
    }

    @Override
    public XmlPushable<FlatObject> shallowClone() {
        return new FlatObjectDAO(mSerializer);
    }

    @Override
    public void write(XmlWritable object) throws IOException, IllegalStateException, IllegalArgumentException {
        object.writeXml(mSerializer);
    }
}


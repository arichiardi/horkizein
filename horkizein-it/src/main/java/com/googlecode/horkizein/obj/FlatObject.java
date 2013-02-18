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
import java.util.ArrayList;
import java.util.Collection;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

/**
 * Implementation of an object without any XmlPushable child. It contains four attributes and four tags.
 */
@XmlTag("flat_obj")
public class FlatObject implements XmlPushable, XmlWritable {
    // This object tag
    public final static String TAG = "flat_obj";
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

    public FlatObject() { /* do nothing */ }


    public void pushAttribute(String tag, String prefix, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushAttribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);

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


    public void pushText(String tag, String text) {
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


    public void pushEndTag(String tag) {
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


    public void pushStartTag(String tag) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag(" + tag + ")");

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
    public int hashCode() {
        int hash = 1;
        hash = hash * 47 + (mBooleanTag == false ? 0 : 1);
        hash = hash * 37 + mIntegerTag;
        hash = hash * 31 + (int)mDoubleTag;
        hash = hash * 17 + (mStringTag == null ? 0 : mStringTag.hashCode());
        hash = hash * 47 + (mBooleanAttr == false ? 0 : 1);
        hash = hash * 37 + mIntegerAttr;
        hash = hash * 31 + (int)mDoubleAttr;
        hash = hash * 17 + (mStringAttr == null ? 0 : mStringAttr.hashCode());
        return hash;
    };

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG + " mBooleanAttr: " + mBooleanAttr);
        sb.append(TAG + " mIntegerAttr: " + mIntegerAttr);
        sb.append(TAG + " mDoubleAttr: " + mDoubleAttr);
        sb.append(TAG + " mStringAttr: " + mStringAttr);
        sb.append(TAG + " mIntegerTag: " + mIntegerTag);
        sb.append(TAG + " mDoubleTag: " + mDoubleTag);
        sb.append(TAG + " mStringTag: " + mStringTag);
        return sb.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        FlatObject item = (FlatObject)obj;

        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mBooleanAttr: " + mBooleanAttr + " - item.mBooleanAttr: "  + item.mBooleanAttr);
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mIntegerAttr: " + mIntegerAttr + " - item.mIntegerAttr: "  + item.mIntegerAttr);
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mDoubleAttr: " + mDoubleAttr + " - item.mDoubleAttr: "  + item.mDoubleAttr);
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mStringAttr: " + mStringAttr + " - item.mStringAttr: "  + item.mStringAttr);

        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mBooleanTag: " + mBooleanTag + " - item.mBooleanTag: "  + item.mBooleanTag);
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mIntegerTag: " + mIntegerTag + " - item.mIntegerTag: "  + item.mIntegerTag);
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mDoubleTag: " + mDoubleTag + " - item.mDoubleTag: "  + item.mDoubleTag);
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + " mStringTag: " + mStringTag + " - item.mStringTag: "  + item.mStringTag);

        return (mBooleanAttr == item.mBooleanAttr &&
                mIntegerAttr == item.mIntegerAttr &&
                mDoubleAttr == item.mDoubleAttr &&
                mStringAttr.equals(item.mStringAttr) &&
                mBooleanTag == item.mBooleanTag &&
                mIntegerTag == item.mIntegerTag &&
                mDoubleTag == item.mDoubleTag &&
                mStringTag.equals(item.mStringTag));
    }


    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {

        out.startTag("", TAG);

        if (mBooleanAttr) {
            out.attribute("", BOOLEAN_ATTR, XML_TRUE);
        } else {
            out.attribute("", BOOLEAN_ATTR, XML_FALSE);
        }
        out.attribute("", INTEGER_ATTR, String.valueOf(mIntegerAttr));
        out.attribute("", DOUBLE_ATTR, String.valueOf(mDoubleAttr));
        out.attribute("", STRING_ATTR, mStringAttr);

        out.startTag("", BOOLEAN_TAG);
        if (mBooleanTag) {
            out.text(XML_TRUE);
        } else {
            out.text(XML_FALSE);
        }
        out.endTag("", BOOLEAN_TAG);

        out.startTag("", INTEGER_TAG);
        out.text(String.valueOf(mIntegerTag));
        out.endTag("", INTEGER_TAG);

        out.startTag("", DOUBLE_TAG);
        out.text(String.valueOf(mDoubleTag));
        out.endTag("", DOUBLE_TAG);

        out.startTag("", STRING_TAG);
        out.text(mStringTag);
        out.endTag("", STRING_TAG);

        out.endTag("", TAG);
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

    /**

     */
    public Collection<String> pushableTags() {
        ArrayList<String> tags = new ArrayList<String>(5);
        tags.add(TAG);
        tags.add(BOOLEAN_TAG);
        tags.add(INTEGER_TAG);
        tags.add(DOUBLE_TAG);
        tags.add(STRING_TAG);
        return tags;
    }

    /**

     */
    public String getTag() {
        return TAG;
    }
}


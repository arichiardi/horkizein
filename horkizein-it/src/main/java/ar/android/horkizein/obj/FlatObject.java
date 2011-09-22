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

public class FlatObject implements XmlPushable, XmlWritable {
	// This object tag
	public final static String TAG = "flat_obj";
	// Constants
	public final static String XML_TRUE = "true";
	public final static String XML_FALSE = "false";
	// Tags
	public final static String BOOLEAN_TAG = "boolean_tag";
	public final static String INTEGER_TAG = "integer_tag";
	public final static String DOUBLE_TAG = "double_tag";
	public final static String STRING_TAG = "string_tag";
	// Attrs
	public final static String BOOLEAN_ATTR = "boolean_attr";
	public final static String INTEGER_ATTR = "integer_attr";
	public final static String DOUBLE_ATTR = "double_attr";
	public final static String STRING_ATTR = "string_attr";

	// watch dog
	private boolean mIsMine;

	public boolean mBooleanTag;
	public int mIntegerTag;
	public double mDoubleTag;
	public String mStringTag;

	public boolean mBooleanAttr;
	public int mIntegerAttr;
	public double mDoubleAttr;
	public String mStringAttr;

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

	/**
	 * @see ar.android.horkizein.xml.XmlPushable#pushText(java.lang.String, java.lang.String)
	 */
	public void pushText(String tag, String text) {
		Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);
		if (mIsMine) {
			if (tag.equals(BOOLEAN_TAG)) {
				if (text.equals(XML_TRUE))
					mBooleanTag = true;
				else
					mBooleanTag = false;
			} else if (tag.equals(INTEGER_TAG)) {
				mIntegerTag = Integer.valueOf(text);
			} else if (tag.equals(DOUBLE_TAG)) {
				mDoubleTag = Double.valueOf(text);
			} else if (tag.equals(STRING_TAG)) {
				mStringTag = new String(text);
			}
		}
	}

	/**
	 * @see ar.android.horkizein.xml.XmlPushable#pushEndTag(java.lang.String)
	 */
	public void pushEndTag(String tag) {
		if (tag.equals(TAG))
			mIsMine = false;
	}

	/**
	 * @see ar.android.horkizein.xml.XmlPushable#pushStartTag(java.lang.String)
	 */
	public void pushStartTag(String tag) {
		if (tag.equals(TAG))
			mIsMine = true;
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

    /**
     * @see ar.android.horkizein.xml.XmlWritable#writeXml(org.xmlpull.v1.XmlSerializer)
     */
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {

    	out.startTag("", TAG);

    	if (mBooleanAttr)
			out.attribute("", BOOLEAN_ATTR, XML_TRUE);
		else
			out.attribute("", BOOLEAN_ATTR, XML_FALSE);

		out.attribute("", INTEGER_ATTR, String.valueOf(mIntegerAttr));
		out.attribute("", DOUBLE_ATTR, String.valueOf(mDoubleAttr));
		out.attribute("", STRING_ATTR, mStringAttr);

		out.startTag("", BOOLEAN_TAG);
		if (mBooleanTag)
			out.text(XML_TRUE);
		else
			out.text(XML_FALSE);
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
}


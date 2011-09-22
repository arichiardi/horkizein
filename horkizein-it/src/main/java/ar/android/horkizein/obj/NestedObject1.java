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

public class NestedObject1 implements XmlPushable, XmlWritable {

    // This object tag
	public final static String TAG = "nested_obj1";

	// watch dog
	private boolean mIsMine;
	private boolean mIsFlatObject;

	// child
	public FlatObject mFlatObject;
	// creator
	private FlatObjectCreator mFlatCreator;

	/**
	 * Creates a NestedObject1 that contains a shallow copy of a FlatObject
	 * @param mMyChild
	 */
	public NestedObject1(FlatObjectCreator flatCreator) {
		mFlatCreator = flatCreator;
	}
	
	/**
	 * Creates an empty NetstedObject1
	 */
	public NestedObject1() {}

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

		if (mIsMine) {
            if (mIsFlatObject) {
                mFlatObject.pushAttribute(tag, name, value);
            }
        }
	}

	/**
	 * @see ar.android.horkizein.xml.XmlPushable#pushText(java.lang.String, java.lang.String)
	 */
	public void pushText(String tag, String text) {
		Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);

		if (mIsMine) {
		    if (mIsFlatObject) {
		        mFlatObject.pushText(tag, text);
		    }
		}
	}

	/**
	 * @see ar.android.horkizein.xml.XmlPushable#pushEndTag(java.lang.String)
	 */
	public void pushEndTag(String tag) {
		if (tag.equals(TAG))
			mIsMine = false;

		if(mIsMine) {
            if (tag.equals(FlatObject.TAG)) {
                mIsFlatObject = false;
            }
        }
	}

	/**
	 * @see ar.android.horkizein.xml.XmlPushable#pushStartTag(java.lang.String)
	 */
	public void pushStartTag(String tag) {
		if (tag.equals(TAG))
			mIsMine = true;

		if(mIsMine) {
		    if (tag.equals(FlatObject.TAG)) {
		        
		        if (mFlatCreator != null)
		        	mFlatObject = mFlatCreator.create();
		        
		        if (mFlatObject != null) {
		        	mFlatObject.pushStartTag(tag);
		        	mIsFlatObject = true;
	    		}
		    }
		}
		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if((obj == null) || (obj.getClass() != this.getClass())) return false;
		return mFlatObject.equals(((NestedObject1)obj).mIsFlatObject);
	}

    /**
     * @see ar.android.horkizein.xml.XmlWritable#writeXml(org.xmlpull.v1.XmlSerializer)
     */
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {

    	out.startTag("", TAG);

    	if(mFlatObject != null)
    	    mFlatObject.writeXml(out);

		out.endTag("", TAG);
    }
}


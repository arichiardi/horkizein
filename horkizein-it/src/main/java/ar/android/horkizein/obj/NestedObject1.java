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
import java.util.ArrayList;
import java.util.Collection;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import ar.android.horkizein.XmlPushable;
import ar.android.horkizein.XmlWritable;
import ar.android.horkizein.test.Constants;

/**
 * Implementation of an XmlPushable which contains a FlatObject.
 */
public class NestedObject1 implements XmlPushable, XmlWritable {

    // This object tag
    public final static String TAG = "nested_obj1";

    // watch dog
    private boolean wdPushedStartTag;
    private boolean wdPushedEndTag;
    //private boolean wdFlatObjStartTag;
    //private boolean wdFlatObjEndTag;

    // child
    public FlatObject mFlatObject;
    // creator
    private FlatObjectCreator mFlatCreator;

    /**
     * Creates a NestedObject1 that contains a shallow copy of a FlatObject
     * @param mMyChild
     */
    public NestedObject1(FlatObject flatSrc) {
    	mFlatCreator = new FlatObjectCreator();
    	mFlatObject = flatSrc; // test purpose
        wdPushedEndTag = wdPushedStartTag = false;
    }

    /**
     * Creates an empty NetstedObject1
     */
    public NestedObject1() {
    	mFlatCreator = new FlatObjectCreator();
    	wdPushedEndTag = wdPushedStartTag = false;
    }

    /**
     * @see ar.android.horkizein.XmlPushable#pushAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void pushAttribute(String tag, String prefix, String name, String value) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushAttribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);
        if (wdPushedStartTag) {
            //if (wdFlatObjStartTag) {
        	mFlatObject.pushAttribute(tag, prefix, name, value);
            //}
        }
    }

    /**
     * @see ar.android.horkizein.XmlPushable#pushText(java.lang.String, java.lang.String)
     */
    public void pushText(String tag, String text) {
        Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);
        if (wdPushedStartTag) {
            //if (wdFlatObjStartTag) {
        	mFlatObject.pushText(tag, text);
            //}
        }
    }

    /**
     * @see ar.android.horkizein.XmlPushable#pushEndTag(java.lang.String)
     */
    public void pushEndTag(String tag) {
    	Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag() - TAG: " + tag);
    	
    	if(wdPushedStartTag) {
    		if (mFlatObject != null) {
    			mFlatObject.pushEndTag(tag);
    		}
    	}
    	
    	if (tag.equals(TAG) && wdPushedStartTag == true)
    		wdPushedEndTag = true;
    }

    /**
     * @see ar.android.horkizein.XmlPushable#pushStartTag(java.lang.String)
     */
    public void pushStartTag(String tag) {
    	Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag() - TAG: " + tag);
        if (tag.equals(TAG))
        	wdPushedStartTag = true;

        if(wdPushedStartTag) {
        	//if (wdFlatObjStartTag == true) {
        	if (tag.equals(FlatObject.TAG)) {
            //	wdFlatObjStartTag = true;
                if (mFlatCreator != null) {
                	Log.d(Constants.PACKAGE_TAG_TEST, TAG + ": flatObj created");
                    mFlatObject = mFlatCreator.create();
                }
            }
        	
        	if (mFlatObject != null) {
        		mFlatObject.pushStartTag(tag);
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

        NestedObject1 o = (NestedObject1)obj;
        return (mFlatObject == o.mFlatObject || (mFlatObject != null && mFlatObject.equals(o.mFlatObject)));
    }

    /**
     * @see ar.android.horkizein.XmlWritable#writeXml(org.xmlpull.v1.XmlSerializer)
     */
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
    	// Writing out
        out.startTag("", TAG);
        if(mFlatObject != null) {
            mFlatObject.writeXml(out);
        }
        out.endTag("", TAG);
    }
    
    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
    	return (mFlatObject.tagCheck() &&
    			wdPushedStartTag &&
    			wdPushedEndTag);
    }

    /**
	 * @see ar.android.horkizein.XmlPushable#pushableTags()
	 */
	public Collection<String> pushableTags() {
		ArrayList<String> tags = new ArrayList<String>();
    	tags.add(TAG);
    	tags.add(FlatObject.TAG);
        return tags;
	}
	
	/**
	 * @see ar.android.horkizein.Taggable#getTag()
	 */
	public String getTag() {
		return TAG;
	}
}


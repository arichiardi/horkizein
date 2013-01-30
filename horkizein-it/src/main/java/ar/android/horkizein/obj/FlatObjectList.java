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
import java.util.List;

import org.xmlpull.v1.XmlSerializer;
import android.util.Log;
import ar.android.horkizein.XmlPushable;
import ar.android.horkizein.XmlPushableList;
import ar.android.horkizein.test.Constants;

public class FlatObjectList extends XmlPushableList<FlatObject> {

	final static String TAG = "flat_obj_list";
	final static String ITEM_TAG = FlatObject.TAG;

	//private int mListIndex;
	//private FlatObject mCurrentItem;
	
	// watch dog
    private boolean wdPushedListStartTag;
    private boolean wdPushedListEndTag;
    private int wdPushedItemStartTagCount;
    private int wdPushedItemEndTagCount;
    
    /**
     * Ctor with input list.
     * @param list
     */
	public FlatObjectList(List<FlatObject> list) {
		super(list, new FlatObjectCreator());
		//mCurrentItem = null;
	}
	
	/**
	 * Plain Ctor. The internal list is empty.
	 */
	public FlatObjectList() {
		super(new ArrayList<FlatObject>(), new FlatObjectCreator());
		//mCurrentItem = null;
	}

	 /**
     * @see ar.android.horkizein.XmlPushable#pushAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
	/*public void pushAttribute(String tag, String prefix, String name, String value) {
		Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushAttribute() - TAG: " + tag + " NAME: " + name +  " TEXT: " + value);
		
		if (mCurrentItem != null) {
			mCurrentItem.pushAttribute(tag, prefix, name, value);
		} else {
			Log.d(Constants.PACKAGE_TAG_TEST, "FlatObjectList.pushAttribute(): currentItem = null");
		}
	}*/

	/**
     * @see ar.android.horkizein.XmlPushable#pushText(java.lang.String, java.lang.String)
     */
	/*public void pushText(String tag, String text) {
		Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushText() - TAG: " + tag + " TEXT: " + text);
		
		if (mCurrentItem != null) {
			mCurrentItem.pushText(tag, text);
		} else {
			Log.d(Constants.PACKAGE_TAG_TEST, "FlatObjectList.pushText(): currentItem = null");
		}
	}*/

	/**
     * @see ar.android.horkizein.XmlPushable#pushStartTag(java.lang.String)
     */
	/*public void pushStartTag(String tag) {
		Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushStartTag(" + tag + ")");
		
		if (tag.equals(TAG)) {
			wdPushedListStartTag = true;
			wdPushedItemStartTagCount = 0;
			wdPushedItemEndTagCount = 0;
		}
		
		if (wdPushedListStartTag == true) {
			if (tag.equals(ITEM_TAG)) {
				wdPushedItemStartTagCount++;
				// build the item through the internal list factory
				mCurrentItem = mFactory.create();

				if (mCurrentItem != null) {
					Log.d(Constants.PACKAGE_TAG_TEST, "FlatObjectList.pushStart(): currentItem is not null");
					mCurrentItem.pushStartTag(tag);
				} else {
					Log.d(Constants.PACKAGE_TAG_TEST, "FlatObjectList.pushText(): currentItem = null");
				}
			}
		}
	}*/

	/**
     * @see ar.android.horkizein.XmlPushable#pushEndTag(java.lang.String)
     */
	/*public void pushEndTag(String tag) {
		Log.d(Constants.PACKAGE_TAG_TEST, TAG + ".pushEndTag(" + tag + ")");
		
		if (wdPushedListStartTag == true) {
			if (tag.equals(ITEM_TAG)) {
				wdPushedItemEndTagCount++;

				if (mCurrentItem != null) {
					try {
						mCurrentItem.pushEndTag(tag);
						add(mCurrentItem);
						//++mListIndex;
					} catch (UnsupportedOperationException e) {
						Log.d(Constants.PACKAGE_TAG_TEST, "FlatObjectList.pushEnd(): " +  e.getMessage());
					} catch (ClassCastException e) {
						Log.d(Constants.PACKAGE_TAG_TEST, "FlatObjectList.pushEnd(): " +  e.getMessage());
					} catch (IllegalArgumentException e) {
						Log.d(Constants.PACKAGE_TAG_TEST, "FlatObjectList.pushEnd(): " +  e.getMessage());
					}
					mCurrentItem = null;

				} else {
					Log.d(Constants.PACKAGE_TAG_TEST, "FlatObjectList.pushText(): currentItem = null");
				}
			}
		} else if (tag.equals(TAG)) {
			wdPushedListEndTag = true;
		}
	}*/

	/**
     * @see ar.android.horkizein.XmlWritable#writeXml(org.xmlpull.v1.XmlSerializer)
     */
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
    	out.startTag("", TAG);
		for (FlatObject ai : this) {
			ai.writeXml(out);
		}
		out.endTag("", TAG);
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
    	
        Log.d(Constants.PACKAGE_TAG_TEST, "this.size() is: " + this.size());
        Log.d(Constants.PACKAGE_TAG_TEST, "----------------");
        for (XmlPushable o : this) {
        	Log.d(Constants.PACKAGE_TAG_TEST, o.toString());
        }
        Log.d(Constants.PACKAGE_TAG_TEST, "o.size() is: " + ((FlatObjectList)obj).size());
        Log.d(Constants.PACKAGE_TAG_TEST, "+++++++++++++");
        for (XmlPushable o : (FlatObjectList)obj) {
        	Log.d(Constants.PACKAGE_TAG_TEST, o.toString());
        }
        return super.equals(obj);
    }
    
    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
    	boolean listCheck = true;
    	
    	for (FlatObject ai : this) {
			listCheck &= ai.tagCheck();
		}
    	
    	return (listCheck);
    }
}

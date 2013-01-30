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
import java.util.List;

import org.xmlpull.v1.XmlSerializer;
import android.util.Log;
import ar.android.horkizein.XmlPushable;
import ar.android.horkizein.XmlPushableCreator;
import ar.android.horkizein.XmlWritable;
import ar.android.horkizein.test.Constants;

public class ObjectWithList implements XmlPushable, XmlWritable {

	final static String TAG = "obj_with_list";
	final static String ITEM_TAG = FlatObject.TAG;

	//private int mListIndex;
	private FlatObject mCurrentItem;
	private List<FlatObject> mList;
	protected XmlPushableCreator<FlatObject> mFactory;
	
	// watch dog
    private boolean wdPushedListStartTag;
    private boolean wdPushedListEndTag;
    private int wdPushedItemStartTagCount;
    private int wdPushedItemEndTagCount;
    
    /**
     * Ctor with input list.
     * @param list
     */
	public ObjectWithList(List<FlatObject> list) {
		mFactory = new FlatObjectCreator();
		mList = list;
		mCurrentItem = null;
	}
	
	/**
	 * Plain Ctor. The internal list is empty.
	 */
	public ObjectWithList() {
		mFactory = new FlatObjectCreator();
		mList = new ArrayList<FlatObject>();
		mCurrentItem = null;
	}

	 /**
     * @see ar.android.horkizein.XmlPushable#pushAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
	public void pushAttribute(String tag, String prefix, String name, String value) {
		if (mCurrentItem != null) {
			mCurrentItem.pushAttribute(tag, prefix, name, value);
		} else {
			Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushAttribute(): currentItem = null");
		}
	}

	/**
     * @see ar.android.horkizein.XmlPushable#pushText(java.lang.String, java.lang.String)
     */
	public void pushText(String tag, String text) {
		if (mCurrentItem != null) {
			mCurrentItem.pushText(tag, text);
		} else {
			Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushText(): currentItem = null");
		}
	}

	/**
     * @see ar.android.horkizein.XmlPushable#pushStartTag(java.lang.String)
     */
	public void pushStartTag(String tag) {

		if (tag.equals(TAG)) {
			wdPushedListStartTag = true;
			wdPushedItemStartTagCount = 0;
			wdPushedItemEndTagCount = 0;
		}
		
		if (wdPushedListStartTag == true) {
			if (tag.equals(ITEM_TAG)) {
				wdPushedItemStartTagCount++;
				// build the item through the list internal factory
				mCurrentItem = mFactory.create();

				if (mCurrentItem != null) {
					Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushStart(): currentItem is not null");
					mCurrentItem.pushStartTag(tag);
				} else {
					Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushText(): currentItem = null");
				}
			}
		}
			
	}

	/**
     * @see ar.android.horkizein.XmlPushable#pushEndTag(java.lang.String)
     */
	public void pushEndTag(String tag) {
		
		if (wdPushedListStartTag == true) {
			if (tag.equals(ITEM_TAG)) {
				wdPushedItemEndTagCount++;

				if (mCurrentItem != null) {
					try {
						mCurrentItem.pushEndTag(tag);
						mList.add(mCurrentItem);
						//++mListIndex;
					} catch (UnsupportedOperationException e) {
						Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushEnd(): " +  e.getMessage());
					} catch (ClassCastException e) {
						Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushEnd(): " +  e.getMessage());
					} catch (IllegalArgumentException e) {
						Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushEnd(): " +  e.getMessage());
					}
					mCurrentItem = null;

				} else {
					Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushText(): currentItem = null");
				}
			}
		} else if (tag.equals(TAG)) {
			wdPushedListEndTag = false;
		}
	}

	/**
     * @see ar.android.horkizein.XmlWritable#writeXml(org.xmlpull.v1.XmlSerializer)
     */
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
    	out.startTag("", TAG);
		for (FlatObject ai : mList) {
			ai.writeXml(out);
		}
		out.endTag("", TAG);
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        ObjectWithList o = (ObjectWithList)obj;
        return (mList != null && mList.equals(o.mList));
    }
    
    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
    	boolean listCheck = true;
    	
    	for (FlatObject ai : mList) {
			listCheck &= ai.tagCheck();
		}
    	
    	return (listCheck &&
    			wdPushedListStartTag &&
    			wdPushedListEndTag &&
    			wdPushedItemEndTagCount == mList.size() &&
    			wdPushedItemStartTagCount == mList.size());
    }

    /**
	 * @see ar.android.horkizein.XmlPushable#pushableTags()
	 */
	public Collection<String> pushableTags() {
		ArrayList<String> tags = new ArrayList<String>(2);
    	tags.add(TAG);
    	tags.add(ITEM_TAG);
        return tags;
	}
	
	/**
	 * @see ar.android.horkizein.Taggable#getTag()
	 */
	public String getTag() {
		return TAG;
	}
}

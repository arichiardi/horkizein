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
/*package com.googlecode.horkizein.obj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.obj.builders.FlatObjectBuilder;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

@XmlTag(
    value = "obj_with_list",
    enclosedPushables = FlatObject.class
)
public class ObjectWithList implements XmlPushable, XmlWritable {

    private final static String TAG = "obj_with_list";
    private final static String ITEM_TAG = FlatObject.TAG;

    //private int mListIndex;
    private FlatObject mCurrentItem;
    
    private XmlBuilder<FlatObject> mFactory;
    private List<FlatObject> mList;
    
    // watch dog
    private boolean wdPushedListItemStartTag;
    private boolean wdPushedListItemEndTag;
    private int wdPushedItemStartTagCount;
    private int wdPushedItemEndTagCount;

    *//**
     * Ctor with input list.
     * @param list
     *//*
    public ObjectWithList(List<FlatObject> list) {
        mFactory = new FlatObjectBuilder();
        mList = list;
        mCurrentItem = null;
    }

    *//**
     * Plain Ctor. The internal list is empty.
     *//*
    public ObjectWithList() {
        mFactory = new FlatObjectBuilder();
        mList = new ArrayList<FlatObject>();
        mCurrentItem = null;
    }

    @Override
    public void attribute(String tag, String prefix, String name, String value) {
        if (wdPushedListItemStartTag) {
            if (mCurrentItem != null) {
                mCurrentItem.attribute(tag, prefix, name, value);
                Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.attribute(): currentItem is not null");
            } else {
                Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.attribute(): currentItem = null");
            }
        }
    }

    @Override
    public void text(String tag, String text) {
        if (wdPushedListItemStartTag) {
            if (mCurrentItem != null) {
                mCurrentItem.pushText(tag, text);
                Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushText(): currentItem is not null");
            } else {
                Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushText(): currentItem is null");
            }
        }
    }

    @Override
    public void startTag(String tag) {

        if (tag.equals(TAG)) {
            wdPushedItemStartTagCount = 0;
            wdPushedItemEndTagCount = 0;
        }
        if (tag.equals(ITEM_TAG)) {
            wdPushedListItemStartTag = true;
            wdPushedItemStartTagCount++;
            // build the item through the factory
            mCurrentItem = mFactory.getInstance();
        }

        if (wdPushedListItemStartTag == true) {
            if (mCurrentItem != null) {
                Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.startTag(): currentItem is not null");
                mCurrentItem.startTag(tag);
            } else {
                Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.startTag(): currentItem is null");
            }
        }
    }

    @Override
    public void endTag(String tag) {

        if (wdPushedListItemStartTag == true) {
            if (mCurrentItem != null) {
                mCurrentItem.pushEndTag(tag);
                Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushEndTag(): currentItem is null");
            } else {
                Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.pushEndTag(): currentItem is null");
            }

            if (tag.equals(ITEM_TAG)) {
                wdPushedItemEndTagCount++;
                wdPushedListItemEndTag = true;
                wdPushedListItemStartTag = false;
                if (mCurrentItem != null) {
                    mList.add(mCurrentItem);
                    mCurrentItem = null;
                }
            }
        }

        if (tag.equals(TAG)) {
            wdPushedListItemStartTag = false;
            wdPushedListItemEndTag =true;
        }
    }

    *//**

     *//*
    @Override
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
        out.startTag("", TAG);
        if (mList != null) {
            for (FlatObject ai : mList) {
                ai.writeXml(out);
            }
        }
        out.endTag("", TAG);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;

        ObjectWithList o = (ObjectWithList)obj;
        if (mList != null && o.mList != null) {
            Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.equals(): this.mList.size() is " + this.mList.size());
            Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.equals(): o.mList.size() is " + o.mList.size());

            int i = 0;
            for (FlatObject fo : mList) {
                FlatObject objFo = o.mList.get(i);
                if (fo.equals(objFo)) {
                    Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.equals(): Objects #" + i + " are equals");
                } else {
                    Log.d(Constants.PACKAGE_TAG_TEST, "ObjectWithList.equals(): Objects #" + i + "are NOT equals");
                }
                i++;
            }
        }
        return (mList == o.mList || (mList != null && o.mList != null && mList.equals(o.mList)));
    }

    *//**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     *//*
    public boolean tagCheck() {
        boolean listCheck = true;

        for (FlatObject ai : mList) {
            listCheck &= ai.tagCheck();
        }

        return (listCheck &&
                wdPushedListItemStartTag == false &&
                wdPushedListItemEndTag == true &&
                wdPushedItemEndTagCount == mList.size() &&
                wdPushedItemStartTagCount == mList.size());
    }
}
*/
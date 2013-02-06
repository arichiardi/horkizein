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
package com.googlecode.horkizein;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlPushableCreator;

import android.util.Log;

/**
 * Abstract list of XmlPushable objects that in turn is a XmlPushable.
 * @param <E>
 */
public abstract class XmlPushableList<E extends XmlPushable> extends AbstractList<E> implements XmlPushable {

    private static String PACKAGE = "horkizein";
    private static String DEFAULT_ITEM_TAG = "item_tag";

    protected XmlPushableCreator<E> mFactory;
    protected List<E> mList;
    protected E mCurrentItem;

    /**
     * Ctor.
     * @param list Backed list.
     * @param factory Creator factory.
     */
    public XmlPushableList(List<E> list, XmlPushableCreator<E> factory) {
        mFactory = factory;
        mList = list;
    }

    /**
     * Ctor. The class internally uses an ArrayList if this constructor is called.
     * @param factory Creator factory.
     */
    public XmlPushableList(XmlPushableCreator<E> factory) {
        mFactory = factory;
        mList = new ArrayList<E>();
    }

    @Override
    public E get(int location) {
        return mList.get(location);
    }

    @Override
    public int size() {
        return mList.size();
    }

    public boolean add(E object) {
        return mList.add(object);
    }

    /**
     * Adds in a specific location
     */
    public void add(int location, E object) {
        mList.add(location, object);
    };

    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        return mList.addAll(location, collection);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return mList.addAll(collection);
    }

    @Override
    public void clear() {
        mList.clear();
    }

    public void pushStartTag(String tag) {
        if (tag.equals(getTag())) {
        }
        if (tag.equals(getItemTag())) {
            mCurrentItem = mFactory.create();
        }

        if (mCurrentItem != null) {
            Log.d(PACKAGE, "XmlPushableList.pushStartTag(): currentItem is not null");
            mCurrentItem.pushStartTag(tag);
        } else {
            Log.d(PACKAGE, "XmlPushableList.pushStartTag(): currentItem is null");
        }
    }

    public void pushAttribute(String tag, String prefix, String name, String value) {
        if (mCurrentItem != null) {
            mCurrentItem.pushAttribute(tag, prefix, name, value);
            Log.d(PACKAGE, "XmlPushableList.pushAttribute(): currentItem is not null");
        } else {
            Log.d(PACKAGE, "XmlPushableList.pushAttribute(): currentItem = null");
        }
    }

    /**
     * Implement this if you want to customize the name of the list item's tag .
     * @return The list item's tag.
     */
    protected String getItemTag() {
        return DEFAULT_ITEM_TAG;
    }

    public void pushText(String tag, String text) {
        if (mCurrentItem != null) {
            mCurrentItem.pushText(tag, text);
            Log.d(PACKAGE, "XmlPushableList.pushText(): currentItem is not null");
        } else {
            Log.d(PACKAGE, "XmlPushableList.pushText(): currentItem is null");
        }
    }

    public void pushEndTag(String tag) {
        if (mCurrentItem != null) {
            mCurrentItem.pushEndTag(tag);
            Log.d(PACKAGE, "XmlPushableList.pushEndTag(): currentItem is null");
        } else {
            Log.d(PACKAGE, "XmlPushableList.pushEndTag(): currentItem is null");
        }

        if (tag.equals(getItemTag())) {
            if (mCurrentItem != null) {
                add(mCurrentItem);
                mCurrentItem = null;
            }
        }
    }

    @Override
    public Collection<String> pushableTags() {
        Collection<String> c = new ArrayList<String>();
        c.add(getTag());
        c.add(getItemTag());
        return c;
    }
}

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
package com.googlecode.horkizein;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.horkizein.XmlPushable;

/**
 * This class is intended to ease the handling of lists that need to be automatically
 * filled by the Horkizein Library. The class can be used "as-is" or customized overriding the methods.<br/>
 * Just {@link XmlPushableArrayList#get} and {@link XmlPushableArrayList#size} have been overridden here so that
 * the list can be changed only by XmlFiller. Therefore, outside the Horkizein world this class is immutable.<br/>
 * <i>IMPORTANT</i>: Do not forget to add the {@link XmlTag} annotation in your sub-class.
 * @param <E> The XmlPushable object handled by this list.
 */
public abstract class XmlPushableArrayList<E extends XmlPushable> extends AbstractList<E> implements XmlPushable {

    protected final List<E> mList;
    protected XmlBuilder<E> mItemBuilder;
    protected E mCurrentItem;
    // watch dogs
    private boolean wdPushedItemStartTag;

    /**
     * Constructor. The class internally creates an ArrayList and copies the input list's content.
     * @param list An input list, to copy data from.
     * @param itemBuilder Item builder.
     */
    public XmlPushableArrayList(List<E> list, XmlBuilder<E> itemBuilder) {
        mItemBuilder = itemBuilder;
        mList = new ArrayList<E>(list);
    }

    /**
     * Constructor. The class internally uses an empty ArrayList.
     * @param itemBuilder Item builder.
     */
    public XmlPushableArrayList(XmlBuilder<E> itemBuilder) {
        mItemBuilder = itemBuilder;
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
    
    /**
     * Override this if you want to customize the name of the tag of the list
     * <i>IMPORTANT</i>: do not forget to add annotation accordingly.
     * @return The tag of the list.
     */
    protected abstract String getTag();
    
    /**
     * Override this if you want to customize the name of the tag of the list item
      * <i>IMPORTANT</i>: do not forget to add annotation accordingly.
     * @return The tag of the list item.
     */
    protected abstract String getItemTag();
    

    @Override
    public void pushStartTag(String tag) {
        if (tag.equals(getTag())) {
            wdPushedItemStartTag = true;
        }

        if (wdPushedItemStartTag == true) {
            if (tag.equals(getItemTag())) {
                mCurrentItem = mItemBuilder.getInstance();
            }

            if (mCurrentItem != null) {
                mCurrentItem.pushStartTag(tag);
            }
        }
    }

    @Override
    public void pushAttribute(String tag, String prefix, String name, String value) {
        if (wdPushedItemStartTag == true && mCurrentItem != null) {
            mCurrentItem.pushAttribute(tag, prefix, name, value);
        }
    }
    
    @Override
    public void pushText(String tag, String text) {
        if (wdPushedItemStartTag == true && mCurrentItem != null) {
            mCurrentItem.pushText(tag, text);
        }
    }

    @Override
    public void pushEndTag(String tag) {
        
        if (wdPushedItemStartTag == true) {
            if (mCurrentItem != null) {
                mCurrentItem.pushEndTag(tag);
            }            
            if (tag.equals(getItemTag())) {
                if (mCurrentItem != null) {
                    mList.add(mCurrentItem);
                    mCurrentItem = null;
                }
            }
        }

        if (tag.equals(getTag())) {
            wdPushedItemStartTag = false;
        }
    }
}

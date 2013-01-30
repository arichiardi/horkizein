/*
** Copyright 2011, Horkizein Open Source Android Libraryensed under the Apache License, Version 2.0 (the "License");
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
package ar.android.horkizein;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ar.android.horkizein.XmlPushable;
import ar.android.horkizein.XmlPushableCreator;

/**
 * Abstract list of XmlPushable objects that in turn is a XmlPushable.
 * @param <E>
 */
public abstract class XmlPushableList<E extends XmlPushable> extends AbstractList<E> implements XmlWritable {
    
	protected XmlPushableCreator<E> mFactory;
	private List<E> mList;
	
	/**
	 * Ctor.
	 * @param list Backed list.
	 * @param factory Creator factory.
	 */
	public XmlPushableList(List<E> list, XmlPushableCreator<E> factory) {
		mFactory = factory;
		mList = new ArrayList<E>(list);
	}
	
	/**
	 * Ctor.
	 * @param factory Creator factory.
	 */
	public XmlPushableList(XmlPushableCreator<E> factory) {
		mFactory = factory;
		mList = new ArrayList<E>();
	}
	
	/**
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public E get(int location) {
	    return mList.get(location);
	}
	
	/**
	 * @see java.util.AbstractList#size(int)
	 */
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
	
	/**
	 * @see java.util.AbstractList#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int location, Collection<? extends E> collection) {
	    return mList.addAll(location, collection);
	}
	
	/**
	 * @see java.util.AbstractCollection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
	    return mList.addAll(collection);
	}
	
	/**
	 * @see java.util.AbstractList#clear()
	 */
	@Override
	public void clear() {
		mList.clear();
	}
}

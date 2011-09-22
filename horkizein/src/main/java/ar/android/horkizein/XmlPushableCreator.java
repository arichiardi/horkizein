package ar.android.horkizein;

import ar.android.horkizein.Creator;

public interface XmlPushableCreator<T extends XmlPushable> extends Creator<T> {
	
	/**
	 * The tag this factory can produce item of.
	 * @return The tag.
	 */
	String getTag();
	
}

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

import java.io.IOException;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * This class does Xml to XmlPushable binding. You can decide the storage you want to use.
 */
public class XmlFiller {
	/**
	 * The parser you want to use.
	 */
	protected XmlPullParser mParser;
	/**
	 * Storage for registered XmlPushables.
	 */
	protected Map<String, XmlPushable> mRegisteredItems;

	/**
	 * Constructor.
	 * @param parser Preferred XmlPullParser.
	 */
	public XmlFiller(XmlPullParser parser, Map<String, XmlPushable> registeredList) {
		mParser = parser;
		mRegisteredItems = registeredList;
	}

	/**
	 * Starts the filling process of registered objects. It first fills registered Lists, then Items.
	 * @throws XmlPushableException	Thrown by this class, mostly related to parser positioning.
	 * @throws XmlPullParserException Thrown by the XmlPullParser directly.
	 * @throws IOException	Thrown by the XmlPullParser directly.
	 */
	final public void fill() throws XmlPushableException, XmlPullParserException, IOException {

		if (mParser == null) throw new XmlPushableException("The XmlPullParser has not been set");
		if (mRegisteredItems == null) throw new XmlPushableException("The Registered Items Map has not been set");

		String tag;
		int eventType = mParser.getEventType();
		if(eventType != XmlPullParser.START_DOCUMENT) throw new XmlPushableException("The XmlPullParser is not at START_DOCUMENT event");

		XmlPushable registeredItem;

		while (!mRegisteredItems.isEmpty() && (eventType != XmlPullParser.END_DOCUMENT)) {

			if(eventType == XmlPullParser.START_TAG) {

				tag = mParser.getName();
				// Then looks for a registered item
				if (mRegisteredItems.size() > 0) {
					registeredItem = mRegisteredItems.get(tag);

					if (registeredItem != null) {
						fillItem(tag, registeredItem);
						mRegisteredItems.remove(tag);
					}
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				registeredItem = null;
			}

			eventType = mParser.next();
		}
	}

	/**
	 * Sets the XmlPullParser to read data from.
	 * @param parser An instance of XmlPullParser.
	 */
	public void setParser(XmlPullParser parser) {
		mParser = parser;
	}

	/**
	 * Registers an XmlPushable object to fill with xml data.
	 */
	public <E extends XmlPushable> void registerNode(XmlPushable item) {
		mRegisteredItems.put(item.getTag(), item);
	}

	/**
	 * Internal routine to fill Items.
	 * @param startTag Item's tag.
	 * @param pullable Object to fill.
	 * @throws XmlPushableException
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	final private void fillItem(String startTag, XmlPushable pullable) throws XmlPushableException, XmlPullParserException, IOException {

		int eventType = mParser.getEventType();

		//should arrive here in START_TAG
		if (eventType != XmlPullParser.START_TAG) throw new XmlPushableException("Parser is not at START_TAG event");
		if (!startTag.equals(mParser.getName())) throw new XmlPushableException("The Parser tag is not <" + startTag + ">");

		String currentTag = startTag;

		while (eventType != XmlPullParser.END_DOCUMENT) {

			if(eventType == XmlPullParser.START_TAG) {

				currentTag = mParser.getName();

				pullable.pushStartTag(currentTag);

				for (int i = 0; i < mParser.getAttributeCount(); ++i) {
					pullable.pushAttribute(currentTag, mParser.getAttributeName(i), mParser.getAttributeValue(i));
				}

			} else if(eventType == XmlPullParser.END_TAG) {

				pullable.pushEndTag(mParser.getName());

				if (startTag.equals(mParser.getName()))
					break;

			} else if(eventType == XmlPullParser.TEXT) {
				pullable.pushText(currentTag, mParser.getText());
			}

			eventType = mParser.next();
		}
	}
}


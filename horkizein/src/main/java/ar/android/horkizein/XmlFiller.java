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
package ar.android.horkizein;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.SparseArray;

/**
 * This class performs the XML binding. You can pass a Map in order to register XmlPushable objects.
 * The Map is modified during the execution (clients should provide a copy of the original data).
 * Note that XmlFiller ignores registered tags of a registered parent. The outermost wins.
 * XmlFiller includes a buffer for multiple TEXT event pulled from the parser. For this reason,
 * pushText() is called exactly once for each tag (if there is text to push).<br>
 * Metadata content is handled the same way as tags, but the developer has to call the function
 * <code>fillToken()</code> instead of <code>fill()</code>. Every time a Metadata event is pulled, XmlFiller calls both pushStartTag()
 * and pushEndTag(), plus pushText() if necessary. To register a Metadata XmlPushable please use
 * the static fields CDSECT_TAG, COMMENT_TAG, DOCDECL_TAG, PROCESSING_TAG in your getTag() implementation.
 * @brief Main executing class.
 * 
 */
public class XmlFiller {
	
	/**
	 * CDATA tag string.
	 */
	public static final String CDSECT_TAG = "CDATA";
	/**
	 * COMMENT tag string.
	 */
	public static final String COMMENT_TAG = "COMMENT";
	/**
	 * DOCDECL tag string.
	 */
	public static final String DOCDECL_TAG = "DOCDECL";
	/**
	 * PROCESSING_INSTRUCTION tag string.
	 */
	public static final String PROCESSING_TAG = "PROCESSING";
	
	private static final int METADATA_EVT = 42;
    /**
     * The parser you want to use.
     */
    protected XmlPullParser mParser;
    /**
     * Storage for registered XmlPushables.
     */
    protected Map<String, XmlPushable> mPushableMap;

    protected SparseArray<String> mMetadataMap;
    
    /**
     * Constructor.
     * @param parser Preferred XmlPullParser class.
     */
    public XmlFiller(XmlPullParser parser) {
        mParser = parser;
        mPushableMap = new HashMap<String, XmlPushable>();
        // building the Metadata lookup table
        buildMetadataMap(); 
    }
    
    /**
     * Constructor.
     * @param parser Preferred XmlPullParser class.
     * @param pushableMap Registered objects Map.
     */
    public XmlFiller(XmlPullParser parser, Map<String, XmlPushable> pushableMap) {
        mParser = parser;
        mPushableMap = pushableMap;
        
        // building the Metadata lookup table
        buildMetadataMap();
    }

    /** 
     * Internal function to build the Metadata SparseArray.
     */
    private void buildMetadataMap() {
    	// building the Metadata lookup table
        mMetadataMap = new SparseArray<String>();
        mMetadataMap.put(XmlPullParser.CDSECT, CDSECT_TAG);
        mMetadataMap.put(XmlPullParser.COMMENT, COMMENT_TAG);
        mMetadataMap.put(XmlPullParser.DOCDECL, DOCDECL_TAG);
        mMetadataMap.put(XmlPullParser.PROCESSING_INSTRUCTION, PROCESSING_TAG);
    }
    
    /**
     * Starts the filling process of registered objects, pulling tag data from the Xml file. 
     * @throws XmlPushableException	Thrown by this class, mostly related to parser positioning.
     * @throws XmlPullParserException Thrown by the XmlPullParser directly.
     * @throws IOException	Thrown by the XmlPullParser directly.
     */
    final public void fill() throws XmlPushableException, XmlPullParserException, IOException {
    	if (mParser == null) throw new XmlPushableException("The XmlPullParser has not been set");
        if (mPushableMap == null) throw new XmlPushableException("The Registered Items Map has not been set");

        String tag;
        int eventType = mParser.getEventType();
        if(eventType != XmlPullParser.START_DOCUMENT) throw new XmlPushableException("The XmlPullParser is not at START_DOCUMENT event");

        while (!mPushableMap.isEmpty() && (eventType != XmlPullParser.END_DOCUMENT)) {
        	
            if(eventType == XmlPullParser.START_TAG) {

                tag = mParser.getName();
                // Then looks for a registered item
                if (mPushableMap.size() > 0) {
                	XmlPushable registeredItem = mPushableMap.get(tag);
                    if (registeredItem != null) {
                        fillItem(tag, registeredItem);
                        mPushableMap.remove(tag);
                    }
                }
            }
            eventType = mParser.next();
        }
    }
    
    /**
     * Internal routine to fill an XmlPushable.
     * @param startTag Item's tag.
     * @param pullable Object to fill.
     * @throws XmlPushableException	Thrown by this class, mostly related to parser positioning.
     * @throws XmlPullParserException Thrown by the XmlPullParser directly.
     * @throws IOException	Thrown by the XmlPullParser directly.
     */
    final private void fillItem(String startTag, XmlPushable pullable) throws XmlPushableException, XmlPullParserException, IOException {

        int eventType = mParser.getEventType();

        //should arrive here in START_TAG
        if (eventType != XmlPullParser.START_TAG) throw new XmlPushableException("Parser is not at START_TAG event");
        if (!startTag.equals(mParser.getName())) throw new XmlPushableException("The Parser tag is not <" + startTag + ">");

        String currentTag = startTag;
        StringBuilder currentText = new StringBuilder();
        
        while (eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_TAG) {
                currentTag = mParser.getName();
                currentText.setLength(0);
                pullable.pushStartTag(currentTag);

                for (int i = 0; i < mParser.getAttributeCount(); ++i) {
                    pullable.pushAttribute(currentTag, mParser.getAttributeName(i), mParser.getAttributeValue(i));
                }

            } else if(eventType == XmlPullParser.END_TAG) {
            	pullable.pushText(currentTag, currentText.toString());
                pullable.pushEndTag(mParser.getName());

                if (startTag.equals(mParser.getName()))
                    break;

            } else if(eventType == XmlPullParser.TEXT) {
            	currentText.append(mParser.getText());
            }
            
            eventType = mParser.next();
        }
    }
    
    /**
     * Starts the filling process of registered objects, including registered XML Metadata tags.
     * Use <code>fillWithToken()</code> instead of <code>fill()</code> if you also need to pull from Metadata.
     * @throws XmlPushableException	Thrown by this class, mostly related to parser positioning.
     * @throws XmlPullParserException Thrown by the XmlPullParser directly.
     * @throws IOException	Thrown by the XmlPullParser directly.
     */
    final public void fillWithToken() throws XmlPushableException, XmlPullParserException, IOException {
        if (mParser == null) throw new XmlPushableException("The XmlPullParser has not been set");
        if (mPushableMap == null) throw new XmlPushableException("The Registered Item Map has not been set");

        int currentEvent = mParser.getEventType();
        if(currentEvent != XmlPullParser.START_DOCUMENT) throw new XmlPushableException("The XmlPullParser is not at START_DOCUMENT event");
        
        StringBuilder previousText = new StringBuilder();
        StringBuilder currentText = new StringBuilder();
        XmlPushable registeredItem = null;
        XmlPushable currentItem = null;
        String previousTag = null;
        String currentTag = null;
        int previousEvent = currentEvent;
        boolean documentIsOver = false;
        
        while (!mPushableMap.isEmpty() && !documentIsOver) {
        	
        	if (mMetadataMap.indexOfKey(currentEvent) > -1) {
        		currentTag = mMetadataMap.get(currentEvent); //...or a registered Metadata tag
        		currentText.append(mParser.getText());
        		currentEvent = METADATA_EVT;
        	} else if(currentEvent == XmlPullParser.START_TAG) {
        		currentTag = mParser.getName(); // either a START_TAG...
        	} else if(currentEvent == XmlPullParser.TEXT) {
        		currentText.append(mParser.getText());
        	} else if (currentEvent == XmlPullParser.END_TAG) {
        		currentTag = mParser.getName();
        		if (registeredItem != null) {
        			String tag = registeredItem.getTag();
        			if (tag != null && tag.equals(mParser.getName())) {
        				registeredItem.pushEndTag(tag);
        				mPushableMap.remove(tag);
        				registeredItem = null;
        			}
        		}
        	} else if (currentEvent == XmlPullParser.END_DOCUMENT) {
        		documentIsOver = true;
        	}
        	
        	// look up for a registered item
        	if (currentTag != null && (currentEvent == METADATA_EVT || currentEvent == XmlPullParser.START_TAG)) {
        		currentItem = mPushableMap.get(currentTag);
        		if (currentItem != null) {
        			currentItem.pushStartTag(currentTag);
        			// Push Attributes
        			for (int i = 0; i < mParser.getAttributeCount(); ++i) {
        				currentItem.pushAttribute(currentTag, mParser.getAttributeName(i), mParser.getAttributeValue(i));
        			}
        		}
        	}
        	
        	// This is a postponed filling algorithm, it always pushes events the iteration after receiving
        	// the actual one from the parser. That's why the first time I find a registered item, i need to skip this part.
        	if (registeredItem != null ) {
        		if (previousEvent == METADATA_EVT && (currentEvent == METADATA_EVT || (currentEvent != METADATA_EVT && currentEvent != XmlPullParser.TEXT))) {
        			registeredItem.pushStartTag(previousTag);
        			registeredItem.pushText(previousTag, previousText.toString());
        			previousText.setLength(0);
        			registeredItem.pushEndTag(previousTag);

        			if (previousTag.equals(registeredItem.getTag())) {
    					mPushableMap.remove(previousTag);
    					registeredItem = null;
        			}
        			
        			if (currentEvent == XmlPullParser.END_TAG && currentTag != null) {
        				registeredItem.pushEndTag(currentTag);
        			}
        		} else if (previousEvent == XmlPullParser.TEXT && currentEvent != XmlPullParser.TEXT) {
        			registeredItem.pushText(previousTag, previousText.toString());
        			previousText.setLength(0);
        		} else if ((previousEvent == XmlPullParser.START_TAG && currentEvent == XmlPullParser.TEXT) ||
        				(previousEvent == XmlPullParser.END_TAG && currentEvent == XmlPullParser.END_TAG)) {

        			registeredItem.pushStartTag(previousTag);
        			// Push Attributes
        			for (int i = 0; i < mParser.getAttributeCount(); ++i) {
        				registeredItem.pushAttribute(previousTag, mParser.getAttributeName(i), mParser.getAttributeValue(i));
        			}
        		}
        	}
        	
        	if (currentItem != null) {
        		// now I can set a registered Item
        		registeredItem = currentItem;
        		currentItem = null;
        	}
        	
        	previousText.append(currentText);
    		currentText.setLength(0);
        	previousTag = currentTag;
        	previousEvent = currentEvent;
            currentEvent = mParser.nextToken(); 
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
     * Registers an XmlPushable object to fill.
     * @param item XmlPushable object.
     */
    public <E extends XmlPushable> void registerNode(E item) {
        mPushableMap.put(item.getTag(), item);
    }
    
    /**
     * Registers a list of XmlPushable objects to fill.
     * @param items XmlPushable list.
     */
    public <E extends XmlPushable> void registerNode(Collection<E> items) {
        for (XmlPushable item : items) {
        	mPushableMap.put(item.getTag(), item);
        }
    }   
}
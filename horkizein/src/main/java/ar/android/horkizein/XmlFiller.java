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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.SparseArray;

/**
 * This class performs the XML binding. You can pass a Collection of String in order to register
 * the tags that XmlFiller accepts as "pushable".
 * XmlFiller includes a buffer for multiple TEXT event pulled from the parser. For this reason,
 * pushText() is called exactly once for each tag (if there is text to push).<br>
 * Metadata content is handled the same way as standard tags: every time a Metadata event is pulled,
 * XmlFiller calls both pushStartTag() and pushEndTag(), plus pushText() if necessary (once).
 * To register a Metadata XmlPushable please use the static fields CDSECT_TAG, COMMENT_TAG, DOCDECL_TAG,
 * PROCESSING_TAG as tag in your implementation.
 * If some tag has been registered but not found in the source .xml file, it will still be considered as
 * "registered". Be sure to call reset() before starting over.
 * XmlFiller's internal data structures are not thread-safe.
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
    /**
     * The parser you want to use.
     */
    protected XmlPullParser mParser;
    /**
     * Storage for registered XmlPushables.
     */
    protected Map<String, List<XmlPushable>> mPushableMap;
    /**
     * Storage for handled Metadata tags.
     */
    protected SparseArray<String> mMetadataMap;
    
    /**
     * Constructor.
     * @param parser Preferred XmlPullParser class.
     */
    public XmlFiller(XmlPullParser parser) {
        mParser = parser;
        mPushableMap = new HashMap<String, List<XmlPushable>>();
        // building the Metadata lookup table
        buildMetadataMap(); 
    }
    
    /**
     * Constructor (for debugging only).
     * @param parser Preferred XmlPullParser class.
     * @param pushableMap Registered objects Map.
     */
    public XmlFiller(XmlPullParser parser, Map<String, List<XmlPushable>> pushableMap) {
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
     * Starts the filling process of registered objects, pulling tag data from the XmlPullParser.
     * This function always fills the outermost tag, meaning that children of registered tags will
     * be pushed to their parent, therefore ignoring the registered object.
     * The filling process respect the insertion order. If two or more objects with the same tag have
     *  been registered, they will be pushed in order (FIFO).
     * @throws XmlPushableException	Thrown by this class, mostly related to parser positioning.
     * @throws XmlPullParserException Thrown by the XmlPullParser directly.
     * @throws IOException	Thrown by the XmlPullParser directly.
     */
    final public <E extends XmlPushable> void outmostFill() throws XmlPushableException, XmlPullParserException, IOException {
    	
    	if (mParser == null) throw new XmlPushableException("The XmlPullParser has not been set");
        if (mPushableMap == null) throw new XmlPushableException("The Registered Items Map has not been set");
        if (mPushableMap.isEmpty()) return;
        
        int currentEvent = mParser.getEventType();

        String currentTag = "";
        String startTag = "";
        XmlPushable registeredItem = null;
        StringBuilder currentText = new StringBuilder();
        
        while (currentEvent != XmlPullParser.END_DOCUMENT) {
            if(currentEvent == XmlPullParser.START_TAG) {
                currentTag = mParser.getName();
                currentText.setLength(0);
                ///////////////////////////////////////////////////////////
                if (registeredItem == null) {
                	// Looking for a registered item
                	List<XmlPushable> stack = mPushableMap.get(currentTag);
            		if (stack != null && !stack.isEmpty()) {
            			registeredItem = stack.remove(0);
            			startTag = currentTag;
            		}
                }
                if (registeredItem != null) {
                	registeredItem.pushStartTag(currentTag);
                	for (int i = 0; i < mParser.getAttributeCount(); ++i) {
                		registeredItem.pushAttribute(currentTag, mParser.getAttributePrefix(i), mParser.getAttributeName(i), mParser.getAttributeValue(i));
                	}
                } else {
                	mParser.nextTag();
                }
                ///////////////////////////////////////////////////////////
            } else if(currentEvent == XmlPullParser.TEXT) {
            	currentText.append(mParser.getText());
            } else if (mMetadataMap.indexOfKey(currentEvent) > -1) {
        		currentTag = mMetadataMap.get(currentEvent); //...entry point for a registered Metadata tag
        		///////////////////////////////////////////////////////////
        		if (registeredItem == null) {
        			// Looking for a registered item
                	List<XmlPushable> stack = mPushableMap.get(currentTag);
            		if (stack != null && !stack.isEmpty()) {
            			registeredItem = stack.remove(0);
            			startTag = currentTag;
            		}
        		}
                if (registeredItem != null) {
                	registeredItem.pushStartTag(currentTag);
            		registeredItem.pushText(currentTag, mParser.getText());
            		registeredItem.pushEndTag(currentTag);
            		if (startTag.equals(currentTag)) {
                    	registeredItem = null;
                    	startTag = "";
                    }
                } else {
                	mParser.nextTag();
                }
                ///////////////////////////////////////////////////////////
            } else if(currentEvent == XmlPullParser.END_TAG) {
            	currentTag = mParser.getName();
            	registeredItem.pushText(currentTag, currentText.toString());
            	registeredItem.pushEndTag(currentTag);

                if (startTag.equals(currentTag)) {
                	registeredItem = null;
                	startTag = "";
                }
            }
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
    	List<XmlPushable> stack = mPushableMap.get(item.getTag());
		if (stack == null) {
			stack = new ArrayList<XmlPushable>();
			mPushableMap.put(item.getTag(), stack);
		}
		stack.add(item);
    }
    
    /**
     * Registers a list of XmlPushable objects to fill.
     * @param items XmlPushable list.
     */
    public <E extends XmlPushable> void registerNode(Collection<E> items) {
    	for (XmlPushable item : items) {
    		List<XmlPushable> stack = mPushableMap.get(item.getTag());
    		if (stack == null) {
    			stack = new ArrayList<XmlPushable>();
    			mPushableMap.put(item.getTag(), stack);
    		}
    		stack.add(item);
    	}
    }
    
    /**
     * Resets the list of registered objects.
     */
    public void reset() {
    	mPushableMap.clear();
    }
}
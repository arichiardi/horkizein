package com.googlecode.horkizein.obj;

public class NoTextObject {

    public final static String TAG = "no_text_obj";
    
    private final String mMyText;
    private final TextObject mTextObject;
    
    private String mSupposedlyNoText = "s0me TeXt, 4 T3st1ng";

    public NoTextObject(String myText, TextObject textObject) { 
        mMyText = myText;
        mTextObject = textObject;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if((o == null) || (o.getClass() != this.getClass())) return false;

        NoTextObject n = (NoTextObject)o;
        return (mSupposedlyNoText == n.mSupposedlyNoText || (mSupposedlyNoText != null && mSupposedlyNoText.equals(n.mSupposedlyNoText)) &&
                 mTextObject == n.mTextObject || (mTextObject != null && mTextObject.equals(n.mTextObject)));
    }
    
    /**
     * Note: This method safely return a reference just because the 
     * returned object is immutable.
     * @return 
     */
    public TextObject getTextObject() {
        return mTextObject;
    }
}

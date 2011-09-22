package ar.android.horkizein.obj;

import ar.android.horkizein.XmlPushableCreator;

public class FlatObjectCreator implements XmlPushableCreator<FlatObject> {

	// This object tag
	public final static String TAG = "flat_obj_c";
	
	// Shallow copy source
	private FlatObject mCopySrc;
	
	public FlatObjectCreator(FlatObject src) {
		mCopySrc = src;
    }
	public FlatObjectCreator() {}
	
	@Override
    public FlatObject create() {
		FlatObject tmp;
		
		if (mCopySrc != null)
			// Just for test, passing the reference
			tmp = mCopySrc;
		else
			// new empty object otherwise
			tmp = new FlatObject();
		
		return tmp;
    }

	@Override
    public String getTag() {
	    return TAG;
    }

}

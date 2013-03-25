package com.googlecode.horkizein.obj;

/**
 * Class that doesn't have {ode XmlTag} annotation.
 */
public class DummyObject {
    
    public static final String TAG = "dummy";
    private final AbstractObject mAbstract;
    private final InterfaceObject mInterface;
    
    public DummyObject(AbstractObject abs, InterfaceObject inter) {
        mAbstract = abs;
        mInterface = inter;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != this.getClass())) return false;
        DummyObject o = (DummyObject)obj;
        return ((mAbstract == o.mAbstract || (mAbstract != null && o.mAbstract != null && mAbstract.equals(o.mAbstract)))
            && (mInterface == o.mInterface || (mInterface != null && o.mInterface != null && mInterface.equals(o.mInterface))));
    }
    /**
     * Note: This method should return a copy because we cannot rely
     * on the fact that the implementation is immutable. But we are
     * testing different things here, therefore a reference is returned.
     * @return 
     */
    public AbstractObject getAbstract() {
        return mAbstract;
    }
    /**
     * Note: This method should return a copy because we cannot rely
     * on the fact that the implementation is immutable. But we are
     * testing different things here, therefore a reference is returned.
     * @return 
     */
    public InterfaceObject getInterface() {
        return mInterface;
    }
}

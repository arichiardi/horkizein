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
package com.googlecode.horkizein.obj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import com.googlecode.horkizein.XmlPushable;
import com.googlecode.horkizein.XmlPushableArrayList;
import com.googlecode.horkizein.XmlTag;
import com.googlecode.horkizein.XmlWritable;
import com.googlecode.horkizein.obj.builders.FlatObjectBuilder;
import com.googlecode.horkizein.test.Constants;

import android.util.Log;

@XmlTag (
    value = "flat_obj_list",
    enclosedPushables = FlatObject.class
)

public class FlatObjectList extends XmlPushableArrayList<FlatObject> implements XmlWritable{
   
    public final static String TAG = "flat_obj_list";
    
    /**
     * Ctor with input list.
     * @param list
     */
    public FlatObjectList(List<FlatObject> list) {
        super(list, new FlatObjectBuilder());
    }

    /**
     * Plain Ctor. The internal list is empty.
     */
    public FlatObjectList() {
        super(new ArrayList<FlatObject>(), new FlatObjectBuilder());
    }

    @Override
    public void writeXml(XmlSerializer out) throws IOException, IllegalStateException, IllegalArgumentException {
        out.startTag("", TAG);
        for (FlatObject ai : this) {
            ai.writeXml(out);
        }
        out.endTag("", TAG);
    }

    @Override
    public boolean equals(Object obj) {

        Log.d(Constants.PACKAGE_TAG_TEST, "this.size() is: " + this.size());
        Log.d(Constants.PACKAGE_TAG_TEST, "----------------");
        int i = 0;
        for (XmlPushable o : this) {
            Log.d(Constants.PACKAGE_TAG_TEST, i + ") " + o.toString());
            i++;
        }
        Log.d(Constants.PACKAGE_TAG_TEST, "o.size() is: " + ((FlatObjectList)obj).size());
        Log.d(Constants.PACKAGE_TAG_TEST, "++++++++++++++++");
        i = 0;
        for (XmlPushable o : (FlatObjectList)obj) {
            Log.d(Constants.PACKAGE_TAG_TEST, i + ") " + o.toString());
            i++;
        }
        return super.equals(obj);
    }

    /**
     * Simple check to see if we push tags in the correct order.
     * @return True or false.
     */
    public boolean tagCheck() {
        boolean listCheck = true;
        for (FlatObject ai : this) {
            listCheck &= ai.tagCheck();
        }
        return (listCheck);
    }

    @Override
    protected String getItemTag() {
        return FlatObject.TAG;
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}

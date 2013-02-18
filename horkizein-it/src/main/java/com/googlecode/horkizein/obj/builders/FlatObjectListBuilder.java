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
package com.googlecode.horkizein.obj.builders;

import com.googlecode.horkizein.XmlBuilder;
import com.googlecode.horkizein.obj.FlatObjectList;

/**
 * Self-explanatory
 */
public class FlatObjectListBuilder implements XmlBuilder<FlatObjectList> {

    @Override
    public FlatObjectList getInstance() {
        // TODO Auto-generated method stub
        return new FlatObjectList();
    }

}
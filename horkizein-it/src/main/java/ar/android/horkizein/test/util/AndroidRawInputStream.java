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
package ar.android.horkizein.test.util;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;

public class AndroidRawInputStream implements Closeable {

    InputStream input;

    /**
     * 
     * @param context 	Android Context class
     * @param id	Android Resource id
     * @throws FileNotFoundException
     */
    public AndroidRawInputStream(Context context, int id) throws Resources.NotFoundException {
	input = context.getResources().openRawResource(id);
    }

    public InputStream getStream() {
	return input;
    }

    public void close() throws IOException {
	input.close();
    }
    
    //Never use finalizer!
//    public void finalize() throws Throwable {
//	try {
//	    close();
//	} finally {
//	    super.finalize();
//	}
//    }
}

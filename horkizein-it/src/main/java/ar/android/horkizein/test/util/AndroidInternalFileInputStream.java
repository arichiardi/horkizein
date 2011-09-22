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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

public class AndroidInternalFileInputStream extends InputStreamReader implements Closeable {

	public FileInputStream input;

	public AndroidInternalFileInputStream(Context context, String filePath) throws FileNotFoundException {
		super(context.openFileInput(filePath));
		input = context.openFileInput(filePath);
	}

	public void close() throws IOException {
		input.close();
		input = null;
	}


	@Override
	public int read() throws IOException {
		return super.read();
	}
   
}

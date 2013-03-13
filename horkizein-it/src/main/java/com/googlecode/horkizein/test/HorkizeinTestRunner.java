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
package com.googlecode.horkizein.test;

import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

/**
 * Main Horkizein InstrumentationTestRunner.
 */
public final class HorkizeinTestRunner extends InstrumentationTestRunner {

    @Override
    public TestSuite getAllTests() {

        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);
        // Object Read Write Tests
        suite.addTestSuite(EqualityTest.class);
        // Object Read Write Tests - Custom Parser
        // (the Custom parser splits TEXT  events that contain more than X chars)
        //suite.addTestSuite(EqualityCustomParserTest.class);
        // Object Error Tests
        suite.addTestSuite(ErrorTest.class);
        // Performance Tests
        return suite;
    }

    @Override
    public ClassLoader getLoader() {
        return HorkizeinTestRunner.class.getClassLoader();
    }
}

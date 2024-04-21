/**
 * Copyright (C) 2022 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.uparser.util.args;

import com.ancevt.util.args.Args;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author <a href="mailto:me@ancevt.com">ancevt</a>
 * Created on Aug 21, 2020
 */
class ArgsTest {

    @Test
    void testNumberFormatException() {
        final Args args = new Args("a b c");
        int a = args.next(int.class, 0);

        assertThat(a, is(0));
        assertNotNull(args.getProblem());
    }

    @Test
    void testNextString() {
        final Args args = new Args("a b c d");
        String a = args.next();
        assertThat(a, is("a"));

        String b = args.next();
        assertThat(b, is("b"));

        args.next();

        String d = args.next();
        assertThat(d, is("d"));
    }

    @Test
    void testNext() {
        final Args args = new Args("a 1 2.5 true");
        String a = args.next(String.class);
        assertThat(a, is("a"));

        int i = args.next(int.class);
        assertThat(i, is(1));

        float f = args.next(float.class);
        assertThat(f, is(2.5f));

        boolean b = args.next(boolean.class);
        assertThat(b, is(true));
    }

    @Test
    void testHasNextAndSkip() {
        final Args args = new Args("a b c");
        args.skip(2);

        assertTrue(args.hasNext());

        args.skip();

        assertFalse(args.hasNext());
    }

    @Test
    void testDelimiter() {
        final Args args = new Args("20,30,40,50", ',');
        assertThat(args.next(), is("20"));
        assertThat(args.next(int.class), is(30));
    }

    @Test
    void testDelimiterStringChar() {
        final Args args = new Args("20,30,40,50", ",");
        assertThat(args.next(), is("20"));
        assertThat(args.next(int.class), is(30));
    }

    @Test
    void testCommon() {
        final Args args = new Args("command --host ancevt.com --port 1234 -d true");

        final String host = args.get("--host", "default.host");
        assertThat(host, is("ancevt.com"));

        final int port = args.get(Integer.class, "--port", 7777);
        assertThat(port, is(1234));

        final boolean debug = args.get(Boolean.class, new String[]{"--debug", "-d"}, false);
        assertTrue(debug);
    }

    @Test
    void testDefaultValue() {
        final Args args = new Args("command --host ancevt.com --port 1234 -d true");
        assertThat(args.get(long.class, "--some", 2L), is(2L));
    }

    @Test
    void testContains() {
        final Args args = new Args("command --foo bar --last");
        assertTrue(args.contains("--last"));
    }

    @Test
    void testQuotes() {
        final Args args = new Args("command --key \"this is value\"");

        assertThat(args.getElements().length, is(3));
        assertThat(args.get("--key"), is("this is value"));
    }

    @Test
    void testSingleQuotes() {
        final Args args = new Args("command --key 'this is value'");

        assertThat(args.getElements().length, is(3));
        assertThat(args.get("--key"), is("this is value"));
    }

    @Test
    void testLastContainsCheckedKey() {
        final Args args = new Args("command --host ancevt.com --port 1234 -d true");
        if (args.contains("-h", "--host")) {
            assertThat(args.get(String.class), is("ancevt.com"));
        } else {
            fail();
        }
    }
}

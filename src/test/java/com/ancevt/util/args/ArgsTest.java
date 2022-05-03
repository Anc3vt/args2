/*
 *   Args II
 *   Copyright (C) 2020 Ancevt (me@ancevt.com)
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ancevt.util.args;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author <a href="mailto:me@ancevt.com">ancevt</a>
 * Created on Aug 21, 2020
 */
class ArgsTest {

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
}

/**
 *   Args II
 *   Copyright (C) 2020 Ancevt
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
package ru.ancevt.util.args;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:i@ancevt.ru">ancevt</a>
 * Created on Aug 21, 2020
 */
class ArgsTest {

    @Test
    void testCommon() {
        final Args args = new Args("command --host ancevt.ru --port 1234 -d true");

        final String host = args.get("--host", "default.host");
        assertThat(host, is("ancevt.ru"));

        final int port = args.get(Integer.class, "--port", 7777);
        assertThat(port, is(1234));

        final boolean debug = args.get(Boolean.class, new String[]{"--debug", "-d"}, false);
        assertTrue(debug);
    }

    @Test
    void testDefaultValue() {
        final Args args = new Args("command --host ancevt.ru --port 1234 -d true");
        assertThat(args.get(Long.class, "--some", 2L), is(2L));
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

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

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ArgsSplitter {

    private static final String SPACE_CHARS = "\n\t\r\b ";

    public static String[] split(final String source) {
        final List<String> list = new ArrayList<>();
        final StringBuilder stringBuilder = new StringBuilder();
        int str = -1;
        int i = 0;
        final int len = source.length();
        while (i < len) {
            final char c = source.charAt(i++);
            if (c == '\\') {
                if (i == len) {
                    break;
                }
                final char c2 = source.charAt(i++);
                stringBuilder.append(c2);
                continue;
            }
            if (str == -1) {
                if (c == '"' || c == '\'') {
                    str = c;
                    continue;
                }
                if (SPACE_CHARS.indexOf(c) != -1) {
                    if (stringBuilder.length() != 0) {
                        list.add(stringBuilder.toString());
                        stringBuilder.setLength(0);
                    }
                    continue;
                }
            } else {
                if (c == str) {
                    str = -1;
                    continue;
                }
            }
            stringBuilder.append(c);
        }
        if (stringBuilder.length() != 0) {
            list.add(stringBuilder.toString());
        }

        return list.toArray(new String[]{});
    }
}

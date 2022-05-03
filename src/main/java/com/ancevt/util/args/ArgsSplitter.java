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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class ArgsSplitter {

    private static final String SPACE_CHARS = "\n\t\r\b ";

    static String @NotNull [] split(final @NotNull String source, char delimiterChar) {
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

                if (delimiterChar == '\0') {
                    if (SPACE_CHARS.indexOf(c) != -1) {
                        if (stringBuilder.length() != 0) {
                            list.add(stringBuilder.toString());
                            stringBuilder.setLength(0);
                        }
                        continue;
                    }
                } else {
                    if (c == delimiterChar) {
                        if (stringBuilder.length() != 0) {
                            list.add(stringBuilder.toString());
                            stringBuilder.setLength(0);
                        }
                        continue;
                    }
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

    public static String @NotNull [] split(String source, @NotNull String delimiterChar) {
        if (delimiterChar.length() != 1) {
            throw new ArgsException("delimiter string must contain one character");
        }
        return split(source, delimiterChar.charAt(0));
    }
}

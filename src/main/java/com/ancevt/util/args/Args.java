/*
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
package com.ancevt.util.args;

import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

public class Args {

    public static void main(String[] args) {
        String source = "localhost --message \"this is the message\" -p 3333 -h localhost --debug true ";
        Args a = new Args(source);

        System.out.println(a.get("--message"));
    }

    private final String source;

    private final String[] elements;

    private int index;

    public Args(String source) {
        this.source = source;
        elements = ArgsSplitter.split(source);
    }

    public Args(String[] args) {
        this.source = collectSource(args);
        elements = args;
    }

    private @NotNull String collectSource(String @NotNull [] args) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (String a : args) {
            a = a.replace("\"", "\\\\\"");
            stringBuilder.append('"').append(a).append('"').append(' ');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public String[] getElements() {
        return elements;
    }

    public boolean contains(String... keys) {
        for (final String e : elements) {
            for (final String k : keys) {
                if (e.equals(k)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String next() {
        return next(String.class);
    }

    public <T> T next(Class<T> type) {
        if(index >= elements.length) {
            throw new ArgsException(format("Index out of bounds, index: %d, elements: %d", index, elements.length));
        }

        T result = get(type, index);
        index++;
        return result;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if(index >= elements.length) {
            throw new ArgsException(format("Index out of bounds, index: %d, elements: %d", index, elements.length));
        }

        this.index = index;
    }

    public void resetIndex() {
        index = 0;
    }

    public int size() {
        return elements.length;
    }

    public <T> T get(Class<T> type, int index, T defaultValue) {
        if (index < 0 || index >= elements.length) return defaultValue;

        return convertToType(elements[index], type);
    }

    public <T> T get(Class<T> type, int index) {
        return get(type, index, null);
    }

    public <T> T get(Class<T> type, String key, T defaultValue) {
        for (int i = 0; i < elements.length - 1; i++) {
            final String currentArgs = elements[i];

            if (currentArgs.equals(key)) {
                return convertToType(elements[i + 1], type);
            }
        }

        return defaultValue;
    }

    public <T> T get(Class<T> type, String @NotNull [] keys, T defaultValue) {
        for (final String key : keys) {
            for (int i = 0; i < elements.length - 1; i++) {
                final String currentArgs = elements[i];

                if (currentArgs.equals(key)) {
                    return convertToType(elements[i + 1], type);
                }
            }
        }

        return defaultValue;
    }

    public <T> T get(Class<T> type, String key) {
        return get(type, key, null);
    }

    public <T> T get(Class<T> type, String[] keys) {
        return get(type, keys, null);
    }

    public String get(String key, String defaultValue) {
        return get(String.class, key, defaultValue);
    }

    public String get(String[] keys, String defaultValue) {
        return get(String.class, keys, defaultValue);
    }

    public String get(String key) {
        return get(String.class, key);
    }

    public String get(String[] keys) {
        return get(String.class, keys);
    }

    private <T> T convertToType(String element, Class<T> type) {
        if (type == String.class) {
            return (T) element;
        } else if (type == Boolean.class || type == boolean.class) {
            return (T) (Boolean.parseBoolean(element) ? Boolean.TRUE : Boolean.FALSE);
        } else if (type == Integer.class || type == int.class) {
            return (T) Integer.valueOf(element);
        } else if (type == Long.class || type == long.class) {
            return (T) Long.valueOf(element);
        } else if (type == Float.class || type == float.class) {
            return (T) Float.valueOf(element);
        } else if (type == Short.class || type == short.class) {
            return (T) Short.valueOf(element);
        } else if (type == Double.class || type == double.class) {
            return (T) Double.valueOf(element);
        } else if (type == Byte.class || type == byte.class) {
            return (T) Byte.valueOf(element);
        } else {
            throw new ArgsException("Type " + type + " not supported");
        }
    }

    public String getSource() {
        return source;
    }

    public boolean isEmpty() {
        return elements == null || elements.length == 0;
    }

    public static @NotNull Args of(String source) {
        return new Args(source);
    }

    public static @NotNull Args of(String[] args) {
        return new Args(args);
    }

}

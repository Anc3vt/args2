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
import org.jetbrains.annotations.Nullable;

import static java.lang.String.format;

public class Args {

    private final String source;

    private final String[] elements;

    private int index;

    private Throwable problem;

    public Args(@NotNull String source) {
        this.source = source;
        elements = ArgsSplitter.split(source, '\0');
    }

    public Args(@NotNull String source, @NotNull String delimiterChar) {
        this.source = source;
        elements = ArgsSplitter.split(source, delimiterChar);
    }

    public Args(@NotNull String source, char delimiterChar) {
        this.source = source;
        elements = ArgsSplitter.split(source, delimiterChar);
    }

    public Args(String @NotNull [] args) {
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

    public boolean hasNext() {
        return index < elements.length;
    }

    public void skip() {
        next();
    }

    public void skip(int count) {
        for(int i = 0; i < count; i ++) {
            next();
        }
    }

    public String next() {
        return next(String.class);
    }

    public <T> T next(Class<T> type) {
        if (index >= elements.length) {
            throw new ArgsException(format("Index out of bounds, index: %d, elements: %d", index, elements.length));
        }

        T result = get(type, index);
        index++;
        return result;
    }

    public <T> T next(Class<T> type, T defaultValue) {
        if (index >= elements.length) {
            throw new ArgsException(format("Index out of bounds, index: %d, elements: %d", index, elements.length));
        }

        T result = get(type, index, defaultValue);
        index++;
        return result;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if (index >= elements.length) {
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

        try {
            return convertToType(elements[index], type);
        } catch (Exception e) {
            problem = e;
            return defaultValue;
        }
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

    private <T> @Nullable T convertToType(String element, Class<T> type) {
        if (type == String.class) {
            return (T) element;
        } else if (type == boolean.class || type == Boolean.class) {
            return (T) (Boolean.parseBoolean(element) ? Boolean.TRUE : Boolean.FALSE);
        } else if (type == int.class || type == Integer.class) {
            return (T) Integer.valueOf(element);
        } else if (type == long.class || type == Long.class) {
            return (T) Long.valueOf(element);
        } else if (type == float.class || type == Float.class) {
            return (T) Float.valueOf(element);
        } else if (type == short.class || type == Short.class) {
            return (T) Short.valueOf(element);
        } else if (type == double.class || type == Double.class) {
            return (T) Double.valueOf(element);
        } else if (type == byte.class || type == Byte.class) {
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

    public boolean hasProblem() {
        return problem != null;
    }

    public Throwable getProblem() {
        return problem;
    }

    public static @NotNull Args of(String source) {
        return new Args(source);
    }

    public static @NotNull Args of(String[] args) {
        return new Args(args);
    }

    public static @NotNull Args of(String source, String delimiterChar) {
        return new Args(source, delimiterChar);
    }

    public static @NotNull Args of(String source, char delimiterChar) {
        return new Args(source, delimiterChar);
    }

}




















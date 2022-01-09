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

import lombok.NonNull;
import lombok.ToString;

@ToString
public class Args {

    private final String source;

    private final String[] elements;

    public Args(@NonNull String source) {
        this.source = source;
        elements = ArgsSplitter.split(source);
    }

    public Args(@NonNull String[] args) {
        this.source = collectSource(args);
        elements = args;
    }

    private String collectSource(String[] args) {
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

    public <T> T get(Class<T> type, String key, T defaultValue) {
        for (int i = 0; i < elements.length - 1; i++) {
            final String currentArgs = elements[i];

            if (currentArgs.equals(key)) {
                return convertToType(elements[i + 1], type);
            }
        }

        return defaultValue;
    }

    public <T> T get(Class<T> type, String[] keys, T defaultValue) {
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
        } else if (type == Boolean.class) {
            return (T) (Boolean.parseBoolean(element) ? Boolean.TRUE : Boolean.FALSE);
        } else if (type == Integer.class) {
            return (T) Integer.valueOf(element);
        } else if (type == Long.class) {
            return (T) Long.valueOf(element);
        } else if (type == Float.class) {
            return (T) Float.valueOf(element);
        } else if (type == Short.class) {
            return (T) Short.valueOf(element);
        } else if (type == Double.class) {
            return (T) Double.valueOf(element);
        } else if (type == Byte.class) {
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

}

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
package com.ancevt.util.args;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ArgsReflectionUtil {

    @Contract("_, _ -> param2")
    public static <T> @NotNull T convert(Args args, @NotNull T objectToFill) throws IllegalAccessException {
        Class<T> type = (Class<T>) objectToFill.getClass();

        for (Field field : type.getDeclaredFields()) {

            ArgsCommand argsCommandAnnotation = field.getDeclaredAnnotation(ArgsCommand.class);
            if (argsCommandAnnotation != null) {
                Class<?> t = field.getType();
                field.setAccessible(true);
                field.set(objectToFill, args.get(t, 0));
                continue;
            }

            ArgsParameter argsParameterAnnotation = field.getDeclaredAnnotation(ArgsParameter.class);
            if (argsParameterAnnotation != null) {

                boolean found = false;

                String[] names = argsParameterAnnotation.names();
                if (names != null) {
                    for (String name : names) {
                        if (args.contains(name)) {
                            field.setAccessible(true);

                            Class<?> fieldType = field.getType();

                            if (fieldType == boolean.class) {
                                field.set(objectToFill, args.contains(name));
                            } else {
                                field.set(objectToFill, args.get(field.getType(), name));
                            }
                            found = true;
                        }
                    }
                }

                if (argsParameterAnnotation.required() && !found)
                    throw new ArgsException("required parameter " + Arrays.toString(names) + " not found");

                continue;
            }
        }

        return objectToFill;
    }

    public static <T> @NotNull T convert(Args args, @NotNull Class<T> type)
            throws NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException {

        Constructor<T> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return convert(args, constructor.newInstance());
    }

}

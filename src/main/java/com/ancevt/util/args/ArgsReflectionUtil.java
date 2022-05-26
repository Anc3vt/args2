package com.ancevt.util.args;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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
                    throw new ArgsException("required parameter not found");

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

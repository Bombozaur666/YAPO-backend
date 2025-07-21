package com.example.YAPO.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ValueConverter {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static Object convert(Class<?> targetType, String value) {
        if (targetType == String.class) return value;
        if (targetType == Integer.class || targetType == int.class) return Integer.parseInt(value);
        if (targetType == Long.class || targetType == long.class) return Long.parseLong(value);
        if (targetType == Boolean.class || targetType == boolean.class) return Boolean.parseBoolean(value);
        if (targetType == Double.class || targetType == double.class) return Double.parseDouble(value);
        if (targetType == LocalDate.class) return LocalDate.parse(value, DATE_FORMAT);
        if (targetType == LocalDateTime.class) return LocalDateTime.parse(value, DATETIME_FORMAT);
        if (targetType.isEnum()) {
                @SuppressWarnings("unchecked")
                Class<? extends Enum> enumType = (Class<? extends Enum>) targetType;
                return Enum.valueOf(enumType, value.toUpperCase());
        }


        throw new IllegalArgumentException("Wrong Type: " + targetType.getSimpleName());
    }
}
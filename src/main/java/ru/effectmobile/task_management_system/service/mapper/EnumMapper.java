package ru.effectmobile.task_management_system.service.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.effectmobile.task_management_system.exception.InvalidEnumValueException;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.ENUM_VALUE_NULL_OR_EMPTY_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.INVALID_ENUM_VALUE_MESSAGE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumMapper {

    private static final Map<Class<? extends Enum<?>>, String> ENUM_VALUES_CACHE = new ConcurrentHashMap<>();

    public static <T extends Enum<T>> T mapToEnum(Class<T> enumClass, String value) {
        if (StringUtils.isBlank(value)) {
            throw new InvalidEnumValueException(String.format(
                    ENUM_VALUE_NULL_OR_EMPTY_MESSAGE,
                    enumClass.getSimpleName(),
                    getAllowedValues(enumClass)
            ));
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException(String.format(
                        INVALID_ENUM_VALUE_MESSAGE,
                        value,
                        enumClass.getSimpleName(),
                        getAllowedValues(enumClass)
                )));
    }

    private static <T extends Enum<T>> String getAllowedValues(Class<T> enumClass) {
        return ENUM_VALUES_CACHE.computeIfAbsent(enumClass, clazz ->
                Arrays.stream(clazz.getEnumConstants())
                        .map(Enum::name)
                        .collect(Collectors.joining(", ")));
    }
}

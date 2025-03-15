package ru.effectmobile.task_management_system.service.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.effectmobile.task_management_system.exception.InvalidEnumValueException;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.ENUM_VALUE_NULL_OR_EMPTY;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.INVALID_ENUM_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumMapper {

    public static <T extends Enum<T>> T mapToEnum(Class<T> enumClass, String value) {
        if (StringUtils.isBlank(value)) {
            throw new InvalidEnumValueException(String.format(
                    ENUM_VALUE_NULL_OR_EMPTY,
                    enumClass.getSimpleName(),
                    getAllowedValues(enumClass)
            ));
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException(String.format(
                        INVALID_ENUM_VALUE,
                        value,
                        enumClass.getSimpleName(),
                        getAllowedValues(enumClass)
                )));
    }

    private static <T extends Enum<T>> String getAllowedValues(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}

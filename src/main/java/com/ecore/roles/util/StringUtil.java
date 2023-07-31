package com.ecore.roles.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

    public static String toUpperSnakeCase(String value) {
        return Optional.ofNullable(value)
                .map(String::trim)
                .map(String::toUpperCase)
                .map(toReplace -> toReplace.replace(" ", "_"))
                .orElse(null);
    }

}

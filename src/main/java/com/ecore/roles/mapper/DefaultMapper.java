package com.ecore.roles.mapper;

import com.ecore.roles.util.StringUtil;
import org.mapstruct.Named;

public interface DefaultMapper {

    @Named("toUpperSnakeCase")
    default String toUpperSnakeCase(String value) {
        return StringUtil.toUpperSnakeCase(value);
    }

}

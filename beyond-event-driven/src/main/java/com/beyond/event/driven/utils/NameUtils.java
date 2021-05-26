package com.beyond.event.driven.utils;

import com.google.common.base.CaseFormat;
import org.springframework.util.StringUtils;

public final class NameUtils {

    private NameUtils() {
        throw new UnsupportedOperationException();
    }

    public static String combinePrefix(final String prefix, final String name) {
        if (StringUtils.isEmpty(prefix)) {
            return name;
        }
        return prefix + '.' + name;
    }

    public static String combineSuffix(final String name, final String suffix) {
        if (StringUtils.isEmpty(suffix)) {
            return name;
        }
        return name + '.' + suffix;
    }

    public static String camelToSnakeCase(final String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }

}
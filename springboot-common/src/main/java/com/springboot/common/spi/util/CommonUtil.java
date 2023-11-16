package com.springboot.common.spi.util;

import com.alibaba.excel.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)");

    public static Map<String, String> toStringMap(String... pairs) {
        Map<String, String> parameters = new HashMap<>();
        if (ArrayUtils.isEmpty(pairs)) {
            return parameters;
        }

        if (pairs.length > 0) {
            if (pairs.length % 2 != 0) {
                throw new IllegalArgumentException("pairs must be even.");
            }
            for (int i = 0; i < pairs.length; i = i + 2) {
                parameters.put(pairs[i], pairs[i + 1]);
            }
        }
        return parameters;
    }

    public static Map<String, String> parseQueryString(String qs) {
        if (StringUtils.isBlank(qs)) {
            return new HashMap<>();
        }
        return parseKeyValuePair(qs, "\\&");
    }


    private static Map<String, String> parseKeyValuePair(String str, String itemSeparator) {
        String[] tmp = str.split(itemSeparator);
        Map<String, String> map = new HashMap<String, String>(tmp.length);
        for (int i = 0; i < tmp.length; i++) {
            Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
            if (!matcher.matches()) {
                continue;
            }
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }


    public static List<String> splitToList(String str, char ch) {
        if (StringUtils.isBlank(str)) {
            return Collections.emptyList();
        }
        return splitToList0(str, ch);
    }

    private static List<String> splitToList0(String str, char ch) {
        List<String> result = new ArrayList<>();
        int ix = 0, len = str.length();
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) == ch) {
                result.add(str.substring(ix, i));
                ix = i + 1;
            }
        }

        if (ix >= 0) {
            result.add(str.substring(ix));
        }
        return result;
    }
}

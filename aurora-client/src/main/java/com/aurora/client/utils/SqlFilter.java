package com.aurora.client.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlFilter {

    // SQL注入过滤
    static final String SQL_KEYWORDS_PATTERN =
            "(?i)(SELECT|FROM|WHERE|CONCAT|AND|NOT|INSERT|UPDATE|DELETE" +
                    "|TABLE|INDEX|VIEW|DROP|ALTER|COLUMN|ADD|SET|GROUP|BY" +
                    "|HAVING|ORDER|ASC|DESC|LIKE|IN|BETWEEN|IS|NULL|TRUE|FALSE" +
                    "|JOIN|LEFT|RIGHT|INNER|OUTER|FULL|ON|AS|DISTINCT|COUNT" +
                    "|MAX|MIN|SUM|AVG|IF|RAND|UPDATEXML|EXTRACTVALUE|LOAD_FILE|SLEEP|OFFSET)";
    // OR 影响排序字段 sort，所以暂时不过滤
    // CREATE 影响常用排序字段， CREATE_TIME，所以暂时不过滤
    static final Pattern keywordPattern = Pattern.compile(SQL_KEYWORDS_PATTERN, Pattern.CASE_INSENSITIVE);

    /**
     * 关键字命中
     */
    public static Boolean hit(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return false;
        }
        Matcher matcher = keywordPattern.matcher(sql);
        return matcher.find();
    }
}

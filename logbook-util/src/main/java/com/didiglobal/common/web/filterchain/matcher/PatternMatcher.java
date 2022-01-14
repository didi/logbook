package com.didiglobal.common.web.filterchain.matcher;

public interface PatternMatcher {

    boolean matches(String pattern, String source);
}

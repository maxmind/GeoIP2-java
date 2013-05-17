package com.maxmind.geoip2.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.maxmind.geoip2.exception.WebServiceException;

public class CodeMatcher extends TypeSafeMatcher<WebServiceException> {

    private String foundErrorCode;
    private final String expectedErrorCode;

    public static CodeMatcher hasCode(String item) {
        return new CodeMatcher(item);
    }

    private CodeMatcher(String expectedErrorCode) {
        this.expectedErrorCode = expectedErrorCode;
    }

    @Override
    protected boolean matchesSafely(final WebServiceException exception) {
        this.foundErrorCode = exception.getCode();
        return this.foundErrorCode.equalsIgnoreCase(this.expectedErrorCode);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(this.foundErrorCode)
                .appendText(" was not found instead of ")
                .appendValue(this.expectedErrorCode);
    }
}
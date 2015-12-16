package com.maxmind.geoip2.matchers;

import com.maxmind.geoip2.exception.HttpException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class HttpStatusMatcher extends TypeSafeMatcher<HttpException> {

    private int foundStatusCode;
    private final int expectedStatusCode;

    public static HttpStatusMatcher hasStatus(int item) {
        return new HttpStatusMatcher(item);
    }

    private HttpStatusMatcher(int expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }

    @Override
    protected boolean matchesSafely(final HttpException exception) {
        this.foundStatusCode = exception.getHttpStatus();
        return this.foundStatusCode == this.expectedStatusCode;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(String.valueOf(this.foundStatusCode))
                .appendText(" was not found instead of ")
                .appendValue(String.valueOf(this.expectedStatusCode));
    }
}

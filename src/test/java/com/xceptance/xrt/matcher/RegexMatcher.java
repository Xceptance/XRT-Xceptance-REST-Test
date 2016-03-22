package com.xceptance.xrt.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by patrick on 3/22/16.
 */
public class RegexMatcher extends TypeSafeMatcher<String> {

    private final String regex;

    private RegexMatcher(String regex) {
        this.regex = regex;
    }

    @Override
    protected boolean matchesSafely(final String s) {
        return s.matches(regex);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matches regular expression='" + regex + "'");
    }

    public static RegexMatcher matchesRegex(final String regex) {
        return new RegexMatcher(regex);
    }
}

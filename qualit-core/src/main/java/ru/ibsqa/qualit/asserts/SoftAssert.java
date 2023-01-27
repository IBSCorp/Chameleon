package ru.ibsqa.qualit.asserts;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

@Deprecated
public class SoftAssert{
    public static <T> void softAssertThat(T actual, Matcher<? super T> matcher) {
        softAssertThat("", actual, matcher);
    }

    public static <T> void softAssertThat(String reason, T actual, Matcher<? super T> matcher) {
        if (!matcher.matches(actual)) {
            Description description = new StringDescription();
            description.appendText(reason)
                    .appendText("\nExpected: ")
                    .appendDescriptionOf(matcher)
                    .appendText("\n     but: ");
            matcher.describeMismatch(actual, description);
            // TODO: addError(new AssertionError(description));
        }
    }

    public static void softAssertThat(String reason, boolean assertion) {
        if (!assertion) {
            // TODO: collector.addError(new AssertionError(reason));
        }
    }
}

package ru.ibsqa.chameleon.steps;

import io.cucumber.java.ru.Когда;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Шаги для сравнения фрагментов JSON
 */
public class JsonCompareStorySteps extends AbstractSteps {

    @Autowired
    private JsonCompareSteps jsonCompareSteps;

    @Когда("^json \"([^\"]*)\" совпадает с \"([^\"]*)\"$")
    public void jsonEquals(
            String actual,
            String expected) {
        flow(()->
                jsonCompareSteps.jsonEquals(actual, expected)
        );
    }

    @Когда("^json \"([^\"]*)\" содержит \"([^\"]*)\"$")
    public void jsonExtends(
            String actual,
            String expected) {
        flow(()->
                jsonCompareSteps.jsonExtends(actual, expected)
        );
    }

    @Когда("^json \"([^\"]*)\" совпадает с \"([^\"]*)\", порядок в массивах совпадает$")
    public void jsonEqualsAndOrder(
            String actual,
            String expected) {
        flow(()->
                jsonCompareSteps.jsonEqualsAndOrder(actual, expected)
        );
    }

    @Когда("^json \"([^\"]*)\" содержит \"([^\"]*)\", порядок в массивах совпадает$")
    public void jsonExtendsAndOrder(
            String actual,
            String expected) {
        flow(()->
                jsonCompareSteps.jsonExtendsAndOrder(actual, expected)
        );
    }

    @Когда("^json \"([^\"]*)\" не совпадает с \"([^\"]*)\"$")
    public void jsonNotEquals(
            String actual,
            String expected) {
        flow(()->
                jsonCompareSteps.jsonNotEquals(actual, expected)
        );
    }

    @Когда("^json \"([^\"]*)\" не содержит \"([^\"]*)\"$")
    public void jsonNotExtends(
            String actual,
            String expected) {
        flow(()->
                jsonCompareSteps.jsonNotExtends(actual, expected)
        );
    }

}

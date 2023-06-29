package ru.ibsqa.chameleon.steps;

import ru.ibsqa.chameleon.converters.Variable;
import org.jbehave.core.annotations.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Шаги для сравнения фрагментов JSON
 */
@Component
public class JsonCompareStorySteps extends AbstractSteps {

    @Autowired
    private JsonCompareSteps jsonCompareSteps;

    @Then("json \"$actual\" совпадает с \"$expected\"")
    public void jsonEquals(Variable actual, Variable expected) {
        flow(()->
            jsonCompareSteps.jsonEquals(actual.getValue(), expected.getValue())
        );
    }

    @Then("json \"$actual\" содержит \"$expected\"")
    public void jsonExtends(Variable actual, Variable expected) {
        flow(()->
            jsonCompareSteps.jsonExtends(actual.getValue(), expected.getValue())
        );
    }

    @Then("json \"{actual}\" совпадает с \"{expected}\", порядок в массивах совпадает")
    public void jsonEqualsAndOrder(Variable actual, Variable expected) {
        flow(()->
            jsonCompareSteps.jsonEqualsAndOrder(actual.getValue(), expected.getValue())
        );
    }

    @Then("json \"{actual}\" содержит \"{expected}\", порядок в массивах совпадает")
    public void jsonExtendsAndOrder(Variable actual, Variable expected) {
        flow(()->
                jsonCompareSteps.jsonExtendsAndOrder(actual.getValue(), expected.getValue())
        );
    }

    @Then("json \"$actual\" не совпадает с \"$expected\"")
    public void jsonNotEquals(Variable actual, Variable expected) {
        flow(()->
                jsonCompareSteps.jsonNotEquals(actual.getValue(), expected.getValue())
        );
    }

    @Then("json \"$actual\" не содержит \"$expected\"")
    public void jsonNotExtends(Variable actual, Variable expected) {
        flow(()->
                jsonCompareSteps.jsonNotExtends(actual.getValue(), expected.getValue())
        );
    }

}

package ru.ibsqa.qualit.steps;

import ru.ibsqa.qualit.converters.Variable;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlowStorySteps extends AbstractSteps {

    @Autowired
    private CoreFieldSteps fieldSteps;

    @When("значение поля \"$fieldName\" равно \"$value\", выполнять следующие шаги:")
    public void ifFlowFieldValueEquals(String fieldName, Variable value) {
        if (getStepFlow().prepareFlowStep()) {
            String actual = fieldSteps.prepareValue(fieldSteps.getFieldValue(fieldName));
            String expected = fieldSteps.prepareValue(value.getValue());
            getStepFlow().createBlock(actual.equals(expected));
        }
    }

    @When("значение поля \"$fieldName\" не равно \"$value\", выполнять следующие шаги:")
    public void ifFlowFieldValueNotEquals(String fieldName, Variable value) {
        if (getStepFlow().prepareFlowStep()) {
            String actual = fieldSteps.prepareValue(fieldSteps.getFieldValue(fieldName));
            String expected = fieldSteps.prepareValue(value.getValue());
            getStepFlow().createBlock(!actual.equals(expected));
        }
    }

    @When("значение выражения \"$variable\" равно \"$value\", выполнять следующие шаги:")
    public void ifFlowVariableValueEquals(Variable variable, Variable value) {
        if (getStepFlow().prepareFlowStep()) {
            String actual = fieldSteps.prepareValue(variable.getValue());
            String expected = fieldSteps.prepareValue(value.getValue());
            getStepFlow().createBlock(actual.equals(expected));
        }
    }

    @When("значение выражения \"$variable\" не равно \"$value\", выполнять следующие шаги:")
    public void ifFlowVariableValueNotEquals(Variable variable, Variable value) {
        if (getStepFlow().prepareFlowStep()) {
            String actual = fieldSteps.prepareValue(variable.getValue());
            String expected = fieldSteps.prepareValue(value.getValue());
            getStepFlow().createBlock(!actual.equals(expected));
        }
    }

    @Then("иначе, выполнять следующие шаги:")
    public void elseFlow() {
        getStepFlow().inverseBlock();
    }

    @Then("конец условия")
    public void endIfFlow() {
        getStepFlow().completeBlock();
    }

}

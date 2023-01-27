package ru.ibsqa.qualit.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.ibsqa.qualit.elements.web.TextInput;

import java.util.List;

public class ValidationInput extends TextInput {

    @Override
    public String getErrorMsg() {
        List<WebElement> elements = findElements(By.xpath("../small"));
        if (elements.size()>0) {
            return elements.get(0).getText();
        }
        return null;
    }

}

package ru.ibsqa.qualit.uia.driver;

import bsh.EvalError;
import bsh.Interpreter;
import ru.ibsqa.qualit.elements.uia.UiaElement;
import ru.ibsqa.qualit.elements.uia.UiaElementType;
import ru.ibsqa.qualit.uia.search_context.UiaSearchContext;
import ru.ibsqa.qualit.uia.search_context.UiaTableRowSearchContext;
import lombok.extern.slf4j.Slf4j;
import mmarquee.automation.ControlType;
import mmarquee.automation.Element;
import mmarquee.automation.ElementNotFoundException;
import mmarquee.automation.controls.AutomationBase;
import mmarquee.automation.controls.Container;
import mmarquee.automation.controls.DataGridCell;
import mmarquee.automation.controls.ElementBuilder;
import mmarquee.uiautomation.TreeScope;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@Component
public class UiaSearchBy {

    public UiaElement create(UiaSearchContext searchContext, By by) {
        String locator = by.toString().replace("By.xpath: ", "");
        return create(searchContext, locator);
    }

    public UiaElement create(UiaSearchContext searchContext, ElementLocator elementLocator) {
        String locator = elementLocator.toString();

        Pattern typeExtractor = Pattern.compile("^([^']+)\\s*'By\\.xpath\\:\\s+(.*)'$");
        Matcher matcher = typeExtractor.matcher(locator);

        if (matcher.find()) {
            locator = matcher.group(2).trim();
            return create(searchContext, locator);
        } else {
            return null;
        }
    }

    public UiaElement create(UiaSearchContext searchContext, String locator) throws NoSuchElementException {
        UiaDriver driver = searchContext.getUiaDriver();
        try {
            AutomationBase automationBase = search(searchContext, locator, driver.getImplicitlywait());
            return new UiaElement((searchContext instanceof UiaTableRowSearchContext) ? UiaElementType.TABLE_CELL : null, automationBase, automationBase.getElement(), driver);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NoSuchElementException(String.format("В контексте [%s] не найден элемент с локатором [%s]", searchContext.getClass().getName(), locator));
            //fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }
    }

    private Pattern typeExtractor = Pattern.compile("(?s)^(\\w+)\\s*=\\s*(.*)$");

    private AutomationBase search(UiaSearchContext searchContext, String locator, long timeout) throws Exception {
        long maxWaitTime = System.currentTimeMillis() + timeout * 1000;
        Exception exception = null;

        Matcher matcher = typeExtractor.matcher(locator);
        boolean find = matcher.find();

        do {
            try {

                if (searchContext instanceof UiaDriver) {

                    UiaDriver driver = (UiaDriver) searchContext;
                    Container container = driver.getAutomationWindow();

                    if (!find) {

                        Interpreter interpreter = new Interpreter();
                        try {
                            locator = locator.replaceAll("'", "\"");
                            interpreter.set("automationContainer", container);
                            interpreter.set("locator", new Locator(container));
                            String statement = locator.startsWith("locator") ? locator : "automationContainer." + locator;
                            Object result = interpreter.eval(statement);
                            AutomationBase base = null;

                            if (result instanceof Locator) {
                                base = ((Locator) result).get();
                            } else {
                                base = (AutomationBase) result;
                            }
                            if (Objects.isNull(base)) {
                                throw new ElementNotFoundException(locator);
                            }
                            return base;
                        } catch (EvalError evalError) {
                            exception = evalError;
                            try {
                                Thread.sleep(101);
                            } catch (InterruptedException e) {
                                log.error(e.getMessage(), e);
                            }
                        }

                    } else {
                        String type = matcher.group(1).trim();
                        String expr = matcher.group(2).replaceFirst("(?s)^\\s*", "");

                        if (type.equalsIgnoreCase("AutomationId")) { // AutomationId=*
                            return container.getControlByAutomationId(expr);
                        } else if (type.equalsIgnoreCase("Name")) { // Name=*;СontrolType=*
                            String[] parts = expr.split(";ControlType=");
                            if (parts.length > 1) {
                                final String name = parts[0];
                                final ControlType controlType = ControlType.valueOf(parts[1]);
                                return container.getControlByControlType(name, controlType);
                            } else { // Name=*
                                return container.getControlByName(expr);
                            }
                        }
                    }

                } else if (searchContext instanceof UiaTableRowSearchContext) {

                    boolean locatorAccepted = false;
                    if (find) {
                        String type = matcher.group(1).trim();
                        String expr = matcher.group(2).trim();
                        UiaTableRowSearchContext tableRowSearchContext = (UiaTableRowSearchContext) searchContext;
                        if (type.equalsIgnoreCase("Name")) { // Name=*
                            locatorAccepted = true;
                            for (Element col : tableRowSearchContext.getRow().findAll(new TreeScope(TreeScope.CHILDREN), tableRowSearchContext.getUiaDriver().getAutomation().createTrueCondition())) {
                                DataGridCell cell = new DataGridCell(new ElementBuilder(col));
                                if (cell.getName().equals(expr)) {
                                    return cell;
                                }
                            }
                        } else if (type.equalsIgnoreCase("Index")) { // Index=*
                            locatorAccepted = true;
                            int index = 0;
                            try {
                                index = Integer.parseInt(expr);
                            } catch (NumberFormatException ne) {
                                fail(String.format("Локатор [%s] должен содержать число", locator));
                            }
                            Element col = null;
                            try {
                                col = tableRowSearchContext.getRow().findAll(new TreeScope(TreeScope.CHILDREN), tableRowSearchContext.getUiaDriver().getAutomation().createTrueCondition()).get(index);
                            } catch (IndexOutOfBoundsException ie) {
                                throw new ElementNotFoundException(String.format("Column [index=%d] not found", index));
                            }
                            return new DataGridCell(new ElementBuilder(col));
                        }
                    }

                    if (!locatorAccepted) {
                        fail(String.format("Локатор [%s] не поддерживается для поиска ячеек таблицы", locator));
                    }
                }

            } catch (ElementNotFoundException ignore) {
                exception = ignore;
                try {
                    Thread.sleep(101);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        } while (System.currentTimeMillis() < maxWaitTime);

        if (Objects.nonNull(exception)) {
            throw exception;
        } else {
            throw new ElementNotFoundException(locator);
        }

    }

}

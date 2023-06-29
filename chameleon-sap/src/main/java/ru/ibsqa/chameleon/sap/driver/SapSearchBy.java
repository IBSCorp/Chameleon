package ru.ibsqa.chameleon.sap.driver;

import ru.ibsqa.chameleon.sap.elements.SapElement;
import ru.ibsqa.chameleon.sap.elements.SapElementType;
import ru.ibsqa.chameleon.sap.search_context.SapSearchContext;
import ru.ibsqa.chameleon.sap.search_context.SapTableRowSearchContext;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Variant;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SapSearchBy {

    List<SapNode> sapNodeList = new ArrayList<>();

    public SapElement create(SapSearchContext searchContext, By by) {
        String locator = by.toString().replace("By.xpath: ", "");
        return create(searchContext, locator);
    }

    public SapElement create(SapSearchContext searchContext, ElementLocator elementLocator) {
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

    public SapElement create(SapSearchContext searchContext, String locator) throws NoSuchElementException {
        SapDriver driver = searchContext.getSapDriver();
        try {
            Variant variant = search(searchContext, locator, driver.getConfiguration().getImplicitlyWait());
            return new SapElement((searchContext instanceof SapTableRowSearchContext) ? SapElementType.TABLE_CELL : null, variant, driver);

            // return new SapElement(variant, driver);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NoSuchElementException(String.format("В контексте [%s] не найден элемент с локатором [%s]", searchContext.getClass().getName(), locator));
            //Assert.fail(Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
        }
    }


    Pattern typeExtractor = Pattern.compile("(.*)=\\s*(.*);ControlType=\\s*(.*)");

    Pattern typeCollectionExtractor = Pattern.compile("(?s)^(\\w+)\\s*=\\s*(.*)$");

    private Variant search(SapSearchContext searchContext, String locator, int timeout) {
        long maxWaitTime = System.currentTimeMillis() + timeout * 1000;
        Matcher matcher = typeExtractor.matcher(locator);
        boolean find = matcher.find();
        SapDriver driver = searchContext.getSapDriver();
        do {
            if (searchContext instanceof SapDriver) {
                if (find) {
                    String property = matcher.group(1);
                    String value = matcher.group(2);
                    String type = matcher.group(3);
                    int position = 0;
                    if (type.contains("Position")) {
                        position = Integer.parseInt(type.replaceAll(".*Position=", ""));
                        type = type.replaceAll(";Position.*", "");
                    }
                    Variant mainWindowVariant = driver.getVariant();
                    ActiveXComponent mainWindow = driver.getSession();
                    walk(mainWindowVariant, mainWindow, 0);
                    for (int i = 0; i < sapNodeList.size(); i++) {
                        if (property.equalsIgnoreCase("text") && sapNodeList.get(i).text.equals(value) && sapNodeList.get(i).type.equals(type)) {
                            Variant result = sapNodeList.get(i + position).variant;
                            sapNodeList.clear();
                            return result;
                        }else if (property.equalsIgnoreCase("toolTip") && sapNodeList.get(i).toolTip.contains(value) && sapNodeList.get(i).type.equals(type)){
                            Variant result = sapNodeList.get(i + position).variant;
                            sapNodeList.clear();
                            return result;
                        }else if (property.equalsIgnoreCase("name") && sapNodeList.get(i).name.contains(value) && sapNodeList.get(i).type.equals(type)){
                            Variant result = sapNodeList.get(i + position).variant;
                            sapNodeList.clear();
                            return result;
                        }
                    }
                }
                String[] parts = locator.split("\\|");
                for (String part : parts) {
                    try {
                        return driver.getSession().invoke("findById", part);
                    } catch (Exception ignored) {

                    }
                }

            } else if (searchContext instanceof SapTableRowSearchContext) {
                matcher = typeCollectionExtractor.matcher(locator);
                find = matcher.find();
                if (find) {
                    String type = matcher.group(1).trim();
                    String expr = matcher.group(2).replaceFirst("(?s)^\\s*", "");
                    SapTableRowSearchContext tableRowSearchContext = (SapTableRowSearchContext) searchContext;
                    if (type.equalsIgnoreCase("Name")) {
                        String[] parts = expr.split(";ControlType=");
                        if (parts.length > 1) {
                            final String name = parts[0];
                            final String controlType = parts[1];
                            int count = Integer.parseInt(tableRowSearchContext.getRow().invoke("count").toString());
                            for (int i = 0; i < count; i++) {
                                if (new ActiveXComponent(tableRowSearchContext.getRow().invoke("item", i).toDispatch()).getProperty("Name").toString().equals(name)) {
                                    return tableRowSearchContext.getRow().invoke("item", i);
                                }
                            }
                        }
                    }
                } else {
                    if (locator.equals(".")) {
                        return ((SapTableRowSearchContext) searchContext).getVariant();
                    }
                    return driver.getSession().invoke("findById", locator);
                }
            }


        } while (System.currentTimeMillis() < maxWaitTime);
        throw new NoSuchElementException("Не найден элемент с локатором:" + locator);
    }

    private void walk(Variant variant, ActiveXComponent element, int level) {
        String name = element.getProperty("Name").toString();
        String type = element.getProperty("Type").toString();
        String text = "";
        try {
            text = element.getProperty("Text").toString();
        } catch (ComFailException e) {
            //  e.printStackTrace();
        }
        String toolTip = "";
        try {
            toolTip = element.getProperty("ToolTip").toString();
        } catch (ComFailException e) {
            //  e.printStackTrace();
        }

        SapNode sapNode = new SapNode(name,  type, toolTip, text, variant, element);
        sapNodeList.add(sapNode);
        int countChildrens;
        ActiveXComponent childrens = element;
        try {
            childrens = new ActiveXComponent(element.invoke("Children").toDispatch());
            countChildrens = Integer.parseInt(childrens.invoke("count").toString());
        } catch (Exception e) {
            countChildrens = 0;
        }
        for (int i = 0; i < countChildrens; i++) {
            Variant childVariant = childrens.invoke("item", i);
            ActiveXComponent child = new ActiveXComponent(childVariant.toDispatch());
            walk(childVariant, child, level + 1);
        }
    }


    class SapNode {
        private String name;
        private String type;
        private String text;
        private Variant variant;
        private String toolTip;
        private ActiveXComponent activeXComponent;

        SapNode(String name, String type, String toolTip, String text, Variant variant, ActiveXComponent activeXComponent) {
            this.name = name;
            this.type = type;
            this.text = text;
            this.toolTip = toolTip;
            this.activeXComponent = activeXComponent;
            this.variant = variant;
        }
    }

}

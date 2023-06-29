package ru.ibsqa.chameleon.uia.driver;

import mmarquee.automation.AutomationException;
import mmarquee.automation.ControlType;
import mmarquee.automation.controls.AutomationBase;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Locator {

    public static Locator NOT_FOUND = new Locator(null);

    private AutomationBase control;
    private boolean deep;

    public Locator(AutomationBase control) {
        this.control = control;
        this.deep = false;
    }

    public static Locator of(AutomationBase control) {
        return new Locator(control);
    }

    public Locator deep() {
        Locator newLocator = new Locator(this.get());
        newLocator.deep = true;
        return newLocator;
    }

    public AutomationBase get() {
        return control;
    }

    public Locator byName(String name) {
        return byName(name, null);
    }

    public Locator byRegexpName(String regexp) {
        return byRegexpName(regexp, null);
    }

    public Locator byName(String name, String controlType) {
        if (Objects.nonNull(name)) {
            return childByFunction(control -> {
                try {
                    return name.equals(control.getName()) &&
                            (Objects.isNull(controlType) || ControlType.fromValue(control.getElement().getControlType()).toString().equals(controlType));
                } catch (AutomationException e) {
                    return false;
                }
            });
        } else {
            return NOT_FOUND;
        }
    }

    public Locator byRegexpName(String regexp, String controlType) {
        if (Objects.nonNull(regexp)) {
            return childByFunction(control -> {
                try {
                    Pattern p = Pattern.compile(regexp);
                    Matcher m = p.matcher(control.getName());
                    return m.find() &&
                            (Objects.isNull(controlType) || ControlType.fromValue(control.getElement().getControlType()).toString().equals(controlType));
                } catch (AutomationException e) {
                    return false;
                }
            });
        } else {
            return NOT_FOUND;
        }
    }

    public Locator byAutomationId(String automationId) {
        return byAutomationId(automationId, null);
    }

    public Locator byAutomationId(String automationId, String controlType) {
        if (Objects.nonNull(automationId)) {
            return childByFunction(control -> {
                try {
                    return automationId.equals(control.getElement().getAutomationId()) &&
                            (Objects.isNull(controlType) || ControlType.fromValue(control.getElement().getControlType()).toString().equals(controlType));
                } catch (AutomationException e) {
                    return false;
                }
            });
        } else {
            return NOT_FOUND;
        }
    }

    public Locator byClassName(String className) {
        return byClassName(className, null);
    }

    public Locator byClassName(String className, String controlType) {
        if (Objects.nonNull(className)) {
            return childByFunction(control -> {
                try {
                    return control.getClassName().equals(className) &&
                            (Objects.isNull(controlType) || ControlType.fromValue(control.getElement().getControlType()).toString().equals(controlType));
                } catch (AutomationException e) {
                    return false;
                }
            });
        } else {
            return NOT_FOUND;
        }
    }

    public Locator byControlType(String controlType) {
        if (Objects.nonNull(controlType)) {
            return childByFunction(control -> {
                try {
                    return ControlType.fromValue(control.getElement().getControlType()).toString().equals(controlType);
                } catch (AutomationException e) {
                    return false;
                }
            });
        } else {
            return NOT_FOUND;
        }
    }

    public Locator byIndex(int index) {
        if (Objects.isNull(control)) {
            return NOT_FOUND;
        }

        try {
            return new Locator(control.getChildren(deep).get(index));
        } catch (AutomationException e) {
            return NOT_FOUND;
        }
    }

    /*
    public Locator any(Function<Locator, Locator>... functions) {
        Locator locator = this;
        return Arrays.stream(functions)
                .map(function -> function.apply(locator))
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }
    */

    public Locator or(Locator... locators) {
        return Arrays.stream(locators)
                .filter(l -> Objects.nonNull(l.get()))
                //.filter(obj -> obj instanceof Locator)
                //.map(obj -> (Locator)obj)
                .findAny()
                .orElse(NOT_FOUND);
    }

    public Locator any(Locator locator1, Locator locator2) {
        return or(locator1, locator2);
    }

    public Locator any(Locator locator1, Locator locator2, Locator locator3) {
        return or(locator1, locator2, locator3);
    }

//    public Locator or(Locator... locators) {
//        Locator res = null;
//        if (locators.length > 0) {
//            AutomationBase ab = null;
//            for (Locator l : locators) {
//                try {
//                    ab = l.get();
//                } catch (NullPointerException ex) {
//                    continue;
//                }
//                res = Locator.of(ab);
//                break;
//            }
//        }
//        return res;
//    }

    private Locator childByFunction(Function<AutomationBase, Boolean> function) {
        try {
            if (Objects.isNull(control)) {
                return NOT_FOUND;
            }
            return control.getChildren(deep)
                    .stream()
                    .filter(function::apply)
                    .findAny()
                    .map(Locator::new)
                    .orElse(NOT_FOUND);
        } catch (AutomationException e) {
            return NOT_FOUND;
        }
    }


    @Override
    public boolean equals(Object e) {
        if (e instanceof Locator) {
            Locator l = (Locator) e;
            if (Objects.isNull(this.control)) {
                return Objects.isNull(l.get());
            }
            return this.control.equals(l.get());
        }
        return false;
    }

}

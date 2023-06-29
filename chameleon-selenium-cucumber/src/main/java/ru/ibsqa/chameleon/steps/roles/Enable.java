package ru.ibsqa.chameleon.steps.roles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@RoleParam
@RoleElement
public @interface Enable {
    String[] value() default {};
}

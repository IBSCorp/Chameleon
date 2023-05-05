package ru.ibsqa.qualit.steps.roles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@RoleParam
public @interface Operator {
    String[] value() default {};
}

package ru.ibsqa.qualit.steps;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StepDescription {
    String action()  default "";
    String subAction()  default "";
    String[] parameters()  default "";
    boolean expertView() default false;
    boolean multiple() default false;
    boolean defaultFromCombobox() default false;
}

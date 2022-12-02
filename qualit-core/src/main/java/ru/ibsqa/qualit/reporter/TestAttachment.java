package ru.ibsqa.qualit.reporter;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestAttachment {
    String value() default "";

    String mimeType() default "";

    String extension() default "";
}

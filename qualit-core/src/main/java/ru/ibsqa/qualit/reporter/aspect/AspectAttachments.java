package ru.ibsqa.qualit.reporter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.ibsqa.qualit.reporter.IReporterManager;
import ru.ibsqa.qualit.reporter.TestAttachment;
import ru.ibsqa.qualit.utils.aspect.AspectUtils;
import ru.ibsqa.qualit.utils.spring.SpringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Aspect
@Slf4j
public class AspectAttachments {

    private final IReporterManager reporterManager = SpringUtils.getBean(IReporterManager.class);

    // ---------------------------------------------Pointcuts-----------------------------------------------------------
    @Pointcut("execution(* *(..)) && !execution(* org.springframework..*(..))")
    public void anyMethod() {
        //pointcut body, should be empty
    }

    @Pointcut("@annotation(ru.ibsqa.qualit.reporter.TestAttachment)")
    public void testAttachment() {
        //pointcut body, should be empty
    }

    @AfterReturning(
            pointcut = "anyMethod() && testAttachment()",
            returning = "data")
    public void testAttachmentAfterReturning(JoinPoint joinPoint, Object data) throws IOException {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        TestAttachment testAttachment = methodSignature.getMethod().getAnnotation(TestAttachment.class);
        String message = testAttachment.value().isEmpty()
                ? methodSignature.getName()
                : AspectUtils.getSubstitutedString(testAttachment.value(), joinPoint);
        if (data instanceof byte[]) {
            reporterManager.createAttachment(message, (byte[])(data), testAttachment.mimeType(), testAttachment.extension());
        } else if (data instanceof InputStream) {
            reporterManager.createAttachment(message, (InputStream)(data), testAttachment.mimeType(), testAttachment.extension());
        } else {
            reporterManager.createAttachment(message, Objects.toString(data).getBytes(StandardCharsets.UTF_8), testAttachment.mimeType(), testAttachment.extension());
        }
    }

}

package ru.ibsqa.chameleon.utils.aspect;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.StringSubstitutor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import ru.ibsqa.chameleon.steps.aspect.ParamExtraction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class AspectUtils {

    public static String getSubstitutedString(String value, JoinPoint joinPoint) {
        return getSubstitutedString(value, getParamsMap(joinPoint));
    }

    public static String getSubstitutedString(String value, Map<String, Object> params) {
        return (new StringSubstitutor(params)).replace(value);
    }

    public static List<ParamExtraction> getParamsList(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        return IntStream.range(0, args.length).mapToObj(
                i ->
                        ParamExtraction.builder()
                                .key(signature.getParameterNames()[i])
                                .value(Optional.ofNullable(args[i]).orElse(StringUtils.EMPTY))
                                .build()
        ).collect(Collectors.toList());
    }

    public static Map<String, Object> getParamsMap(JoinPoint joinPoint) {
        return getParamsList(joinPoint)
                .stream()
                .collect(Collectors.toMap(ParamExtraction::getKey, ParamExtraction::getValue));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> Optional<T> getAnnotation(JoinPoint joinPoint, Class<T> annotationType) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[] annotations = method.getDeclaredAnnotations();

        return Stream.of(annotations)
                .filter(a -> a.annotationType().equals(annotationType))
                .map(a -> (T) a)
                .findAny();
    }

    public static Optional<String> extractAnnotationValue(JoinPoint joinPoint, Pattern pattern) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[] annotations = method.getDeclaredAnnotations();

        return Stream.of(annotations)
                .map(a -> {
                    Matcher matcher = pattern.matcher(StringEscapeUtils.unescapeJava(a.toString()));
                    if (matcher.find()) {
                        String value = matcher.group(2);
                        if (StringUtils.isEmpty(value)) {
                            return null;
                        }
                        return value.replaceAll("\\\\\"", "\"");
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findAny();
    }

}

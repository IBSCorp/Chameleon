package ru.ibsqa.chameleon.utils.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectionUtils {
    private static final Pattern CLASS_PATTERN = Pattern.compile("^(.*)\\..+\\(.*\\)$");

    public static Optional<Method> getMethodBySignature(String signature) {
        if (Objects.nonNull(signature)) {
            Matcher matcher = CLASS_PATTERN.matcher(signature);
            if (matcher.find()) {
                String classSignature = matcher.group(1);
                try {
                    Class<?> clazz = Class.forName(classSignature);
                    return Arrays.stream(clazz.getMethods())
                            .filter(method -> method.toGenericString().endsWith(signature))
                            .findAny();
                } catch (ClassNotFoundException e) {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

}

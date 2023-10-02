package ru.ibsqa.chameleon.utils.error;

public class ExceptionUtils {

    private static class ThrowableWrapper extends Throwable {

        private final Throwable throwable;

        public ThrowableWrapper(Throwable throwable) {
            super();
            this.throwable = throwable;
        }

        @SuppressWarnings("unchecked")
        public <T extends Throwable> T throwNested() throws T {
            throw (T) throwable;
        }
    }

    private static <T extends Throwable> T throwThis(T throwable) throws T {
        throw throwable;
    }

    public static <T extends Throwable> void throwUnchecked(T throwable) {
        new ThrowableWrapper(throwable).throwNested();
    }

}
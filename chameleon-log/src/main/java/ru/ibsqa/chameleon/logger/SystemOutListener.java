package ru.ibsqa.chameleon.logger;

public class SystemOutListener extends AbstractSystemLogListener {

    public SystemOutListener() {
        super(System.out, false);
    }

}

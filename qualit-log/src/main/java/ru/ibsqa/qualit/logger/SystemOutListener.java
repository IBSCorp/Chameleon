package ru.ibsqa.qualit.logger;

public class SystemOutListener extends AbstractSystemLogListener {

    public SystemOutListener() {
        super(System.out, false);
    }

}

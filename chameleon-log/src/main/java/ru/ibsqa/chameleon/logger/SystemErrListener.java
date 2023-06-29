package ru.ibsqa.chameleon.logger;

public class SystemErrListener extends AbstractSystemLogListener {

    public SystemErrListener() {
        super(System.err, true);
    }

}

package ru.ibsqa.qualit.logger;

public class SystemErrListener extends AbstractSystemLogListener {

    public SystemErrListener() {
        super(System.err, true);
    }

}

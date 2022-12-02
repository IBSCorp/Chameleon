package ru.ibsqa.qualit.configuration.suites;


public enum ConfigLevelEnum implements Comparable<ConfigLevelEnum>{
    //порядок указывает приоритет выполнения
    TASKTRACKER,
    COMMAND_LINE,
    CONFIG_FILE,
    ;
}

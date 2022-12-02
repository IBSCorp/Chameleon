package ru.ibsqa.qualit.integration;

public enum TaskStatus {
    PASSED("ПРОЙДЕН"), FAILED("НЕ ПРОЙДЕН"), IN_PROGRESS("В ПРОЦЕССЕ");
    private String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TaskStatus getStatus(String status){
        for (TaskStatus taskStatus: TaskStatus.values()){
            if (taskStatus.getValue().equals(status)){
                return taskStatus;
            }
        }
        return null;
    }

    public static String getExpectedStatus(){
        String result = "";
        for (TaskStatus taskStatus: TaskStatus.values()){
            if (result.isEmpty()){
                result = taskStatus.getValue() +  ";";
            } else {
                result = result + " " + taskStatus.getValue() +  ";";
            }
            result = result + " " + taskStatus.getValue() + " ";
        }
        return result;
    }

}

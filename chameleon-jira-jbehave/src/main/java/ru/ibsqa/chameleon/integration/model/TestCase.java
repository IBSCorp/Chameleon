package ru.ibsqa.chameleon.integration.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TestCase {

    public TestCase(String name){
        this.name = name;
    }

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private List<String> attachments;

    @Getter
    @Setter
    private String comment;
}

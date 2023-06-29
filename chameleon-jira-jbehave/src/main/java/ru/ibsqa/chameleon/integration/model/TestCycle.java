package ru.ibsqa.chameleon.integration.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TestCycle {

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    String version;

    @Getter
    @Setter
    List<TestCase> testCases;
}

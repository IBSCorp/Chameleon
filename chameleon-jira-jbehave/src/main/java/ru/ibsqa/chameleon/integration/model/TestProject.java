package ru.ibsqa.chameleon.integration.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TestProject{

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private TestCycle testCycle;

}

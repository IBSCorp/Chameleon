package ru.ibsqa.qualit.steps.visibility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StepVisibilityLayer {

    @Getter
    private final StepVisibilityLayer parent;

    @Getter
    private final boolean hidden;

}

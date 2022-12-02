package ru.ibsqa.qualit.context;

import ru.ibsqa.qualit.elements.IFacade;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class PickElementResult<FACADE extends IFacade,CONTEXT extends IContextObject> {

    // Найденный элемент
    @Getter @Setter
    FACADE element;

    // Контекст, где его нашли (страница)
    @Getter @Setter
    CONTEXT contextObject;

    // Менеджер контекстов, который нашел
    @Getter @Setter
    IContextManager<CONTEXT> contextManager;
}

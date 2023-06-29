package ru.ibsqa.chameleon.page_factory.pages;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Page {
    public Page(String name) {
        this.name = name;
    }

    @NonNull
    @Getter
    private Class<? extends IPageObject> asClass;

    @NonNull
    @Getter
    private String name;


    @Override
    public String toString(){
        return name;
    }
}

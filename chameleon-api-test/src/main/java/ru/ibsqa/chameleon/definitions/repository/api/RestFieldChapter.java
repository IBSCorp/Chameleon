package ru.ibsqa.chameleon.definitions.repository.api;

import ru.ibsqa.chameleon.definitions.repository.data.AbstractDataMetaFieldList;

public enum RestFieldChapter {
    PARAMS(MetaParams.class), HEADER(MetaHeader.class), BODY(MetaBody.class), COOKIE(MetaCookie.class);

    private Class<? extends AbstractDataMetaFieldList> clazz;

    <FIELDLIST extends AbstractDataMetaFieldList> RestFieldChapter (Class<FIELDLIST> clazz) {
        this.clazz = clazz;
    }

    public static RestFieldChapter getChapter(Class clazz) {
        for (RestFieldChapter chapter : RestFieldChapter.values()) {
            if (clazz.isAssignableFrom(chapter.clazz)) {
                return chapter;
            }
        }
        return null;
    }

    public static RestFieldChapter getChapter(Object object)
    {
        return RestFieldChapter.getChapter(object.getClass());
    }

}

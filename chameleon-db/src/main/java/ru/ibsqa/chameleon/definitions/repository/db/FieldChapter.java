package ru.ibsqa.chameleon.definitions.repository.db;

public enum FieldChapter {
    PARAMS(MetaParams.class), HEADER(MetaResult.class);

    private Class<? extends AbstractFieldList> clazz;

    <FIELDLIST extends AbstractFieldList> FieldChapter(Class<FIELDLIST> clazz) {
        this.clazz = clazz;
    }

    public static FieldChapter getChapter(Class clazz) {
        for (FieldChapter chapter : FieldChapter.values()) {
            if (clazz.isAssignableFrom(chapter.clazz)) {
                return chapter;
            }
        }
        return null;
    }

}

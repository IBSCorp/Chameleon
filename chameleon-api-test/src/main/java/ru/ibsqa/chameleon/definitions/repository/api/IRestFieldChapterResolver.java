package ru.ibsqa.chameleon.definitions.repository.api;

import ru.ibsqa.chameleon.definitions.repository.data.AbstractDataMetaField;

public interface IRestFieldChapterResolver {

    default RestFieldChapter getChapter(AbstractDataMetaField metaField) {
        return RestFieldChapter.getChapter(metaField.getFieldList());
    }

}

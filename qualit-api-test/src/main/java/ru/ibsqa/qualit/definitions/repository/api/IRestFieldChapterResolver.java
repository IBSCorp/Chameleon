package ru.ibsqa.qualit.definitions.repository.api;

import ru.ibsqa.qualit.definitions.repository.data.AbstractDataMetaField;

public interface IRestFieldChapterResolver {

    default RestFieldChapter getChapter(AbstractDataMetaField metaField) {
        return RestFieldChapter.getChapter(metaField.getFieldList());
    }

}

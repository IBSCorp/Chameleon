package ru.ibsqa.qualit.db.context;

import ru.ibsqa.qualit.definitions.repository.IRepositoryManager;
import ru.ibsqa.qualit.definitions.repository.db.MetaQuery;
import ru.ibsqa.qualit.elements.IFacade;
import ru.ibsqa.qualit.elements.db.DefaultResultField;
import ru.ibsqa.qualit.elements.db.IFacadeResultField;
import ru.ibsqa.qualit.evaluate.IEvaluateManager;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import lombok.Getter;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DefaultRowObject implements IRowObject {

    private MetaQuery metaQuery;

    private ResultSet resultSet;

    @Getter
    private List<IFacadeResultField> fields = new ArrayList<>();

    private IRepositoryManager repositoryManager = SpringUtils.getBean(IRepositoryManager.class);

    private IEvaluateManager evaluateManager = SpringUtils.getBean(IEvaluateManager.class);

    @Override
    public void initialize(MetaQuery metaQuery, ResultSet resultSet) {
        this.metaQuery = metaQuery;
        this.resultSet = resultSet;
    }

    @Override
    public <T extends IFacade> T getField(String fieldName) {
        return (T) getFields().stream().filter(item -> ((DefaultResultField) item).getName().equals(fieldName)).findFirst().get();
    }
}

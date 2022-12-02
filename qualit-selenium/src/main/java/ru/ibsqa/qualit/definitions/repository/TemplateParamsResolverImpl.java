package ru.ibsqa.qualit.definitions.repository;

import org.springframework.stereotype.Component;

@Component
public class TemplateParamsResolverImpl implements ITemplateParamsResolver {

    @Override
    public Object[] getParams(String name) {
        return new String[]{name};
    }

}

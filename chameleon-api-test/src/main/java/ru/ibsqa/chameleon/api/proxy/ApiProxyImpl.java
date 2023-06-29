package ru.ibsqa.chameleon.api.proxy;

import ru.ibsqa.chameleon.definitions.repository.IRepositoryManager;
import ru.ibsqa.chameleon.definitions.repository.api.MetaProxy;
import io.restassured.specification.ProxySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ApiProxyImpl implements IApiProxy {

    @Autowired
    private IRepositoryManager repositoryManager;

    @Override
    public ProxySpecification getSpecificationByProxy(String proxyName) {

        if (null != proxyName && !proxyName.isEmpty()) {
            MetaProxy proxy = repositoryManager.pickElement(proxyName, MetaProxy.class);
            ProxySpecification proxySpecification = new ProxySpecification(proxy.getHost(), proxy.getPort(), proxy.getScheme());
            if (Objects.nonNull(proxy.getUser()) && !proxy.getUser().isEmpty() &&
                    Objects.nonNull(proxy.getPassword()) && !proxy.getPassword().isEmpty()) {
                proxySpecification = proxySpecification.withAuth(proxy.getUser(), proxy.getPassword());
            }
            return proxySpecification;
        }

        return null;
    }


}

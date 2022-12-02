package ru.ibsqa.qualit.api.proxy;

import io.restassured.specification.ProxySpecification;

public interface IApiProxy {
    ProxySpecification getSpecificationByProxy(String proxyName);
}

package ru.ibsqa.chameleon.api.authentication;

import io.restassured.authentication.AuthenticationScheme;
import io.restassured.specification.AuthenticationSpecification;
import io.restassured.specification.RequestSpecification;

public interface IApiAuthentication {
    AuthenticationScheme getSchemeByCredentialName(String credentialName);
    RequestSpecification getSpecificationByCredentialName(AuthenticationSpecification authenticationSpecification, String credentialName);
}

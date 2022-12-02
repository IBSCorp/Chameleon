package ru.ibsqa.qualit.api.authentication;

import ru.ibsqa.qualit.definitions.repository.IRepositoryManager;
import ru.ibsqa.qualit.definitions.repository.api.MetaCredential;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.specification.AuthenticationSpecification;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiAuthenticationImpl implements IApiAuthentication {

    @Autowired
    private IRepositoryManager repositoryManager;

    @Override
    public AuthenticationScheme getSchemeByCredentialName(String credentialName) {
        if (null != credentialName && !credentialName.isEmpty()) {

            MetaCredential credential = repositoryManager.pickElement(credentialName, MetaCredential.class);

            switch (credential.getAuthentication()) {
                case PREEMPTIVE_BASIC:
                    return RestAssured.preemptive().basic(credential.getUser(), credential.getPassword());
                case BASIC:
                    return RestAssured.basic(credential.getUser(), credential.getPassword());
                case DIGEST:
                    return RestAssured.digest(credential.getUser(), credential.getPassword());
                case FORM:
                    return RestAssured.form(credential.getUser(), credential.getPassword());
                case CSRF:
                    return RestAssured.form(credential.getUser(), credential.getPassword(), FormAuthConfig.formAuthConfig().withAutoDetectionOfCsrf());
                case OAUTH2:
                    return RestAssured.oauth2(credential.getToken());
            }
        }

        return RestAssured.DEFAULT_AUTH;
    }

    @Override
    public RequestSpecification getSpecificationByCredentialName(AuthenticationSpecification authenticationSpecification, String credentialName) {
        if (null != authenticationSpecification && null != credentialName && !credentialName.isEmpty()) {

            MetaCredential credential = repositoryManager.pickElement(credentialName, MetaCredential.class);

            switch (credential.getAuthentication()) {
                case PREEMPTIVE_BASIC:
                    return authenticationSpecification.preemptive().basic(credential.getUser(), credential.getPassword());
                case BASIC:
                    return authenticationSpecification.basic(credential.getUser(), credential.getPassword());
                case DIGEST:
                    return authenticationSpecification.digest(credential.getUser(), credential.getPassword());
                case FORM:
                    return authenticationSpecification.form(credential.getUser(), credential.getPassword());
                case CSRF:
                    return authenticationSpecification.form(credential.getUser(), credential.getPassword(), FormAuthConfig.formAuthConfig().withAutoDetectionOfCsrf());
                case OAUTH2:
                    return authenticationSpecification.oauth2(credential.getToken());
            }
        }
        return null;
    }


}

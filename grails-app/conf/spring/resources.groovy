import com.eservice.dao.emetrics.UserDAO
import com.eservice.eumowy.CustomDateEditorRegistrar
import com.eservice.eumowy.auth.AuthRoles
import com.eservice.eumowy.auth.AuthUser
import com.eservice.eumowy.auth.DomainAuthenticationProvider
import com.eservice.eumowy.auth.FakeEUmowyAuthenticator
import com.eservice.eumowy.auth.LdapEUmowyAuthenticator
import com.eservice.eumowy.auth.microldap.MicroLDAPClientImpl
import com.eservice.eumowy.dao.CbdDAO
import com.eservice.eumowy.documents.DocumentDefinitionsConfiguration
import com.eservice.eumowy.microbisnode.MicroBisnodeClientImpl
import com.eservice.eumowy.mocks.MicroBisnodeClientMock
import com.eservice.eumowy.mocks.WebsClientMock
import com.eservice.eumowy.propEditors.CustomPropertyEditorRegistrar
import com.eservice.eumowy.sms.DefaultSmsClient
import com.eservice.eumowy.sms.FakeSmsClient
import com.eservice.eumowy.util.EumowyCustomEnvironment
import com.eservice.service.security.ECbdRoleService
import com.eservice.service.security.NoRoleService
import com.eservice.service.security.UserService
import com.eservice.webs.client.WebsClient
import grails.util.Environment
import org.jasypt.digest.config.SimpleDigesterConfig
import org.jasypt.salt.RandomSaltGenerator
import org.jasypt.util.password.ConfigurablePasswordEncryptor

import static com.eservice.eumowy.auth.AuthRoles.EUM_PH_BZOS
import static com.eservice.eumowy.auth.AuthRoles.EUM_ZRD
import static com.eservice.eumowy.auth.AuthUser.user
import static java.util.Arrays.asList

// Place your Spring DSL code here

beans = {

//    importBeans('classpath:/applicationContext.xml')
    importBeans('classpath:/applicationContextWS.xml')
    importBeans('classpath:/applicationContextWEBS.xml')

    /**
     * SPRING BEANS
     * */
    saltGenerator(RandomSaltGenerator) {}

    simpleDigesterConfig(SimpleDigesterConfig) {
        algorithm = 'SHA-1'
        iterations = '1000'
        saltGenerator = ref('saltGenerator')
        saltSizeBytes = '8'
    }

    passwordEncryptor(ConfigurablePasswordEncryptor) {
        config = ref('simpleDigesterConfig')
        stringOutputType = 'base64'
    }

    userDAO(UserDAO) {
        sessionFactory = ref('sessionFactory')
    }

    roleService(ECbdRoleService) {
        sessionFactory = ref('sessionFactory')
    }

    switch (Environment.getCurrent().getName()) {
        case Environment.DEVELOPMENT.name:
            websWebServiceClient(WebsClientMock) {}
            microBisnodeClient(MicroBisnodeClientMock, "/opt/eumowy/mock/microbisnode/") {}
            break;
        default:
            microBisnodeClient(MicroBisnodeClientImpl, '${microBisnode.service.uri}') {}
    }

    microLDAPClient(MicroLDAPClientImpl, '${microLDAP.service.uri}', '${microLDAP.service.domain}',
            '${microLDAP.service.groups}', '${microLDAP.service.department}') {}

    //TODO MK
    smsClient(DefaultSmsClient, '${smsClient.uri}', '${smsClient.sender}', '${smsClient.source}', '${smsClient.partner}')
//    smsClient(FakeSmsClient)

    userService(UserService) {
        roleService = ref('roleService')
        userDAO = ref('userDAO')
        passwordEncryptor = ref('passwordEncryptor')
        sessionFactory = ref('sessionFactory')
    }

    /**
     * LOGIN BEANS
     * */
    customPropertyEditorRegistrar(CustomPropertyEditorRegistrar) {}
    customDateEditorRegistrar(CustomDateEditorRegistrar)

    ldapAuthenticator(LdapEUmowyAuthenticator) {
        ldapClient = ref('microLDAPClient')
        userDetailsService = ref('domainUserDetailsService')
        cbd = ref('cbdService')
        messageSource = ref('messageSource')
    }

    fakeAuthenticator(FakeEUmowyAuthenticator) {
        users = asList(
                user(
                        "askonieczny",
                        "a",
                        "Andrzej",
                        "Skonieczny",
                        "test@apreel.com",
                        2L,
                        "R3101",
                        EUM_PH_BZOS
//                        EUM_ZRD
                )
        )
    }

    // custom authentication
    daoAuthenticationProvider(DomainAuthenticationProvider) {
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
//        authenticator = ref('ldapAuthenticator')
         authenticator = ref('fakeAuthenticator')
    }

    cbdDAO(CbdDAO) {
        dataSource = ref('dataSource')
    }

    documentTemplatesConfiguration(DocumentDefinitionsConfiguration)

}

import com.eservice.dao.emetrics.UserDAO
import com.eservice.eumowy.CustomDateEditorRegistrar
import com.eservice.eumowy.auth.EServiceAuthenticationProvider
import com.eservice.eumowy.dao.CbdDAO
import com.eservice.eumowy.microbisnode.MicroBisnodeClientImpl
import com.eservice.eumowy.mocks.MicroBisnodeClientMock
import com.eservice.eumowy.mocks.WebsClientMock
import com.eservice.eumowy.propEditors.CustomPropertyEditorRegistrar
import com.eservice.eumowy.util.EumowyCustomEnvironment
import com.eservice.service.security.ECbdRoleService
import com.eservice.service.security.NoRoleService
import com.eservice.service.security.UserService
import com.eservice.webs.client.WebsClient
import grails.util.Environment
import org.jasypt.digest.config.SimpleDigesterConfig
import org.jasypt.salt.RandomSaltGenerator
import org.jasypt.util.password.ConfigurablePasswordEncryptor

// Place your Spring DSL code here

beans = {

//    importBeans('classpath:/applicationContext.xml')
    importBeans('classpath:/applicationContextWS.xml')
    importBeans('classpath:/applicationContextWEBS.xml')

    /**
     * SPRING BEANS
     * */
    saltGenerator(RandomSaltGenerator){}

    simpleDigesterConfig(SimpleDigesterConfig){
        algorithm = 'SHA-1'
        iterations = '1000'
        saltGenerator = ref('saltGenerator')
        saltSizeBytes = '8'
    }

    passwordEncryptor(ConfigurablePasswordEncryptor){
        config  = ref('simpleDigesterConfig')
        stringOutputType = 'base64'
    }

    userDAO(UserDAO){
        sessionFactory = ref('sessionFactory')
    }

    roleService(ECbdRoleService){
        sessionFactory = ref('sessionFactory')
    }

    switch (Environment.getCurrent().getName()) {
        case Environment.DEVELOPMENT.name:
            websWebServiceClient(WebsClientMock){}
            microBisnodeClient(MicroBisnodeClientMock, "/opt/settings/mock/microbisnode/"){}
            break;
        default:
            microBisnodeClient(MicroBisnodeClientImpl, '${microBisnode.service.uri}'){}
    }

    userService(UserService){
        roleService = ref('roleService')
        userDAO = ref('userDAO')
        passwordEncryptor = ref('passwordEncryptor')
        sessionFactory = ref('sessionFactory')
    }

    /**
     * LOGIN BEANS
     * */
    customPropertyEditorRegistrar(CustomPropertyEditorRegistrar){}
    customDateEditorRegistrar(CustomDateEditorRegistrar)

    // custom authentication
    daoAuthenticationProvider(EServiceAuthenticationProvider) {
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
        userService = ref('userService')
        cbdService = ref('cbdService')
    }

    cbdDAO(CbdDAO){
        dataSource = ref('dataSource')
    }


}
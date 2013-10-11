import com.eservice.dao.emetrics.UserDAO
import com.eservice.eum.ws.client.AcceptUmowaWSClient
import com.eservice.eumowy.CustomDateEditorRegistrar
import com.eservice.eumowy.auth.EServiceAuthenticationProvider
import com.eservice.eumowy.dao.CbdDAO
import com.eservice.eumowy.propEditors.CustomPropertyEditorRegistrar
import com.eservice.eumowy.util.EumowyCustomEnvironment
import com.eservice.eumowy.ws.AcceptUmowaWSClientFactory
import com.eservice.service.security.ECbdRoleService
import com.eservice.service.security.NoRoleService
import com.eservice.service.security.UserService
import grails.util.Environment
import org.jasypt.digest.config.SimpleDigesterConfig
import org.jasypt.salt.RandomSaltGenerator
import org.jasypt.util.password.ConfigurablePasswordEncryptor
import org.springframework.ws.client.core.WebServiceTemplate

// Place your Spring DSL code here

beans = {

    importBeans('classpath:/applicationContext.xml')

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

    switch (Environment.getCurrent().getName()) {
        case EumowyCustomEnvironment.MOCK.getName():
            roleService(NoRoleService){}
            break;
        default:
            roleService(ECbdRoleService){
                sessionFactory = ref('sessionFactory')
            }
            break;
    }

    userService(UserService){
        roleService = ref('roleService')
        userDAO = ref('userDAO')
        passwordEncryptor = ref('passwordEncryptor')
        sessionFactory = ref('sessionFactory')
    }

    webServiceTemplate(WebServiceTemplate, ref('messageFactory')){
        defaultUri = grailsApplication.config.eumowySyncWSAddress
        marshaller = ref('marshaller')
        unmarshaller = ref('marshaller')
    }

    acceptUmowaWSClientFactory(AcceptUmowaWSClientFactory) {
        webServiceClient = ref('webServiceClient')
    }

    acceptUmowaWSClient(acceptUmowaWSClientFactory: "getInstance"){}

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
    }

    cbdDAO(CbdDAO){
        dataSource = ref('dataSource')
    }
	

}
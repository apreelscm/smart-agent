import com.eservice.dao.emetrics.UserDAO
import com.eservice.eumowy.auth.EServiceAuthenticationProvider
import com.eservice.eumowy.dao.CbdDAO
import com.eservice.eumowy.propEditors.CustomDateEditorRegistrar
import com.eservice.eumowy.propEditors.CustomPropertyEditorRegistrar
import com.eservice.service.security.ECbdRoleService
import com.eservice.service.security.NoRoleService
import com.eservice.service.security.UserService
import grails.util.Environment
import org.jasypt.digest.config.SimpleDigesterConfig
import org.jasypt.salt.RandomSaltGenerator
import org.jasypt.util.password.ConfigurablePasswordEncryptor

// Place your Spring DSL code here

beans = {

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

    switch (Environment.getCurrent()) {
        case Environment.DEVELOPMENT:
            roleService(NoRoleService){}
            break;
        case Environment.TEST:
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

    /**
     * LOGIN BEANS
     * */
    customPropertyEditorRegistrar(CustomPropertyEditorRegistrar){}

    // custom authentication
    daoAuthenticationProvider(EServiceAuthenticationProvider) {
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
        userService = ref('userService')
    }

    cbdDAO(CbdDAO){
        dataSource = ref('dataSource')
    }
	
	customDateEditorRegistrar(CustomDateEditorRegistrar)
}
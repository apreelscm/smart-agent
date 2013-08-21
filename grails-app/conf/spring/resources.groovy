import com.eservice.eumowy.auth.EServiceAuthenticationProvider
import com.eservice.eumowy.dao.CbdDAO
import com.eservice.eumowy.propEditors.CustomPropertyEditorRegistrar

// Place your Spring DSL code here


beans = {

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
}
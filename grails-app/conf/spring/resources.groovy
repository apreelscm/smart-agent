// Place your Spring DSL code here


import eservice.auth.EServiceAuthenticationProvider
import eservice.ui.EServiceFilter

beans = {


    // custom authentication
    daoAuthenticationProvider(EServiceAuthenticationProvider) {
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
        userService = ref('userService')
    }

}
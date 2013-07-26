import com.eservice.eumowy.auth.EServiceAuthenticationProvider

// Place your Spring DSL code here


beans = {
    // custom authentication
    daoAuthenticationProvider(EServiceAuthenticationProvider) {
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
        userService = ref('userService')
    }
}
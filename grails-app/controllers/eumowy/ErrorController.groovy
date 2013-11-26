package eumowy

import org.hibernate.StaleObjectStateException
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class ErrorController {
    def handle = {
        //Optimistic locking exception is handled in ActivityController(optimisticLockingHandler)
        render(view: "/error", model: [errorMessage: "Wystąpił nieoczekiwany błąd"])
    }
}
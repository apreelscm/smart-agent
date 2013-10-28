package eumowy

import org.hibernate.StaleObjectStateException

class ErrorController {
    def handle = {
        def exception = request.exception.cause.class
        if(exception == org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException || exception == StaleObjectStateException)
            render(view: "/error", model: [errorMessage: "Proces został zmodyfikowany przez innego użtykownika"])
        else
            render(view: "/error", model: [errorMessage: "Wystąpił nieoczekiwany błąd"])
    }
}
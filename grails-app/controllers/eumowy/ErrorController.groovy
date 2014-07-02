package eumowy

class ErrorController {
    def handle = {
        //Optimistic locking exception is handled in ActivityController(optimisticLockingHandler)
        render(view: "/error", model: [errorMessage: "Wystąpił nieoczekiwany błąd"])
    }
}
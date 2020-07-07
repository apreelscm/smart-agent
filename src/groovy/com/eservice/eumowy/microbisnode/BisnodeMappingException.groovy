package com.eservice.eumowy.microbisnode

/**
 * thrown when bisnode response doesn't match expected model
 */
class BisnodeMappingException extends Exception {

    BisnodeMappingException(Throwable cause){
        super(cause)
    }
}

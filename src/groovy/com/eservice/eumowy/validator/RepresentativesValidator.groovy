package com.eservice.eumowy.validator

import com.eservice.eumowy.command.RepresentativeCommand


class RepresentativesValidator {
    public static def validate = { value, cmd, errors, prefix ->
        boolean representativesHasErrors = false

        value.each { representativeCommand ->
            if(representativeCommand.name && representativeCommand.surname) {
                representativeCommand.processCommand = cmd
                representativeCommand.validate()

                if(representativeCommand.hasErrors()) {
                    representativesHasErrors = true
                }
            }
        }

        if(value.size() > 0) {
            RepresentativeCommand firstRepresentative = value[0]

            if(!(firstRepresentative.name && firstRepresentative.surname)) {
                errors.reject(String.format("%s.atleast.one.required", prefix))
                return false
            }
        }

        if(representativesHasErrors) {
            errors.reject(String.format("%s.has.errors", prefix))
            return false
        }

        return true
    }
}

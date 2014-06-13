package com.eservice.eumowy.validator

import com.eservice.eumowy.command.RepresentativeCommand


class RepresentativesValidator {
    public static def validate = { value, cmd, errors, prefix ->
        boolean representativesHasErrors

        value.each { representativeCommand ->
            representativeCommand.processCommand = cmd
            representativeCommand.validate()

            if(representativeCommand.hasErrors()) {
                representativesHasErrors = true
            }
        }

        if(value.size() > 0) {
            RepresentativeCommand firstRepresentative = value[0]

            if(!(firstRepresentative.imie && firstRepresentative.nazwisko)) {
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

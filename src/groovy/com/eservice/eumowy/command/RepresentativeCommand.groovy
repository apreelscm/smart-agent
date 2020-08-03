package com.eservice.eumowy.command

import com.eservice.eumowy.enums.options.AcceptorVerification
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.enums.options.TelephoneType
import com.eservice.eumowy.validator.CustomValidator
import com.eservice.eumowy.validator.NumberValidator
import grails.validation.Validateable
import org.apache.commons.validator.routines.EmailValidator

import static com.google.common.base.Strings.isNullOrEmpty

@Validateable(nullable = true)
class RepresentativeCommand implements Serializable{
    transient ProcessCommand processCommand

    public static final String PROCURATOR_POSITION = "Pełnomocnik"

    Long id

    String salutation
    String name
    String surname
    String position

    AcceptorVerification verification
    String pesel
    Date birthDate
    String birthCountry
    String birthCity

    IdentityDocumentType documentType
    String documentNumber
    Date documentIssueDate
    Date documentExpirationDate

    String citizenship
    String address
    String country

    Boolean isPolitician = Boolean.FALSE
    Boolean isDirectPep
    Boolean hasSignedContract = Boolean.FALSE

    String email
    String phoneNumber
    TelephoneType telephoneType

    String getMobilePhone(){
        return TelephoneType.MOBILE == telephoneType ? phoneNumber : null
    }

    String getLandlinePhone(){
        return TelephoneType.LANDLINE == telephoneType ? phoneNumber : null
    }

    void setMobilePhone(String mobilePhone){
        if (mobilePhone){
            phoneNumber = mobilePhone
            telephoneType = TelephoneType.MOBILE
        }
    }

    void setLandlinePhone(String landlinePhone){
        if (landlinePhone){
            phoneNumber = landlinePhone
            telephoneType = TelephoneType.LANDLINE
        }
    }

    boolean isProcuratorPosition(){
        return position == PROCURATOR_POSITION
    }

    static constraints = {
        salutation(nullable: true)
        name(nullable: true, shared: "lettersOnly")
        surname(nullable: true, shared: "lettersOnly")
        position(nullable: true)

        verification(nullable: true, validator: { value, cmd, errors ->
            return CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa, "verification",
                    "representative.option.required")
        })

        pesel(nullable: true, validator: {value, cmd, errors ->
            return AcceptorVerification.PESEL.equals(cmd.verification) ? NumberValidator.validatePesel(value, cmd, errors, "pesel") : true
        })

        documentType(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() || cmd.procuratorPosition, "documentType",
                    "representative.typDokumentu.required")
        })
        documentNumber(nullable: true, maxSize: 20, shared: "alphanumeric", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() || cmd.procuratorPosition,
                    "documentNumber", "representative.seriaNrDokumentu.required")
        })
        documentIssueDate(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.documentType == IdentityDocumentType.IDENTITY_CARD,
                    "documentIssueDate", "representative.dataWydaniaDokumentu.required")
        })
        documentExpirationDate(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.documentType == IdentityDocumentType.IDENTITY_CARD,
                    "documentExpirationDate", "representative.dataWaznosciDokumentu.required")
        })

        birthDate(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorVerification.BIRTH_DATE.equals(cmd.verification),
                    "birthDate", "representative.dataUrodzenia.required")
        })
        birthCountry(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.hasNewUmowa,
                    "birthCountry", "representative.krajUrodzenia.required")
        })
        birthCity(nullable: true, maxSize: 255, shared: "alphanumeric", validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.processCommand.hasNewUmowa,
                    "birthCity", "representative.miastoUrodzenia.required")
        })

        address(nullable: true, maxSize: 100, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() && cmd.processCommand.hasNewUmowa,
                    propertyName, "representative.adres.required")
        })

        country(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() && cmd.processCommand.hasNewUmowa,
                    propertyName, "representative.kraj.required")
        })

        isPolitician(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.processCommand.hasNewUmowa,
                    propertyName, "representative.czyStanowiskoPolityczne.required")
        })

        isDirectPep(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.isPolitician,
                    propertyName, "representative.czyPepBezposredni.required")
        })

        citizenship(nullable: true, maxSize: 30, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() && cmd.processCommand.hasNewUmowa,
                    "citizenship", "representative.obywatelstwo.required")
        })

        hasSignedContract(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.processCommand.hasNewUmowa,
                    "hasSignedContract", "representative.hasSignedContract.required")
        })

        email(nullable: true, blank: true, validator: { value, cmd, errors ->
            if (isNullOrEmpty(value)) return true

            if (!EmailValidator.instance.isValid(value)) {
                errors.rejectValue("email", "email.invalid", [value] as Object[], "")
                return false
            }

            return true
        })

        phoneNumber(nullable: true, maxSize: 20)
        telephoneType(nullable: true)
    }
}

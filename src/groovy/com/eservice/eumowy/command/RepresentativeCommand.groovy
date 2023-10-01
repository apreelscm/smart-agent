package com.eservice.eumowy.command


import com.eservice.eumowy.enums.options.AcceptorVerification
import com.eservice.eumowy.enums.options.IdentityDocumentType
import com.eservice.eumowy.enums.options.TelephoneType
import com.eservice.eumowy.validator.CustomValidator
import com.eservice.eumowy.validator.NumberValidator
import grails.validation.Validateable
import org.apache.commons.validator.routines.EmailValidator

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
    @Deprecated
    String birthCity

    IdentityDocumentType documentType
    String documentNumber
    Date documentIssueDate
    Date documentExpirationDate

    @Deprecated
    String address
    String citizenship
    String streetTitle
    String street
    String houseNumber
    String flatNumber
    String city
    String postalCode
    String postOffice
    String country

    Boolean isPolitician = Boolean.FALSE
    Boolean isDirectPep
    Boolean hasSignedContract
    boolean validateHasSignedContract = true
    Boolean isCBDDataChangedManually = Boolean.FALSE

    String email
    String phoneNumber
    TelephoneType telephoneType

    // dane z CBD
    String midCBD
    String legalFormCBD

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
            return CustomValidator.validateRequired(value, errors, true, "verification",
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
            CustomValidator.validateRequired(value, errors, cmd.documentType == IdentityDocumentType.IDENTITY_CARD && (cmd.processCommand.isPersonForm() || cmd.procuratorPosition),
                    "documentIssueDate", "representative.dataWydaniaDokumentu.required")
        })
        documentExpirationDate(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.documentType == IdentityDocumentType.IDENTITY_CARD && (cmd.processCommand.isPersonForm() || cmd.procuratorPosition),
                    "documentExpirationDate", "representative.dataWaznosciDokumentu.required")
        })

        birthDate(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, AcceptorVerification.BIRTH_DATE.equals(cmd.verification),
                    "birthDate", "representative.dataUrodzenia.required")
        })
        birthCountry(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, true, "birthCountry", "representative.krajUrodzenia.required")
        })

        streetTitle(nullable: true, maxSize: 4, blank: true);

        street(nullable: true, maxSize: 40, shared: "alphanumeric", validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm(),
                    propertyName, "representative.street.required")
        })

        houseNumber(nullable: true, maxSize: 6, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm(),
                    propertyName, "representative.houseNumber.required")
        })

        flatNumber(nullable: true, maxSize: 4)

        city(nullable: true, maxSize: 33, shared: "alphanumericWithBrackets", validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm(),
                    propertyName, "representative.city.required")
        })

        postalCode(nullable: true, maxSize: 6, shared: "postalCodeValidator", validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm(),
                    propertyName, "representative.postalCode.required")
        })

        postOffice(nullable: true, maxSize: 33, shared: "alphanumeric")

        country(nullable: true, validator: { value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm(),
                    propertyName, "representative.kraj.required")
        })

        isPolitician(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, true,
                    propertyName, "representative.czyStanowiskoPolityczne.required")
        })

        isDirectPep(nullable: true, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value != null, errors, cmd.isPolitician,
                    propertyName, "representative.czyPepBezposredni.required")
        })

        citizenship(nullable: true, maxSize: 30, validator: {value, cmd, errors ->
            CustomValidator.validateRequired(value, errors, cmd.processCommand.isPersonForm() ||
                    (cmd.processCommand.isCompanyForm() && cmd.isProcuratorPosition()),
                    "citizenship", "representative.obywatelstwo.required")
        })

        hasSignedContract(nullable: true, validator: {value, cmd, errors ->
            if (!cmd.validateHasSignedContract) return

            CustomValidator.validateRequired(value != null, errors, true,
                    "hasSignedContract", "representative.hasSignedContract.required")
        })

        email(nullable: true, blank: true, validator: { value, cmd, errors ->
            /*Sets.newHashSet(LegalForm.PARTNERSHIP_COMPANY.name(), LegalForm.PERSON.name()).contains(cmd.processCommand.dzialalnoscForma) && cmd.hasSignedContract*/
            if (!CustomValidator.validateRequired(value != null, errors, cmd.hasSignedContract,
                    "email", "representative.email.required")) {
                return false
            }

            if (value == null || value.size() == 0) return true

            if (!EmailValidator.instance.isValid(value)) {
                errors.rejectValue("email", "email.invalid", [value] as Object[], "")
                return false
            }

            return true
        })

        phoneNumber(nullable: true, maxSize: 20, validator: { value, cmd, errors ->
            if (!CustomValidator.validateRequired(value != null, errors, cmd.hasSignedContract,
                    "phoneNumber", "representative.phoneNumber.required")) {
                return false
            }
        })
        
        telephoneType(nullable: true, validator: { value, cmd, errors ->
            if (!CustomValidator.validateRequired(value != null, errors, cmd.hasSignedContract,
                    "telephoneType", "representative.telephoneType.required")) {
                return false
            }
        })
        isCBDDataChangedManually(nullable: true)
        midCBD(nullable: true)
    }
}

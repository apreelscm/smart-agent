package com.eservice.eumowy

import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.AcceptorVerification
import com.eservice.eumowy.enums.options.IdentityDocumentType
import org.apache.commons.logging.LogFactory

class Representative implements Serializable {
    private static final LOG = LogFactory.getLog(Representative.class)

    public enum Type{
        REPRESENTATIVE, BENEFICIARY
    }

    Type type

    String salutation
    String name
    String surname
    String position

    AcceptorVerification verification
    String pesel
    String countryCode
    Date birthDate

    AcceptorLocation locationType

    IdentityDocumentType documentType

    String documentNumber
    String citizenship
    String address
    String country

    Boolean ownsAcceptor
    Boolean controlsAcceptor
    Boolean overQuarterOfVotes
    Integer votesPercentage

    Boolean isPolitician = false

    static belongsTo = [Process]

    static mapping = {
        sort "ID"
        table name: "REPRESENTATIVE", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.REPRESENTATIVE_SEQ']

        type column: "TYPE"

        salutation column: "SALUTATION"
        name column: "NAME"
        surname column: "SURNAME"
        position column: "POSITION"

        verification column: "VERIFICATION"
        pesel column: "PESEL"
        countryCode column: "COUNTRY_CODE"
        birthDate column: "BIRTH_DATE"

        locationType column: "LOCATION_TYPE"

        documentType column: "ID_DOCUMENT_TYPE"

        documentNumber column: "ID_NUMBER"
        citizenship column: "CITIZENSHIP"
        address column: "ADDRESS"
        country column: "COUNTRY"

        ownsAcceptor column: "OWNS_ACCEPTOR"
        controlsAcceptor column: "CONTROL_ACCEPTOR"
        overQuarterOfVotes column: "OVER_QUARTER_VOTES"
        votesPercentage column: "PERCENT_VOTES"

        isPolitician column: "POLITICAL_POSITION"
    }

    static constraints = {
       salutation(nullable: true)
       name(nullable: false)
       surname(nullable: false)
       position(nullable: true)
       verification(nullable: true)
       locationType(nullable: true)
       pesel(nullable: true)
       countryCode(nullable: true)
       documentType(nullable: true)
       documentNumber(nullable: true)
       birthDate(nullable: true)
       citizenship(nullable: true)
       address(nullable: true)
       country(nullable: true)
       ownsAcceptor(nullable: true)
       controlsAcceptor(nullable: true)
       overQuarterOfVotes(nullable: true)
       votesPercentage(nullable: true)
       isPolitician(nullable: true)
    }

    public String getFullName() {
        return name + " " + surname
    }

    public boolean isRepresentative() {
        return Type.REPRESENTATIVE.equals(type)
    }

    public boolean isBeneficiary() {
        return Type.BENEFICIARY.equals(type)
    }

    def afterInsert() {
        LOG.info(String.format("Utworzono %s - %s (id: %s)", type, fullName, id))
    }

    def afterUpdate() {
        LOG.info(String.format("Zaktualizowano %s - %s (id: %s)", type, fullName, id))
    }
}

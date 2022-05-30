package com.eservice.eumowy

import org.apache.commons.logging.LogFactory

import static java.lang.String.format

class RepresentativeCbdData implements Serializable {
    private static final LOG = LogFactory.getLog(RepresentativeCbdData.class)

    String legalForm;
    String salutation
    String name
    String surname
    String position
    String pesel
    String birthCountryCode
    Date birthDate
    String birthCountry
    String documentNumber
    Date documentIssueDate
    Date documentExpirationDate
    String citizenship
    String streetTitle
    String street
    String houseNumber
    String flatNumber
    String city
    String postalCode
    String postOffice
    String country
    Boolean hasSignedContract
    String email
    String mobilePhone
    String landlinePhone

    static belongsTo = [representative: Representative]

    static mapping = {
        sort "ID"
        table name: "REPRESENTATIVE_CBD_DATA", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.REPRESENTATIVE_CBD_SEQ']
        salutation column: "SALUTATION"
        name column: "NAME"
        surname column: "SURNAME"
        position column: "POSITION"
        pesel column: "PESEL"
        birthCountryCode column: "COUNTRY_CODE"
        birthDate column: "BIRTH_DATE"
        birthCountry column: "BIRTH_COUNTRY"
        documentNumber column: "ID_NUMBER"
        documentIssueDate column: "ID_ISSUE_DATE"
        documentExpirationDate column: "ID_EXPIRATION_DATE"
        citizenship column: "CITIZENSHIP"
        streetTitle column: "STREET_TITLE"
        street column: "STREET"
        houseNumber column: "HOUSE_NUMBER"
        flatNumber column: "FLAT_NUMBER"
        city column: "CITY"
        postalCode column: "POSTAL_CODE"
        postOffice column: "POST_OFFICE"
        country column: "COUNTRY"
        hasSignedContract column: "CONTRACT_SIGNED"
        email column : "EMAIL"
        landlinePhone column : "LANDLINE_PHONE"
        mobilePhone column : "MOBILE_PHONE"
        legalForm column: "LEGAL_FORM"
    }

    static constraints = {
       salutation(nullable: true)
       name(nullable: true)
       surname(nullable: true)
       position(nullable: true)
       pesel(nullable: true)
       birthCountryCode(nullable: true)
       documentNumber(nullable: true)
       documentIssueDate(nullable: true)
       documentExpirationDate(nullable: true)
       birthDate(nullable: true)
       birthCountry(nullable: true)
       citizenship(nullable: true)
       streetTitle(nullable: true)
       street(nullable: true)
       houseNumber(nullable: true)
       flatNumber(nullable: true)
       city(nullable: true)
       postalCode(nullable: true)
       postOffice(nullable: true)
       country(nullable: true)
       hasSignedContract(nullable: true)
       email(nullable: true)
       landlinePhone(nullable: true)
       mobilePhone(nullable: true)
       legalForm(nullable: true)
    }

    def afterInsert() {
        LOG.info(format("Utworzono - %s (id: %s)", name, id))
    }

    def afterUpdate() {
        LOG.info(format("Zaktualizowano - %s (id: %s)", name, id))
    }
}

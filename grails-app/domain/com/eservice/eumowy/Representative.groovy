package com.eservice.eumowy

import com.eservice.eumowy.enums.options.AcceptorLocation
import com.eservice.eumowy.enums.options.IdentityDocumentType
import org.apache.commons.logging.LogFactory

class Representative implements Serializable {
    private static final LOG = LogFactory.getLog(Representative.class)

    public enum Type{
        REPRESENTATIVE, BENEFICIARY
    }

    Type typ

    String tytul
    String imie
    String nazwisko
    String stanowisko

    String pesel

    AcceptorLocation typLokalizacji
    String lokalizacjaPesel
    String lokalizacjaKraj

    IdentityDocumentType typDokumentu

    String seriaNrDokumentu
    Date dataUrodzenia
    String obywatelstwo
    String adres

    Boolean posiadaAkceptanta
    Boolean kontrolujeAkceptanta
    Boolean znaczaceUdzialy
    Integer procentUdzialow

    static belongsTo = [Process]

    static mapping = {
        sort "ID"
        table name: "REPRESENTATIVE", schema:DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.REPRESENTATIVE_SEQ']

        tytul column: "SALUTATION"
        imie column: "NAME"
        nazwisko column: "SURNAME"
        stanowisko column: "POSITION"

        pesel column: "PESEL"

        typLokalizacji column: "LOCATION_TYPE"
        lokalizacjaPesel column: "LOCATION_PESEL"
        lokalizacjaKraj column: "LOCATION_COUNTRY"

        typDokumentu column: "ID_DOCUMENT_TYPE"

        seriaNrDokumentu column: "ID_NUMBER"
        dataUrodzenia column: "BIRTH_DATE"
        obywatelstwo column: "CITIZENSHIP"
        adres column: "ADDRESS"

        posiadaAkceptanta column: "OWNS_ACCEPTOR"
        kontrolujeAkceptanta column: "CONTROL_ACCEPTOR"
        znaczaceUdzialy column: "OVER_QUARTER_VOTES"
        procentUdzialow column: "PERCENT_VOTES"

        typ column: "TYPE"
    }

    static constraints = {
       tytul(nullable: true)
       imie(nullable: true)
       nazwisko(nullable: true)
       stanowisko(nullable: true)
       pesel(nullable: true)
       typLokalizacji(nullable: true)
       lokalizacjaPesel(nullable: true)
       lokalizacjaKraj(nullable: true)
       typDokumentu(nullable: true)
       seriaNrDokumentu(nullable: true)
       dataUrodzenia(nullable: true)
       obywatelstwo(nullable: true)
       adres(nullable: true)
       posiadaAkceptanta(nullable: true)
       kontrolujeAkceptanta(nullable: true)
       znaczaceUdzialy(nullable: true)
       procentUdzialow(nullable: true)
    }

    public String getFullName() {
        return imie + " " + nazwisko
    }

    public boolean isRepresentative() {
        return Type.REPRESENTATIVE.equals(typ)
    }

    public boolean isBeneficiary() {
        return Type.BENEFICIARY.equals(typ)
    }

    def afterInsert() {
        LOG.info(String.format("Utworzono %s - %s (id: %s)", typ, fullName, id))
    }

    def afterUpdate() {
        LOG.info(String.format("Zaktualizowano %s - %s (id: %s)", typ, fullName, id))
    }
}

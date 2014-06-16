package com.eservice.eumowy

import com.eservice.eumowy.enums.AcceptorLocation
import com.eservice.eumowy.enums.IdentityDocumentType

class Representative {
    private enum Type{
        REPRESENTATIVE, BENEFICIARY
    }

    Type type

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

    Boolean czyStanowiskoPolityczne

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

        czyStanowiskoPolityczne column: "POLITICAL_POSITION"

        posiadaAkceptanta column: "OWNS_ACCEPTOR"
        kontrolujeAkceptanta column: "CONTROL_ACCEPTOR"
        znaczaceUdzialy column: "OVER_QUARTER_VOTES"
        procentUdzialow column: "PERCENT_VOTES"

        type column: "TYPE"
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
       czyStanowiskoPolityczne(nullable: true)
       posiadaAkceptanta(nullable: true)
       kontrolujeAkceptanta(nullable: true)
       znaczaceUdzialy(nullable: true)
       procentUdzialow(nullable: true)
    }
}

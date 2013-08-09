package eservice.docx.converter

import eservice.docx.converter.domain.Acceptor
import eservice.docx.converter.domain.Dokument
import eservice.docx.converter.domain.PH
import eservice.docx.converter.domain.Representant

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 18.07.13
 * Time: 09:36
 * To change this template use File | Settings | File Templates.
 */
class DocxMapper {

    def static process(domainModel){

        PH ph = new PH(imie: "Zenon", nazwisko: "Pehacz", numerSprzedazowy: "12345", nazwaPlikuPodpisu: "signature3.jpg",);

        Representant firstRepresentant = new Representant(imie: "Pawel", nazwisko: "Szkup", nazwaPlikuPodpisu: "signature1.jpg");
        Representant secondRepresentant = new Representant(imie: "Henryk", nazwisko: "Nowak", nazwaPlikuPodpisu: "signature2.jpg");

        Acceptor acceptor = new Acceptor(nazwa: "Szkup Corp.", siedziba: "5th av. New York",
                pierwszyReprezentant: firstRepresentant, drugiReprezentant: secondRepresentant );

        Dokument dm = new Dokument(
                ph: ph,
                akceptant: acceptor,
                dataPodpisaniaUmowy: "21-01-2012",
                dataPodpisaniaAneksu: "02-06-2013",
                oplataZaTransakcjeInternetowe: "34,50",
                oplataZaPrzyjmowanieZaplatyKartamiWObcejWalucie: "25,00",
                oplataZaUruchomieniePrzyjmowaniaZaplatyKartamiWObcejWalucie: "12,45");

        return dm;
    }
}

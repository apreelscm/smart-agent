package mock

import com.eservice.eumowy.command.ProcessCommand

class PanelMockService {

    def getAdresacjaSeciowa(ProcessCommand cmd) {
    }

    def getAdresDoKorespondencjiPunktu(ProcessCommand cmd) {
    }

    def getAdresDoKorespondencjizAkecptantem(ProcessCommand cmd) {
    }

    def getAneksDoUmowyNajmuZestawuPos(ProcessCommand cmd) {
    }

    def getAneksDoUmowyPrepaid(ProcessCommand cmd) {

    }

    def getCzasObowiazywaniaUmowy(ProcessCommand cmd) {

    }

    def getDaneAkceptanta(ProcessCommand cmd) {
        cmd.akceptantNazwaOficjalna= "akceptantNazwaOficjalna"
        cmd.akceptantNazwaSieciowa= "akceptantNazwaSieciowa"
        cmd.akceptantRegon = "1234567895"
        //cmd.akceptantNip = "1234567819";
    }

    def getDaneDoWydruku(ProcessCommand cmd) {

    }

    def getDanePos(ProcessCommand cmd) {

    }

    def getDanePunktu(ProcessCommand cmd) {

    }

    def getDcc(ProcessCommand cmd) {

    }

    def getDccZakresUruchomienia(ProcessCommand cmd) {

    }

    def getDeklaracjeAkceptanta(ProcessCommand cmd) {

    }

    def getDodajPos(ProcessCommand cmd) {

    }

    def getDodajPunkt(ProcessCommand cmd) {

    }

    def getDodatkoweUslugi(ProcessCommand cmd) {

    }

    def getDodatkoweUslugi2(ProcessCommand cmd) {

    }

    def getDodatkoweUslugiMud(ProcessCommand cmd) {

    }

    def getDodatkoweUslugiUTAIntegracja(ProcessCommand cmd) {

    }

    def getDodatkoweWyposazenie(ProcessCommand cmd) {

    }

    def getFormaDoladowania(ProcessCommand cmd) {

    }

    def getFunkcjeTerminala(ProcessCommand cmd) {

    }

    def getIfplus(ProcessCommand cmd) {

    }

    def getInformacjeDodatkowe(ProcessCommand cmd) {

    }

    def getInformacjeTechniczne(ProcessCommand cmd) {

    }

    def getOkresLojalnosciowy(ProcessCommand cmd) {

    }

    def getOpieka(ProcessCommand cmd) {

    }

    def getOplataDCCZaUruchomienie(ProcessCommand cmd) {

    }

    def getOplatyDCC(ProcessCommand cmd) {

    }

    def getOsobaDoKontaktu(ProcessCommand cmd) {

    }

    def getOsobaDoKontaktuWPunkcie(ProcessCommand cmd) {

    }

    def getOsobaKtoraPozyskalaAkceptanta(ProcessCommand cmd) {

    }

    def getOsobaUprawnionaDoPodpisaniaUmowy(ProcessCommand cmd) {

    }

    def getPoziomOplatiWarunkiPlatnosciKarty(ProcessCommand cmd) {

    }

    def getPoziomOplatIWarunkiPlatnosciPP(ProcessCommand cmd) {

    }

    def getPromocyjneObnizenieOplatyZaZestawPos(ProcessCommand cmd) {

    }

    def getRachunekBankowyKlienta(ProcessCommand cmd) {

    }

    def getScoring(ProcessCommand cmd) {

    }

    def getSerwis(ProcessCommand cmd) {

    }

    def getSerwisEkonomiczny(ProcessCommand cmd) {

    }

    def getSerwisKomfort(ProcessCommand cmd) {

    }

    def getSerwisPrzestiz(ProcessCommand cmd) {

    }

    def getSiedzibaAkceptanta(ProcessCommand cmd) {

    }

    def getUmowa2(ProcessCommand cmd) {

    }

    def getUwagi(ProcessCommand cmd) {
        cmd.notes = "";
    }

    def getWykazPunktowAkceptujacychKartyPlatnicze(ProcessCommand cmd) {

    }

    def getZalaczniki(ProcessCommand cmd) {

    }

    def getZestawPos(ProcessCommand cmd) {

    }

    def getZestawPosOdplatneUzywanie(ProcessCommand cmd) {

    }

    def getZestawPosStawkiPreferencyjne(ProcessCommand cmd) {

    }
}
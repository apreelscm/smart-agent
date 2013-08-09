package eservice.docx.converter.domain

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 18.07.13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
class Representant {
    String imie;
    String nazwisko;
    String nazwaPlikuPodpisu;

    InputStream getPodpis() {
        try {
            return new FileInputStream(nazwaPlikuPodpisu);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        null;
    }
}

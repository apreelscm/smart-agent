package eumowy

class BankTagLib {

    static namespace = "dict"

    def dictionaryService;

    Closure bankSelect = { attrs ->
        attrs.from = dictionaryService.getBankComboBox()*.wartosc
        attrs.keys = dictionaryService.getBankComboBox()*.klucz
        fieldImpl(out, attrs)
    }

    def fieldImpl(out, attrs) {
        log.info("attrs: "+attrs);
        out << g.select(attrs)
    }

}
package eumowy

class DictionaryTagLib {

    static namespace = "dict"

    def dictionaryService;

    Closure streetSelect = { attrs ->
        attrs.from = dictionaryService.getUlicaComboBox()*.value
        fieldImpl(out, attrs)
    }

    def fieldImpl(out, attrs) {
        log.info("attrs: "+attrs);
        out << g.select(attrs)
    }

}
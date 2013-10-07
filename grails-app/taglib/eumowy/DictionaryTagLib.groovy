package eumowy

class DictionaryTagLib {

    static namespace = "dict"

    def dictionaryService;

    Closure streetSelect = { attrs ->
        attrs.from = dictionaryService.getUlicaComboBox()*.value
        if (!attrs.value && attrs.default){
            attrs.value = attrs.default
        }
        fieldImpl(out, attrs)
    }
	
	Closure typeSelect = { attrs ->
		def nipNum = attrs.nip
		attrs.from = dictionaryService.getPosTypeComboBox(nipNum)*.value
		fieldImpl(out, attrs)
	}
	
	Closure cbdPointsSelect = { attrs ->
		def nipNum = attrs.nip
		attrs.from = dictionaryService.getCbdPointsComboBox(nipNum)
		attrs.optionKey = "id"
		attrs.optionValue = "value"
		fieldImpl(out, attrs)
	}

    def fieldImpl(out, attrs) {
        out << g.select(attrs)
    }

}
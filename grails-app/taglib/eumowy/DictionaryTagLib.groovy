package eumowy

import groovy.lang.Closure;

class DictionaryTagLib {

    static namespace = "dict"

    def dictionaryService;

    Closure streetSelect = { attrs ->
        attrs.from = dictionaryService.getUlicaComboBox()*.value
        fieldImpl(out, attrs)
    }
	
	Closure typeSelect = { attrs ->
		def nipNum = attrs.nip
		attrs.from = dictionaryService.getPosTypeComboBox(nipNum)*.value
		fieldImpl(out, attrs)
	}

    def fieldImpl(out, attrs) {
        out << g.select(attrs)
    }

}
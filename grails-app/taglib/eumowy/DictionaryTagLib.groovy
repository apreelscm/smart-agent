package eumowy

class DictionaryTagLib {

    static namespace = "dict"

    def dictionaryService;

    Closure streetSelect = { attrs ->
        attrs.from = dictionaryService.getUlicaComboBox()*.value
        if (!attrs.value && attrs.default){
            attrs.value = attrs.default
        }
		attrs.from.add(0,"")
        fieldImpl(out, attrs)
    }

    Closure simCardSelect = { attrs ->
        attrs.from = dictionaryService.getSimCardComboBox()*.value
        if (!attrs.value && attrs.default){
            attrs.value = attrs.default
        }
        attrs.from.add(0,"")
        fieldImpl(out, attrs)
    }
	
	Closure typeSelect = { attrs ->
		def nipNum = attrs.nip
        def medium = attrs.medium
		attrs.from = dictionaryService.getPosTypeComboBox(nipNum, medium)*.value
        attrs.from.add(0,"")
		fieldImpl(out, attrs)
	}
	
	Closure cbdPointsSelect = { attrs ->
		def nipNum = attrs.nip
		attrs.from = dictionaryService.getCbdPointsComboBox(nipNum)
		attrs.optionKey = "id"
		attrs.optionValue = "value"
		fieldImpl(out, attrs)
	}
	
    Closure citySelect = { attrs ->

        def nipNum = attrs.nip
        def medium = attrs.medium
        attrs.from = dictionaryService.getPosTypeComboBox(nipNum, medium)*.value
        attrs.from.add(0,"")
        fieldImpl(out, attrs)
    }

	Closure mccSelect = { attrs ->
		attrs.from = dictionaryService.getMccComboBox()
		attrs.optionKey = "code"
		attrs.optionValue = "description"
		attrs.from.add(0,new Expando(["code": "", "description": ""]))
		fieldImpl(out, attrs)
	}

    def fieldImpl(out, attrs) {
        out << g.select(attrs)
    }

}
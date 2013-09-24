package eumowy

class EumowyFieldTagLib {

    static namespace = "eumowy"


    Closure textField = { attrs ->

        if (!attrs.containsKey('name')) {
            throwTagError("Tag [textArea] is missing required attribute [name]")
        }

        StringBuilder sb = new StringBuilder()

        //def style = attrs.remove("style")
        //sb.append("""<div ${style?' style=\"'+ style+'\"':''}">""");
		
        attrs.class = attrs.class + " " + hasErrors(bean:attrs.validatable,field:attrs.name,'error')
		if (attrs.class?.indexOf("error") != -1) {
			sb.append("<div style=\"padding-right: 2em; display: inline;\">")
		}
        //attrs.style = "width:100%"
        sb.append(g.textField(attrs))

        if(attrs.remove("validatable")){
            def isError = attrs.class.contains("error")
            if(isError){
                def message = attrs.errorMessage ?: message(code:'default.validation.required.error', default: 'Pole Wymagane');
                def icon = g.resource(dir: "images/skin", file: "exclamation.png");

                def imgBody = """<img src="${icon}" class="errorNotification" data-message="${message}" style="cursor:pointer; position:absolute; margin:2px;"/>"""
                sb.append(imgBody)
            }
        }
		if (attrs.class?.indexOf("error") != -1) {
			sb.append("""</div>""")
		}
        out << sb.toString()
    }

    Closure percentageField = { attrs ->
        attrs.type="number"
        attrs.step="any"
        attrs.postfix = message(code:"panel.percent")

        fieldImpl(out, attrs)
    }

    Closure currencyField = { attrs ->
        attrs.type="number"
        attrs.step="any"
        attrs.postfix = message(code:"panel.polish.currency")

        fieldImpl(out, attrs)
    }

    Closure numberField = { attrs ->
        attrs.type="number"
        attrs.step="any"

        fieldImpl(out, attrs)
    }


    def fieldImpl(out, attrs) {
        if (!attrs.containsKey('name')) {
            throwTagError("Tag [textArea] is missing required attribute [name]")
        }

        def width = attrs.remove("width")
        def postfix = attrs.remove("postfix")
        int offset = attrs.offset.toString().isNumber() ? Integer.valueOf(attrs.remove("offset")) : 0

        attrs.style = "text-align:right" + (width ? "; width:${width}" : "")
        attrs.class = attrs.class + " " + hasErrors(bean:attrs.validatable,field:attrs.name,'error')

        StringBuilder sb = new StringBuilder()
        if (attrs.class?.indexOf("error") != -1) {
			sb.append("<div style=\"padding-right: 2em; display: inline;\">")
        }
		sb.append(g.field(attrs))

        if(attrs.remove("validatable")){
            def isError = attrs.class.contains("error")
            if(isError){
                def message = attrs.errorMessage ?: message(code:'default.validation.required.error', default: 'Pole Wymagane');
                def icon = g.resource(dir: "images/skin", file: "exclamation.png");
                def imgBody = """<img src="${icon}" class="errorNotification" data-message="${message}" style="cursor:pointer; position:absolute; margin:2px;"/>"""
                sb.append(imgBody)
            }
        }

        if(postfix){
            if (attrs.class?.indexOf("error") != -1) {
                sb.append("""<span class="postfix-span" ${offset != 0 ? ' style="right:-'+offset+'px;"' : ''}>${postfix}</span>""")
                sb.append("""</div>""")
            }
            else {
                sb.append(""" ${postfix}""")
            }
        }


        out << sb.toString()
    }
}
package eumowy

import com.eservice.eumowy.DomainConsts

class EumowyFieldTagLib {

    static namespace = "eumowy"


    Closure textField = { attrs ->

        if (!attrs.containsKey('name')) {
            throwTagError("Tag [textArea] is missing required attribute [name]")
        }

        StringBuilder sb = new StringBuilder()

        //def style = attrs.remove("style")
        //sb.append("""<div ${style?' style=\"'+ style+'\"':''}">""");

        def validateField = attrs.remove("validateField") ?: attrs.name
        attrs.class = attrs.class + " " + hasErrors(bean:attrs.validatable,field:validateField,'error')


		if (attrs.class?.indexOf("error") != -1) {
			sb.append("<div style=\"padding-right: 2em; display: inline;\">")
		}
        //attrs.style = "width:100%"
        sb.append(g.textField(attrs))

        def cmd = attrs.remove("validatable")
        if(cmd){
            def isError = attrs.class.contains("error")
            if(isError){

                def message = attrs.errorMessage ?: message(error:cmd.errors.getFieldError(validateField));
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

    Closure flatPriceField = { attrs ->
        if(attrs.value == null || attrs.value == ""){
            attrs.value = "-"
        }
        attrs.type="text"
        attrs.postfix = message(code:"panel.polish.currency")

        fieldImpl(out, attrs)
    }

    Closure percentageField = { attrs ->
        attrs.type="text"
        attrs.postfix = message(code:"panel.percent")

        fieldImpl(out, attrs)
    }

    Closure currencyField = { attrs ->
        attrs.type="text"
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

        def validateField = attrs.remove("validateField") ?: attrs.name
        attrs.class = attrs.class + " " + hasErrors(bean:attrs.validatable,field:validateField,'error')

        StringBuilder sb = new StringBuilder()
        if (attrs.class?.indexOf("error") != -1) {
			sb.append("<div style=\"padding-right: 2em; display: inline;\">")
        }
		sb.append(g.field(attrs))

        def cmd = attrs.remove("validatable")
        if(cmd){
            def isError = attrs.class.contains("error")
            if(isError){
                def message = attrs.errorMessage ?: message(error:cmd.errors.getFieldError(validateField));
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
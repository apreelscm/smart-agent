package eumowy

class EumowyFieldTagLib {

    static namespace = "eumowy"


    Closure textField = { attrs ->
        out << createErrorTag(g.textField(attrs), attrs)
    }

    Closure select = { attrs ->
        out << createErrorTag(g.select(attrs), attrs)
    }

    private String createErrorTag(def tag, def attrs) {
        if (!attrs.containsKey('name')) {
            throwTagError("Tag is missing required attribute name")
        }

        StringBuilder sb = new StringBuilder()

        def validateField = attrs.remove("validateField") ?: attrs.name
        attrs.class = attrs.class + " " + hasErrors(bean:attrs.validatable,field:validateField,'error')

        if (attrs.class?.indexOf("error") != -1) {
            sb.append("<div class=\"error-wrapper\">")
        }

        sb.append(tag)

        def cmd = attrs.remove("validatable")
        if(cmd){
            def isError = attrs.class.contains("error")
            if(isError){

                def message = attrs.getErrorMessageCode ?: message(error:cmd.errors.getFieldError(validateField));
                def icon = asset.assetPath(src: "skin/exclamation.png")

                def imgBody = """<img src="${icon}" class="errorNotification" data-message="${message}"/>"""
                sb.append(imgBody)
            }
        }
        if (attrs.class?.indexOf("error") != -1) {
            sb.append("""</div>""")
        }

        return sb.toString()
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

    Closure enumRadioGroup = { attrs, body ->
        if(!attrs.values || !attrs.name) {
            throwTagError("Attributes 'values' and 'name' are required!")
        }

        createField(attrs, body, "/common/tags/enumRadioGroup")
    }

    private def createField(attrs, body, template) {
        Map props = [:]

        props.template = template
        props.model = attrs

        out << render(props, body)
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
			sb.append("<div style=\"display: inline-block;\">")
        }
		sb.append(g.field(attrs))

        def cmd = attrs.remove("validatable")
        if(cmd){
            def isError = attrs.class.contains("error")
            if(isError){
                def message = attrs.errorMessage ?: message(error:cmd.errors.getFieldError(validateField));
                def icon = asset.assetPath(src: "skin/exclamation.png")
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
                sb.append("""${postfix}""")
            }
        }


        out << sb.toString()
    }
}

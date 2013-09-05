package eumowy

class EumowyFieldTagLib {

    static namespace = "eumowy"


    Closure textField = { attrs ->

        if (!attrs.containsKey('name')) {
            throwTagError("Tag [textArea] is missing required attribute [name]")
        }

        StringBuilder sb = new StringBuilder()

        attrs.class = hasErrors(bean:attrs.validatable,field:attrs.name,'error')

        def style = attrs.remove("style")
        sb.append("""<div class="" style="${style}">""");
        attrs.style = "width:100%"

        sb.append(g.textField(attrs))

        if(attrs.remove("validatable")){
            def isError = (attrs.class == "error")
            if(isError){
                def message = attrs.errorMessage ?: message(code:'default.validation.required.error', default: 'Pole Wymagane');
                def icon = g.resource(dir: "images/skin", file: "exclamation.png");
                def imgBody = """<img src="${icon}" class="errorNotification" data-message="${message}" style="cursor:pointer; position:absolute; margin:2px;"/>"""
                sb.append(imgBody)
            }
        }

        sb.append("""</div>""")
        out << sb.toString()
    }

    Closure percentageField = { attrs ->
        attrs.classDiv = "postfix"
        attrs.type="number"
        attrs.step="any"
        attrs.postfix = message(code:"panel.percent")

        fieldImpl(out, attrs)
    }

    Closure currencyField = { attrs ->
        attrs.classDiv = "postfix"
        attrs.type="number"
        attrs.step="any"
        attrs.postfix = message(code:"panel.polish.currency")

        fieldImpl(out, attrs)
    }


    def fieldImpl(out, attrs) {
        def classDiv = attrs.remove("classDiv")
        def width = attrs.remove("width")
        def postfix = attrs.remove("postfix")
        int offset = attrs.offset.toString().isNumber() ? Integer.valueOf(attrs.remove("offset")) : 15


        attrs.style = "text-align:right"

        if (!attrs.containsKey('name')) {
            throwTagError("Tag [textArea] is missing required attribute [name]")
        }

        StringBuilder sb = new StringBuilder()

        attrs.class = hasErrors(bean:attrs.validatable,field:attrs.name,'error')
        sb.append("""<div class=" ${classDiv ?: ''}  ${attrs.class}" style="position:relative; margin-right: 25px; ${width ? 'width:' + width : ''}">""");

        sb.append(g.field(attrs))

        if(attrs.remove("validatable")){
            def isError = (attrs.class == "error")
            if(isError){
                def message = attrs.errorMessage ?: message(code:'default.validation.required.error', default: 'Pole Wymagane');
                def icon = g.resource(dir: "images/skin", file: "exclamation.png");
                def imgBody = """<img src="${icon}" class="errorNotification" data-message="${message}" style="cursor:pointer; position:absolute; margin:2px; right:-35px;"/>"""
                sb.append(imgBody)
            }
        }


        sb.append("""<span style="position:absolute; right:-${offset}px; top: 0px">${postfix}</span>""")

        sb.append("""</div>""")

        out << sb.toString()
    }
}
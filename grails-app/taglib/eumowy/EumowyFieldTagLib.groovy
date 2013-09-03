package eumowy

class EumowyFieldTagLib {

    static namespace = "eumowy"

    Closure textArea = { attrs ->

        if (!attrs.containsKey('name')) {
            throwTagError("Tag [textArea] is missing required attribute [name]")
        }

        StringBuilder sb = new StringBuilder()

        def isMandatory = attrs.mandatory

        if(isMandatory){
            if (!attrs.containsKey('bean')) {
                throwTagError("Tag [textArea] is missing required attribute [bean]")
            }

            attrs.class = hasErrors(bean:attrs.bean,field:attrs.name,'error')

            def style = attrs.remove("style")
            sb.append("""<div class="" style="${style}">""");
            attrs.style = "width:100%"


            sb.append(g.textArea(attrs))

            def isError = (attrs.class == "error")
            if(isError){
                def message = attrs.errorMessage ?: message(code:'default.validation.required.error', default: 'Pole Wymagane');
                def icon = g.resource(dir: "images/skin", file: "exclamation.png");
                def imgBody = """<img src="${icon}" onclick="allertError('${message}')"  style="cursor:pointer; position:absolute"/>"""
                sb.append(imgBody)
            }

            sb.append("""</div>""")
        }
        else{
            sb.append(g.textArea(attrs))
        }


        out << sb.toString()
    }

    Closure textField = { attrs ->

        if (!attrs.containsKey('name')) {
            throwTagError("Tag [textArea] is missing required attribute [name]")
        }

        StringBuilder sb = new StringBuilder()

        def isMandatory = attrs.mandatory

        if(isMandatory){
            if (!attrs.containsKey('bean')) {
                throwTagError("Tag [textArea] is missing required attribute [bean]")
            }

            attrs.class = hasErrors(bean:attrs.bean,field:attrs.name,'error')

            def style = attrs.remove("style")
            sb.append("""<div class="" style="${style}">""");
            attrs.style = "width:100%"


            sb.append(g.textField(attrs))

            def isError = (attrs.class == "error")
            if(isError){
                def message = attrs.errorMessage ?: message(code:'default.validation.required.error', default: 'Pole Wymagane');
                def icon = g.resource(dir: "images/skin", file: "exclamation.png");
                def imgBody = """<img src="${icon}" class="errorNotification" data-message="${message}" style="cursor:pointer; position:absolute; margin:2px"/>"""
                sb.append(imgBody)
            }

            sb.append("""</div>""")
        }
        else{
            sb.append(g.textField(attrs))
        }


        out << sb.toString()
    }

    /* def fieldImpl(out, attrs) {

         if (!attrs.containsKey('name')) {
             throwTagError("Tag [textArea] is missing required attribute [name]")
         }

         out << g.select(attrs)
     }*/

}
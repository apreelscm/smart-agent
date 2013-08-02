package eumowy

import grails.artefact.Artefact


@Artefact("TagLibrary")
class RequiredFieldTagLib {

    static namespace = "apreel"

    final def DEFAULT_MESSAGE = "Wymagane Pole"

    final def H_DIRECTION = "horizontal"
    final def V_DIRECTION = "vertical"


    Closure textField = { attrs ->
        attrs.type = "text"
        attrs.tagName = "textField"
        attrs.field = g.textField(attrs);
        fieldImpl(out, attrs)
    }

    Closure selectField = { attrs ->
        attrs.type = "select"
        attrs.tagName = "selectField"
        attrs.field = g.select(attrs);
        fieldImpl(out, attrs)
    }

    def fieldImpl(out, attrs) {
        //layout direction
        def direction = attrs.direction ?: H_DIRECTION;

        //text field
        def name = attrs.name;
        def title = attrs.title;
        def inputField = attrs.field

        //error icon
        def isError = attrs.isError ?: false;
        def message = attrs.errorMessage ?: DEFAULT_MESSAGE;
        def icon = g.resource(dir: "images/skin", file: "exclamation.png");
        def imgBody = """<img src="${icon}" onclick="allertError('${message}')" class="display-none" style="cursor:pointer;"/>"""

        StringBuilder sb = new StringBuilder();

        if (direction.equals(V_DIRECTION)) {
            sb << """
              <div class="requiredField ${isError ? 'error' : ''}">
                <label for="${name}">
               ${title}
                </label>
               <p> ${inputField}
                ${isError ? imgBody : '' }
                </p>
            </div>
        """
        } else {
            sb << """
              <div class="requiredField display-inline-block ${isError ? 'error' : ''}">
                <label for="${name}">
               ${title}
                </label>
              ${inputField}
               ${imgBody}
            </div>
        """
        }

        log.info(sb.toString())

        out << sb.toString()
    }

}
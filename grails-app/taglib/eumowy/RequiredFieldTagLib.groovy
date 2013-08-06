package eumowy

import grails.artefact.Artefact


@Artefact("TagLibrary")
class RequiredFieldTagLib {

    static namespace = "apreel"


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
        def id = attrs.id;
        def name = attrs.name;
        def disabled = attrs.disabled;
        def title = attrs.title;
        def inputField = attrs.field

        //error icon
        def isError = attrs.isError ?: false;
        def message = attrs.errorMessage ?: message(code:'default.validation.required.error', default: 'Pole Wymagane');
        def icon = g.resource(dir: "images/skin", file: "exclamation.png");
        def imgBody = """<img src="${icon}" onclick="allertError('${message}')" class="visibility-hidden" style="cursor:pointer;"/>"""

        StringBuilder sb = new StringBuilder();

        if (direction.equals(V_DIRECTION)) {
            sb << """
              <div ${id ? 'id = "'+ id +'"': ''} class="requiredField ${isError ? 'error' : ''} display-block" style="disabled:${disabled}">
                <label for="${name}" style="margin-bottom:10px">
               ${title}
                </label>
               ${inputField} ${imgBody}
            </div>
        """
        } else {
            sb << """
              <div ${id ? 'id = "'+ id +'"' : ''} class="requiredField display-inline-block ${isError ? 'error' : ''}">
                <label for="${name}">
               ${title}
                </label>
              ${inputField}
               ${imgBody}
            </div>
        """
        }

        out << sb.toString()
    }

}
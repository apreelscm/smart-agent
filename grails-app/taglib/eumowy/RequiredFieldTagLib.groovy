package eumowy

class RequiredFieldTagLib {

    static namespace = "apreel"

    final def DEFAULT_MESSAGE = "Wymagane Pole"

    final def H_DIRECTION = "horizontal"
    final def V_DIRECTION = "vertical"

    def requiredField = { attrs ->

        //layout direction
        def direction = attrs.direction ?: H_DIRECTION;

        //text field
        def name = attrs.name;
        def title = attrs.title;
        def textField = g.textField(name: name)

        //error icon
        def isError = attrs.isError?:false;
        def message = attrs.errorMessage ?: DEFAULT_MESSAGE;
        def icon = g.resource(dir: "images/skin", file: "exclamation.png");
        def imgBody = """<img src="${icon}" onclick="allertError('${message}')" style="cursor:pointer;"/>"""

        StringBuilder sb = new StringBuilder();

        if(direction.equals(V_DIRECTION)){
            sb << """
              <div ${isError ? 'class="error"' : ''}>
                <label for="${name}">
               ${title}
                </label>
               <p> ${textField}
                ${isError ? imgBody  : '' }
                </p>
            </div>
        """
        }
        else{
            sb << """
              <div class="display-inline ${isError ? 'error' : ''}">
                <label for="${name}">
               ${title}
                </label>
              ${textField}
               ${isError ? imgBody  : '' }
            </div>
        """
        }

        log.info(sb.toString())

        out << sb.toString()
    }

}
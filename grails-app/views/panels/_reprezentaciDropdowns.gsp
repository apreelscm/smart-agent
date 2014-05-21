<g:set var="firstNames" value="${[""] + representativesBisnode?.collect {it.firstName}}"/>
<g:set var="lastNames" value="${[""] + representativesBisnode?.collect {it.lastName}}"/>

<div>
    <eumowy:textField name="reprezentant1Tytul" value="${data.reprezentant1Tytul}" validatable="${data}" readonly="readonly" class="tytulField"/>

    <label for="reprezentant1Imie"><g:message code="panel.first.name"/>:</label>
    <g:select name="reprezentant1Imie" from="${firstNames}" value="${data.reprezentant1Imie}" validatable="${data}" class="imieField" required="required"/>

    <label for="reprezentant1Nazwisko"><g:message code="panel.last.name"/>:</label>
    <g:select name="reprezentant1Nazwisko" from="${lastNames}" value="${data.reprezentant1Nazwisko}" validatable="${data}" class="nazwiskoField" required="required"/>

    <label for="reprezentant1Stanowisko"><g:message code="panel.position"/>:</label>
    <eumowy:textField name="reprezentant1Stanowisko" value="${data.reprezentant1Stanowisko}" validatable="${data}" readonly="readonly" class="positionField"/>
</div>

<div>
    <eumowy:textField name="reprezentant2Tytul" value="${data.reprezentant2Tytul}" validatable="${data}" readonly="readonly" class="tytulField"/>

    <label for="reprezentant2Imie"><g:message code="panel.first.name"/>:</label>
    <g:select name="reprezentant2Imie" from="${firstNames}" value="${data.reprezentant2Imie}" class="imieField"/>

    <label for="reprezentant2Nazwisko"><g:message code="panel.last.name"/>:</label>
    <g:select name="reprezentant2Nazwisko" from="${lastNames}" value="${data.reprezentant2Nazwisko}" class="nazwiskoField"/>

    <label for="reprezentant2Stanowisko"><g:message code="panel.position"/>:</label>
    <eumowy:textField name="reprezentant2Stanowisko" value="${data.reprezentant2Stanowisko}" validatable="${data}" readonly="readonly" class="positionField"/>
</div>

<div>
    <eumowy:textField name="reprezentant3Tytul" value="${data.reprezentant3Tytul}" validatable="${data}" readonly="readonly" class="tytulField"/>

    <label for="reprezentant3Imie"><g:message code="panel.first.name"/>:</label>
    <g:select name="reprezentant3Imie" from="${firstNames}" value="${data.reprezentant3Imie}" class="imieField"/>

    <label for="reprezentant3Nazwisko"><g:message code="panel.last.name"/>:</label>
    <g:select name="reprezentant3Nazwisko" from="${lastNames}" value="${data.reprezentant3Nazwisko}" class="nazwiskoField"/>

    <label for="reprezentant3Stanowisko"><g:message code="panel.position"/>:</label>
    <eumowy:textField name="reprezentant3Stanowisko" value="${data.reprezentant3Stanowisko}" validatable="${data}" readonly="readonly" class="positionField"/>
</div>

<script type="text/javascript">
    var representatives = {};

    <g:each in="${representativesBisnode}" var="representative" status="i">
        representatives[${i}] = {title: '${representative.title}', fistName: '${representative.firstName}', lastName: '${representative.lastName}', position: '${representative.position}'};
    </g:each>

    jQuery(".imieField, .nazwiskoField").change(function() {
        var $this = jQuery(this),
            selectedOptionNo = $this[0].selectedIndex,
            parentDiv = $this.parent('div'),
            firstNameSelect = parentDiv.find('.imieField'),
            lastNameSelect = parentDiv.find('.nazwiskoField'),
            titleInput = parentDiv.find('.tytulField'),
            positionInput = parentDiv.find('.positionField');

        firstNameSelect[0].selectedIndex = selectedOptionNo;
        lastNameSelect[0].selectedIndex = selectedOptionNo;

        if(selectedOptionNo === 0) {
            positionInput.val('');
            titleInput.val('')
        } else {
            positionInput.val(representatives[selectedOptionNo - 1].position);
            titleInput.val(representatives[selectedOptionNo - 1].title);
        }
    });
</script>
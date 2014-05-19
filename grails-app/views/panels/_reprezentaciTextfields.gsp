<div>
    <g:select name="reprezentant1Tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.reprezentant1Tytul}"/>

    <label for="reprezentant1Imie"><g:message code="panel.first.name"/>:</label>
    <eumowy:textField name="reprezentant1Imie" value="${data.reprezentant1Imie}" validatable="${data}" maxlength ="13" required="true"/>

    <label for="reprezentant1Nazwisko"><g:message code="panel.last.name"/>:</label>
    <eumowy:textField name="reprezentant1Nazwisko" value="${data.reprezentant1Nazwisko}" validatable="${data}" maxlength ="35" required="true" class="nazwiskoField"/>

    <label for="reprezentant1Stanowisko"><g:message code="panel.position"/>:</label>
    <eumowy:textField name="reprezentant1Stanowisko" value="${data.reprezentant1Stanowisko}" validatable="${data}"/>
</div>

<div>
    <g:select name="reprezentant2Tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.reprezentant2Tytul}"/>

    <label for="reprezentant2Imie"><g:message code="panel.first.name"/>:</label>
    <g:textField name="reprezentant2Imie" value="${data.reprezentant2Imie}" maxlength ="13"/>

    <label for="reprezentant2Nazwisko"><g:message code="panel.last.name"/>:</label>
    <g:textField name="reprezentant2Nazwisko" value="${data.reprezentant2Nazwisko}" maxlength ="35" class="nazwiskoField"/>

    <label for="reprezentant2Stanowisko"><g:message code="panel.position"/>:</label>
    <eumowy:textField name="reprezentant2Stanowisko" value="${data.reprezentant2Stanowisko}" validatable="${data}"/>
</div>

<div>
    <g:select name="reprezentant3Tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.reprezentant3Tytul}"/>

    <label for="reprezentant3Imie"><g:message code="panel.first.name"/>:</label>
    <g:textField name="reprezentant3Imie" value="${data.reprezentant3Imie}" maxlength ="13"/>

    <label for="reprezentant3Nazwisko"><g:message code="panel.last.name"/>:</label>
    <g:textField name="reprezentant3Nazwisko" value="${data.reprezentant3Nazwisko}" maxlength ="35" class="nazwiskoField"/>

    <label for="reprezentant3Stanowisko"><g:message code="panel.position"/>:</label>
    <eumowy:textField name="reprezentant3Stanowisko" value="${data.reprezentant3Stanowisko}" validatable="${data}" required="true"/>
</div>
<div id="acceptorsPanel" style="margin: 0">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 750px">
            <ul class="table-list centre">
                <li>
                    <span>
                        <span><g:select name="reprezentant1Tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.reprezentant1Tytul}"/></span>
                        <span><g:message code="panel.first.name"/>: </span>
                        <span><eumowy:textField name="reprezentant1Imie" value="${data.reprezentant1Imie}" validatable="${data}" maxlength ="13" required="true"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.last.name"/>: </span>
                        <span><eumowy:textField name="reprezentant1Nazwisko" value="${data.reprezentant1Nazwisko}" validatable="${data}" maxlength ="14" required="true"/></span>
                    </span>
                </li>
                <li>
                    <span>
                        <span><g:select name="reprezentant2Tytul" from="['Pan','Pani']" valueMessagePrefix="person.title" value="${data.reprezentant2Tytul}"/></span>
                        <span><g:message code="panel.first.name"/>: </span>
                        <span><g:textField name="reprezentant2Imie" value="${data.reprezentant2Imie}" maxlength ="13"/></span>
                    </span>
                    <span>
                        <span><g:message code="panel.last.name"/>: </span>
                        <span><g:textField name="reprezentant2Nazwisko" value="${data.reprezentant2Nazwisko}" maxlength ="14"/></span>
                    </span>
                </li>
            </ul>
            <span>
                <span><g:message code="todo" default="Adres email do wysyłki dokumentu"/>:</span>
                <span><g:textField id="emailDoWysylkiDokumentu" class="" name="emailDoWysylkiDokumentu" value="${data.emailDoWysylkiDokumentu}" validatable="${data}" style="width: 150px" email="true"/></span>
            </span>
        </div>
    </fieldset>
</div>

<r:script>
  /*  jQuery(document).ready(function() {
        alert(jQuery("#kontaktEmail").val())
        if(jQuery("#kontaktEmail").length){
            jQuery("#emailDoWysylkiDokumentu").val(jQuery("#kontaktEmail").val()).attr("readonly","true")
        }
    });*/
</r:script>
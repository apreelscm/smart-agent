
<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'process.label', default: 'Process')}" />
    <g:set var="isNewProcess" value="${processInstance?.status.equals(Process.ProcessStatus.NEW)}" />
    <title>${entityName}</title>

</head>
<body>
<a href="#show-process" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><g:link class="list" action="list"><g:message code="process.list.label" /></g:link></li>
    </ul>
</div>

<div id="show-process" role="main">

    <ul class="property-list process" >
        <li style="display:block;margin:0em 0em 1em 0em">
            <label for="clientName">
                <g:message code="process.clientName.label" default="Nazwa Klienta" />
            </label>
            <g:textField name="clientName" value="${processInstance?.clientName}" disabled="disabled"/>
        </li>


            <li style="display:inline;" >
                <label for="clientNip">
                    <g:message code="process.clientNip.label" default="NIP" />

                </label>
                <g:textField name="clientNip" value="${processInstance?.clientNip}" disabled="disabled"/>
            </li>


            <li  style="display:inline;" >
                <label for="saleSection">
                    <g:message code="process.saleSection.label" default="Rodzaj Działania" />
                </label>
            </li>


            <li  style="display:inline;" >
                <label for="phNumber">
                    <g:message code="process.phNumber.label" default="Numer Przedstawiciela" />

                </label>
                <g:textField name="phNumber" value="${processInstance?.phNumber}" disabled="disabled"/>
            </li>

            <li  style="display:inline;" >
                <label for="phFirstName">
                    <g:message code="process.phFirstName.label" default="Imię i Naziwsko" />
                </label>
                <g:textField name="phFirstName" value="${processInstance?.phFirstName + " " + processInstance?.phSurname}"
                             disabled="disabled"/>
            </li>
    </ul>

    <ul style="display:block;margin:1em 1em 1em 1em">
        <li style="display:inline;"><button>Pokaż Dokumenty</button></li>
        <li style="display:inline;"><button>Pobierz Załączniki</button></li>
    </ul>


    <div style="margin-left: 1em ; margin-right: 1em;margin-bottom: 1em">
        <embed src="${resource(dir:'files', file:'pedef.pdf')}" type='application/pdf'
               style="height: 1100px; width: 100%"/>
    </div>

%{--
    <object data="${resource(dir:'files', file:'pedef.pdf')}" type="application/pdf">
        <p>Your web browser doesn't have a PDF plugin.
        Instead you can <a href="filename.pdf">click here to
        download the PDF file.</a></p>
    </object>--}%

        <g:checkBox name="observe"/> Obserwuj

    <g:form style="${!isNewProcess ? 'display:none' : ''}">
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${processInstance?.id}" />
            <g:actionSubmit class="save" action="accept" value="Zaakceptuj"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            <g:actionSubmit class="delete" action="reject" value="Odrzuć"
                            formnovalidate=""
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
    </g:form>
</div>
</body>
</html>



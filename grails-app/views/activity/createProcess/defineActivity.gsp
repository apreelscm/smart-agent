<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'activity.label', default: 'Activity')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
    <r:require module="expandable_tree"/>

    <style>
        div.disabled{
            pointer-events: none;
        }

        div.disabled a{
            color: #a7a7a7;
            text-decoration-line: line-through;
        }
    </style>

</head>
<body>

<section id="create-activity">

    <h1 class="ng linia-bottom">Wybierz działanie</h1>

    <g:form>
        <div class="activityTree" style="margin: 10px auto 0 auto;display: table;">

            <div id="nowaUmowa" class="expendable">
                <div class="checkBoxBlock">
                    <g:checkBox id="nowaUmowaCB" name="nowaUmowa"/>
                    <a class="expandHeader expanded" href="#">Nowa umowa</a>
                </div>
            </div>

            <div id="rozszerzenie" class="expendable">
                <a class="expander expandHeader expanded" href="#">Rozszerzenie</a>

                <div class="content nesting-sub1" style="display: block;">
                    <div class="checkBoxBlock">
                        <g:checkBox id="dodatkowyPunktCB" name="dodatkowyPunkt" class="_nowaUmowa _zmianaWarunkow" />
                        <label for="dodatkowyPunktCB"><g:message code="todo" default="Dodatkowy Punkt"/></label>
                    </div>

                    <div class="checkBoxBlock">
                        <g:checkBox id="dodatkowyPosCB" name="dodatkowyPos"/>
                        <label for="dodatkowyPosCB"><g:message code="todo" default="Dodatkowy Pos"/></label>
                    </div>
                </div>
            </div>

            <div id="zmianaWarunkow" class="expendable">
                <a class="expander expandHeader expanded" href="#">Zmiana warunków handlowych</a>

                <div class="content nesting-sub1" style="display: block;">

                    <div class="checkBoxBlock">
                        <g:checkBox id="zmianaProwizjiCB" name="zmianaProwizji"/>
                        <label for="zmianaProwizjiCB"><g:message code="todo" default="Zmiana prowizji"/></label>
                    </div>

                    <div class="checkBoxBlock">
                        <g:checkBox id="zmianaWarunkowDccCB" name="zmianaWarunkowDcc"/>
                        <label for="zmianaWarunkowDccCB"><g:message code="todo" default="Zmiana warunków DCC"/></label>
                    </div>

                    <div class="checkBoxBlock">

                        <a class="expander expanderSub expanded" href="#">Dodanie aneksu Koszty +</a>

                        <div class="content nesting-sub2" style="display: block;">
                            <div class="checkBoxBlock">
                                <g:checkBox id="wymianaUmowyNajmuCB" name="wymianaUmowyNajmu" />
                                <label for="wymianaUmowyNajmuCB"><g:message code="todo" default="Wymiana umowy najmu"/></label>
                            </div>

                            <div class="checkBoxBlock">
                                <g:checkBox id="aneksCB" name="aneks"/>
                                <label for="aneksCB"> <g:message code="todo" default="Aneks"/></label>
                            </div>
                        </div>
                    </div>

                    <div class="checkBoxBlock">
                        <g:checkBox id="zmianaTabeliOplatDodatkowychCB" name="zmianaTabeliOplatDodatkowych"/>
                        <label for="zmianaTabeliOplatDodatkowychCB"><g:message code="todo" default="Zmiana tabeli opłat dodatkowych"/></label>
                    </div>

                    <div class="checkBoxBlock">
                        <g:checkBox id="zmianaWarunkowPrepaidCB" name="zmianaWarunkowPrepaid"/>
                        <label for="zmianaWarunkowPrepaidCB"><g:message code="todo" default="Zmiana warunków prepaid"/></label>
                    </div>

                    <div class="checkBoxBlock">
                        <g:checkBox id="zmianaOkresuLojalnosciowegoCB" name="zmianaOkresuLojalnosciowego"/>
                        <label for="zmianaOkresuLojalnosciowegoCB"><g:message code="todo" default="Zmiana okresu Lojalnościowego"/></label>
                    </div>

                    <div class="checkBoxBlock">
                        <g:checkBox id="promocyjneObnizenieNajmuCB" name="promocyjneObnizenieNajmu"/>
                        <label for="promocyjneObnizenieNajmuCB"> <g:message code="todo" default="Promocyjne obniżenie najmu"/></label>
                    </div>
                </div>
            </div>

            <div id="dodatkoweFuncjonalnosci" class="expendable">
                <a class="expander expandHeader expanded" href="#">Dodatkowe funkcjonalności</a>

                <div class="content nesting-sub1" style="display: block;">

                    <div class="checkBoxBlock">
                        <g:checkBox id="dodaniePrepaidCB" name="dodaniePrepaid"/>
                        <label for="dodaniePrepaidCB"><g:message code="todo" default="Dodanie Prepaid"/></label>
                    </div>

                    <div class="checkBoxBlock">
                        <g:checkBox id="dodanieDccCB" name="dodanieDcc"/>
                        <label for="dodanieDccCB"><g:message code="todo" default="Dodanie DCC"/></label>
                    </div>

                    <div class="checkBoxBlock">
                        <g:checkBox id="dodanieCashBackCB" name="dodanieCashBack"/>
                        <label for="dodanieCashBackCB"><g:message code="todo" default="Dodanie CashBack"/></label>
                    </div>

                    <div class="checkBoxBlock">
                        <g:checkBox id="dodanieIkoCB" name="dodanieIko"/>
                        <label for="dodanieIkoCB"><g:message code="todo" default="Dodanie IKO"/></label>
                    </div>


                    <div id="pakietSerwisowy" class="checkBoxBlock">
                        <a class="expander expanderSub expanded" href="#">Pakiet serwisowy</a>

                        <div class="content nesting-sub2" style="display: block;">
                            <div class="checkBoxBlock">
                                <g:checkBox id="ekonomicznyCB" name="ekonomiczny"/>
                                <label for="ekonomicznyCB"><g:message code="todo" default="Ekonomiczny"/></label>
                            </div>

                            <div class="checkBoxBlock">
                                <g:checkBox id="komfortCB" name="komfort"/>
                                <label for="komfortCB"><g:message code="todo" default="Komfort"/></label>
                            </div>

                            <div class="checkBoxBlock">
                                <g:checkBox id="prestizCB" name="prestiz"/>
                                <label for="prestizCB"><g:message code="todo" default="Prestiż"/></label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="poprawDane" class="expendable">
                <div class="checkBoxBlock">
                    <g:checkBox id="poprawDaneCB" name="poprawDane"/>
                   <a class="expandHeader expanded" href="#">Popraw już wprowadzone dane</a>
                </div>
            </div>

            <div id="odrzucDokumenty" class="expendable">
                <div class="checkBoxBlock">
                    <g:checkBox id="odrzucDokumentyCB" name="odrzucDokumenty"/>
                    <a class="expandHeader expanded" href="#">Odrzuć jeszcze niezaakceptowane dokumenty</a>
                </div>
            </div>
        </div>

        <div style="margin:25px auto 0 auto;display: table;position: relative;left: -40px">
            <g:message code="todo" default="Uwagi dla COA:"/>
            <g:textArea name="notes" style="margin-left: 8px; height: 100px"/>

            <div id="notesMessageBox">
                <g:if test="${flash.infoMessage}">
                    <g:render template="message/infoMessage" model="[message: flash.infoMessage]"/>
                </g:if>
                <g:if test="${errorMessage}">
                    <g:render template="message/errorMessage" model="[message: errorMessage]"/>
                </g:if>
            </div>
        </div>

      %{--  <g:link controller="attachment">Dodaj załącznik</g:link>--}%

        <nav style="margin-top: 20px">
            <fieldset>
                <g:submitButton name="continue" class="button submit" value="Dalej" />
            </fieldset>
        </nav>
    </g:form>

</section>

</body>
</html>
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
                    <g:message code="todo" default="Dodatkowy Punkt"/>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox id="dodatkowyPosCB" name="dodatkowyPos"/>
                    <g:message code="todo" default="Dodatkowy Pos"/>
                </div>
            </div>
        </div>


        <div id="zmianaWarunkow" class="expendable">
            <a class="expander expandHeader expanded" href="#">Zmiana warunków handlowych</a>

            <div class="content nesting-sub1" style="display: block;">

                <div class="checkBoxBlock">
                    <g:checkBox id="zmianaProwizjiCB" name="zmianaProwizji"/>
                    <g:message code="todo" default="Zmiana prowizji"/>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox id="zmianaWarunkowDccCB" name="zmianaWarunkowDcc"/>
                    <g:message code="todo" default="Zmiana warunków DCC"/>
                </div>

                <div class="checkBoxBlock">

                    <a class="expander expanderSub expanded" href="#">Dodanie aneksu Koszty +</a>

                    <div class="content nesting-sub2" style="display: block;">
                        <div class="checkBoxBlock">
                            <g:checkBox id="wymianaUmowyNajmuCB" name="wymianaUmowyNajmu" />
                            <g:message code="todo" default="Wymiana umowy najmu"/>
                        </div>

                        <div class="checkBoxBlock">
                            <g:checkBox id="aneksCB" name="aneks"/>
                            <g:message code="todo" default="Aneks"/>
                        </div>
                    </div>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox id="zmianaTabeliOplatDodatkowychCB" name="zmianaTabeliOplatDodatkowych"/>
                    <g:message code="todo" default="Zmiana tabeli opłat dodatkowych"/>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox id="zmianaWarunkowPrepaidCB" name="zmianaWarunkowPrepaid"/>
                    <g:message code="todo" default="Zmiana warunków prepaid"/>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox id="zmianaOkresuLojalnosciowegoCB" name="zmianaOkresuLojalnosciowego"/>
                    <g:message code="todo" default="Zmiana okresu Lojalnościowego"/>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox id="promocyjneObnizenieNajmuCB" name="promocyjneObnizenieNajmu"/>
                    <g:message code="todo" default="Promocyjne obniżenie najmu"/>
                </div>
            </div>
        </div>


        <div id="dodatkoweFuncjonalnosci" class="expendable">
            <a class="expander expandHeader expanded" href="#">Dodatkowe funkcjonalności</a>

            <div class="content nesting-sub1" style="display: block;">

                <div class="checkBoxBlock">
                    <g:checkBox id="dodaniePrepaidCB" name="dodaniePrepaid"/>
                    <g:message code="todo" default="Dodanie Prepaid"/>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox id="dodanieDccCB" name="dodanieDcc"/>
                    <g:message code="todo" default="Dodanie DCC"/>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox id="dodanieCashBackCB" name="dodanieCashBack"/>
                    <g:message code="todo" default="Dodanie CashBack"/>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox id="dodanieIkoCB" name="DodanieIko"/>
                    <g:message code="todo" default="Dodanie IKO"/>
                </div>


                <div id="pakietSerwisowy" class="checkBoxBlock">
                    <a class="expander expanderSub expanded" href="#">Pakiet serwisowy</a>

                    <div class="content nesting-sub2" style="display: block;">
                        <div class="checkBoxBlock">
                            <g:checkBox id="ekonomicznyCB" name="ekonomiczny"/>
                            <g:message code="todo" default="Ekonomiczny"/>
                        </div>

                        <div class="checkBoxBlock">
                            <g:checkBox id="komfortCB" name="komfort"/>
                            <g:message code="todo" default="Komfort"/>
                        </div>

                        <div class="checkBoxBlock">
                            <g:checkBox id="prestizCB" name="prestiz"/>
                            <g:message code="todo" default="Prestiż"/>
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
        <g:textArea name="myField" style="margin-left: 8px; height: 100px"/>
    </div>


    <nav style="margin-top: 20px">
        <g:form action="create_chooseCalc">
            <fieldset>
                <g:submitButton name="Dalej" class="button submit"/>
            </fieldset>
        </g:form>
    </nav>

</section>

</body>
</html>
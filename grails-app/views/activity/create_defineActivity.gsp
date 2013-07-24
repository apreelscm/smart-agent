<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'activity.label', default: 'Activity')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>

    <g:javascript src="simple-expand.js" />

    <g:javascript>
        var $j = jQuery.noConflict();

        $j(document).ready(function () {
            $j('.expander').simpleexpand();
        });

    </g:javascript>
</head>


<body>

<section id="create-activity" >

    <h1 class="ng linia-bottom">Wybierz działanie</h1>

    <div class="activityTree" style="margin: 10px auto 0 auto;display: table;">
        <div id="nowaUmowa" class="expendable">
            <div class="checkBoxBlock">
                <g:checkBox name="nowaUmowa" />
                <a class="expandHeader expanded" href="#">Nowa umowa</a>
            </div>
        </div>

        <div id="rozszerzenie" class="expendable">
            <a class="expander expandHeader expanded" href="#">Rozszerzenie</a>
            <div class="content nesting-sub1" style="display: block;">
                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Dodatkowy Punkt" />
                </div>
                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPos" />
                    <g:message code="todo" default="Dodatkowy Pos" />
                </div>
            </div>
        </div>

        <div id="zmianaWarunkow" class="expendable">
            <a class="expander expandHeader expanded" href="#">Zmiana warunków handlowych</a>
            <div class="content nesting-sub1" style="display: block;">

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Zmiana prowizji" />
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Zmiana warunków DCC" />
                </div>

                <div class="checkBoxBlock">

                    <a class="expander expanderSub expanded" href="#">Dodanie aneksu Koszty +</a>


                    <div class="content nesting-sub2" style="display: block;">
                        <div class="checkBoxBlock">
                            <g:checkBox name="wymianaUmowyNajmu" />
                            <g:message code="todo" default="Wymiana umowy najmu" />
                        </div>
                        <div class="checkBoxBlock">
                            <g:checkBox name="aneks" />
                            <g:message code="todo" default="Aneks" />
                        </div>
                    </div>
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Zmiana tabeli opłat dodatkowych" />
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Zmiana warunków prepaid" />
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Zmiana okresu Lojalnościowego" />
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Promocyjne obniżenie najmu" />
                </div>
            </div>
        </div>


        <div id="dodatkoweFuncjonalnosci" class="expendable">
            <a class="expander expandHeader expanded" href="#">Dodatkowe funkcjonalności</a>
            <div class="content nesting-sub1" style="display: block;">

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Dodanie Prepaid" />
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Dodanie DCC" />
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Dodanie CashBack" />
                </div>

                <div class="checkBoxBlock">
                    <g:checkBox name="dodatkowyPunkt" />
                    <g:message code="todo" default="Dodanie IKO" />
                </div>


                <div class="checkBoxBlock">
                    <a class="expander expanderSub expanded" href="#"> Pakiet serwisowy</a>

                    <div class="content nesting-sub2" style="display: block;">
                        <div class="checkBoxBlock">
                            <g:checkBox name="wymianaUmowyNajmu" />
                            <g:message code="todo" default="Ekonomiczny" />
                        </div>
                        <div class="checkBoxBlock">
                            <g:checkBox name="aneks" />
                            <g:message code="todo" default="Komfort" />
                        </div>
                        <div class="checkBoxBlock">
                            <g:checkBox name="aneks" />
                            <g:message code="todo" default="Prestiż" />
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <div style="margin:25px auto 0 auto;display: table;position: relative;left: -40px">
        <g:message code="todo" default="Uwagi dla COA:"/>
        <g:textArea name="myField" style="margin-left: 8px; height: 100px"/>
    </div>



    <nav  style="margin-top: 20px">
        <g:form action="create_chooseCalc">
            <fieldset>
                <g:submitButton name="Dalej" class="przycisk-submit"/>
            </fieldset>
        </g:form>
    </nav>

</section>

</body>
</html>
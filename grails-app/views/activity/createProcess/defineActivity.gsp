<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="defineActivity.header.title"/></title>
    <asset:javascript src="apreel/expandable-tree.js"/>

    <script type="text/javascript">
		jQuery('input[type="checkbox"]').on("change", function() {
			jQuery("ul.errors").remove();
		});
    </script>
</head>

<body>

<section id="create-activity">
<h1 class="ng linia-bottom"><g:message code="defineActivity.header.title"/></h1>

    <div id="notesMessageBox">
        <g:if test="${infoMessage}">
            <g:render template="message/infoMessage" model="[message: infoMessage]"/>
        </g:if>
        <g:if test="${errorMessage}">
            <g:render template="message/errorMessage" model="[message: errorMessage]"/>
        </g:if>
    </div>

 <g:form>
     <div class="activityTree" style="margin: 10px auto 0 auto;display: table;">
         <g:set var="activities" value="${processInstance?.activities*.code}"/>
         
         <div id="nowaUmowa" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="nowaUmowaCB" name="nowaUmowa" data-selected="${activities?.contains('nowaUmowa')}"/>
                 <a class="expandHeader expanded" href="#"><label for="nowaUmowaCB"><g:message code="activity.nowaUmowa.name"/> </label></a>
             </div>
         </div>

         <div id="rozszerzenie" class="expendable">
             <a class="expander expandHeader expanded" href="#"><g:message code="activity.rozszerzenie.name"/></a>

             <div class="content nesting-sub1" style="display: block;">
                 <div class="checkBoxBlock">
                     <g:checkBox id="dodatkowyPunktCB" name="dodatkowyPunkt" data-selected="${activities?.contains('dodatkowyPunkt')}"/>
                     <label for="dodatkowyPunktCB"><g:message code="activity.dodatkowyPunkt.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodatkowyPosCB" name="dodatkowyPos" data-selected="${activities?.contains('dodatkowyPos')}"/>
                     <label for="dodatkowyPosCB"><g:message code="activity.dodatkowyPos.name"/></label>
                 </div>
             </div>
         </div>

         <div id="zmianaWarunkow" class="expendable">
             <a class="expander expandHeader expanded" href="#"><g:message code="activity.zmianaWarunkowHandlowych.name"/></a>

             <div class="content nesting-sub1" style="display: block;">

                 <div class="checkBoxBlock">
                     <a class="expander expanderSub expanded" href="#"><g:message code="activity.zmianaProwizjiAneks.name"/></a>
                     <div class="content nesting-sub2" style="display: block;">
                         <div class="checkBoxBlock">
                             <g:checkBox id="wymianaUmowyZaplatyCB" name="wymianaUmowyZaplaty" data-selected="${activities?.contains('wymianaUmowyZaplaty')}"/>
                             <label for="wymianaUmowyZaplatyCB"><g:message code="activity.wymianaUmowyZaplaty.name"/></label>
                         </div>
                         <div class="checkBoxBlock">
                             <!-- 'Aneks' to dawne dzialanie 'Zmiana prowizji' -->
                             <g:checkBox id="zmianaProwizjiCB" name="zmianaProwizji" data-selected="${activities?.contains('zmianaProwizji')}"/>
                             <label for="zmianaProwizjiCB"> <g:message code="activity.zmianaProwizji.name"/></label>
                         </div>
                     </div>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="zmianaWarunkowDccCB" name="zmianaWarunkowDcc" data-selected="${activities?.contains('zmianaWarunkowDcc')}"/>
                     <label for="zmianaWarunkowDccCB"><g:message code="activity.zmianaWarunkowDcc.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodanieAneksuKosztyPlusCB" name="dodanieAneksuKosztyPlus" data-selected="${activities?.contains('dodanieAneksuKosztyPlus')}"/>
                     <label for="dodanieAneksuKosztyPlusCB"><g:message code="activity.dodanieAneksuKosztyPlus.name"/></label>
                 </div>


                 <div class="checkBoxBlock">

                     <a class="expander expanderSub expanded" href="#"><g:message code="activity.zmianaOplatyZaNajemIDodFunkc.name"/></a>

                     <div class="content nesting-sub2" style="display: block;">
                         <div class="checkBoxBlock">
                             <g:checkBox id="wymianaUmowyNajmuCB" name="wymianaUmowyNajmu" data-selected="${activities?.contains('wymianaUmowyNajmu')}"/>
                             <label for="wymianaUmowyNajmuCB"><g:message code="activity.wymianaUmowyNajmu.name"/></label>
                         </div>

                         <div class="checkBoxBlock">
                             <g:checkBox id="aneksCB" name="aneks" data-selected="${activities?.contains('aneks')}"/>
                             <label for="aneksCB"> <g:message code="activity.aneks.name"/></label>
                         </div>
                     </div>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="zmianaWarunkowPrepaidCB" name="zmianaWarunkowPrepaid" data-selected="${activities?.contains('zmianaWarunkowPrepaid')}"/>
                     <label for="zmianaWarunkowPrepaidCB"><g:message code="activity.zmianaWarunkowPrepaid.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="zmianaOkresuLojalnosciowegoCB" name="zmianaOkresuLojalnosciowego" data-selected="${activities?.contains('zmianaOkresuLojalnosciowego')}"/>
                     <label for="zmianaOkresuLojalnosciowegoCB"><g:message code="activity.zmianaOkresuLojalnosciowego.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="promocyjneObnizenieNajmuCB" name="promocyjneObnizenieNajmu" data-selected="${activities?.contains('promocyjneObnizenieNajmu')}"/>
                     <label for="promocyjneObnizenieNajmuCB"> <g:message code="activity.promocyjneObnizenieNajmu.name"/></label>
                 </div>
             </div>
         </div>

         <div id="dodatkoweFuncjonalnosci" class="expendable">
             <a class="expander expandHeader expanded" href="#"><g:message code="activity.dodatkoweFunkcjonalnosci.name"/></a>

             <div class="content nesting-sub1" style="display: block;">

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodaniePrepaidCB" name="dodaniePrepaid" data-selected="${activities?.contains('dodaniePrepaid')}"/>
                     <label for="dodaniePrepaidCB"><g:message code="activity.dodaniePrepaid.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodanieDccCB" name="dodanieDcc" data-selected="${activities?.contains('dodanieDcc')}"/>
                     <label for="dodanieDccCB"><g:message code="activity.dodanieDcc.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodanieCashBackCB" name="dodanieCashBack" data-selected="${activities?.contains('dodanieCashBack')}"/>
                     <label for="dodanieCashBackCB"><g:message code="activity.dodanieCashBack.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="logoKalkulatorSesjaCB" name="logoKalkulatorSesja" data-selected="${activities?.contains('logoKalkulatorSesja')}"/>
                     <label for="logoKalkulatorSesjaCB"><g:message code="activity.logoKalkulatorSesja.name"/></label>
                 </div>


                 <div id="pakietSerwisowy" class="checkBoxBlock">
                     <a class="expander expanderSub expanded" href="#"><g:message code="activity.pakietSerwisowy.name"/></a>

                     <div class="content nesting-sub2" style="display: block;">
                         <div class="checkBoxBlock" id="ekonomiczny">
                             <g:checkBox id="ekonomicznyCB" name="ekonomiczny" data-selected="${activities?.contains('ekonomiczny')}"/>
                             <label for="ekonomicznyCB"><g:message code="activity.ekonomiczny.name"/></label>
                         </div>

                         <div class="checkBoxBlock" id="komfort">
                             <g:checkBox id="komfortCB" name="komfort" data-selected="${activities?.contains('komfort')}"/>
                             <label for="komfortCB"><g:message code="activity.komfort.name"/></label>
                         </div>

                         <div class="checkBoxBlock" id="prestiz">
                             <g:checkBox id="prestizCB" name="prestiz" data-selected="${activities?.contains('prestiz')}"/>
                             <label for="prestizCB"><g:message code="activity.prestiz.name"/></label>
                         </div>
                     </div>
                 </div>
             </div>
         </div>

         %{--<div id="pakiet" class="expendable">--} chwilowo ukryte na prosbe eService%
             %{--<a class="expander expandHeader expanded" href="#"><g:message code="activity.pakiet.name"/></a>--}%

             %{--<div class="content nesting-sub1">--}%
                 %{--<div class="checkBoxBlock">--}%
                     %{--<g:checkBox id="pakietStartCB" name="pakietStart" data-selected="${activities?.contains('pakietStart')}"/>--}%
                     %{--<label for="pakietStartCB"><g:message code="activity.pakietStart.name"/></label>--}%
                 %{--</div>--}%
                 %{--<div class="checkBoxBlock">--}%
                     %{--<g:checkBox id="pakietStartPlusCB" name="pakietStartPlus" data-selected="${activities?.contains('pakietStartPlus')}"/>--}%
                     %{--<label for="pakietStartPlusCB"><g:message code="activity.pakietStartPlus.name"/></label>--}%
                 %{--</div>--}%
                 %{--<div class="checkBoxBlock">--}%
                     %{--<g:checkBox id="pakietMobilnyCB" name="pakietMobilny" data-selected="${activities?.contains('pakietMobilny')}"/>--}%
                     %{--<label for="pakietMobilnyCB"><g:message code="activity.pakietMobilny.name"/></label>--}%
                 %{--</div>--}%
             %{--</div>--}%
         %{--</div>--}%

         <div id="wymianaTerminala" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="wymianaTerminalaCB" name="wymianaTerminala" data-selected="${activities?.contains('wymianaTerminala')}"/>
                 <a class="expandHeader expanded" href="#">  <label for="wymianaTerminalaCB"><g:message code="activity.wymianaTerminala.name"/> </label></a>
             </div>
         </div>

         <div id="poprawDane" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="poprawDaneCB" name="poprawDane" data-selected="${activities?.contains('poprawDane')}"/>
                 <a class="expandHeader expanded" href="#">  <label for="poprawDaneCB"><g:message code="activity.poprawDane.name"/> </label></a>
             </div>
         </div>

         <div id="odrzucDokumenty" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="odrzucDokumentyCB" name="odrzucDokumenty" data-selected="${activities?.contains('odrzucDokumenty')}"/>
                 <a class="expandHeader expanded" href="#"><label for="odrzucDokumentyCB"><g:message code="activity.odrzucDokumenty.name"/> </label></a>
             </div>
         </div>

         <div id="uzupelnijPodpisy" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="uzupelnijPodpisyCB" name="uzupelnijPodpisy" data-selected="${activities?.contains('uzupelnijPodpisy')}"/>
                 <a class="expandHeader expanded" href="#"><label for="uzupelnijPodpisyCB"><g:message code="activity.uzupelnijPodpisy.name"/> </label></a>
             </div>
         </div>
     </div>

     <div style="margin:30px auto 0 auto;display: table;">
         <g:message code="defineActivity.uwagiDoCoa.name"/>
         <g:textArea name="notes" style="height: 100px; display: block; min-width: 380px" value="${notesToCOA}"/>
     </div>


     <nav style="margin-top: 20px">
         <fieldset>
             <g:submitButton name="continue" class="button submit" value="${message(code: 'default.navigation.button.next')}"/>
         </fieldset>
     </nav>
 </g:form>
</section>

</body>
</html>
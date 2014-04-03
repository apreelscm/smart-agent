<%@ page import="com.eservice.eumowy.Activity" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="defineActivity.header.title"/></title>
    <r:require module="expandable_tree"/>

    <r:script>
		jQuery('input[type="checkbox"]').on("change", function() {
			jQuery("ul.errors").remove();
		});
    </r:script>
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

         <div id="nowaUmowa" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="nowaUmowaCB" name="nowaUmowa" data-selected="${(processInstance?.activities*.code)?.contains('nowaUmowa')}"/>
                 <a class="expandHeader expanded" href="#"><label for="nowaUmowaCB"><g:message code="activity.nowaUmowa.name"/> </label></a>
             </div>
         </div>

         <div id="rozszerzenie" class="expendable">
             <a class="expander expandHeader expanded" href="#"><g:message code="activity.rozszerzenie.name"/></a>

             <div class="content nesting-sub1" style="display: block;">
                 <div class="checkBoxBlock">
                     <g:checkBox id="dodatkowyPunktCB" name="dodatkowyPunkt" data-selected="${(processInstance?.activities*.code)?.contains('dodatkowyPunkt')}"/>
                     <label for="dodatkowyPunktCB"><g:message code="activity.dodatkowyPunkt.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodatkowyPosCB" name="dodatkowyPos" data-selected="${(processInstance?.activities*.code)?.contains('dodatkowyPos')}"/>
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
                             <g:checkBox id="wymianaUmowyZaplatyCB" name="wymianaUmowyZaplaty" data-selected="${(processInstance?.activities*.code)?.contains('wymianaUmowyZaplaty')}"/>
                             <label for="wymianaUmowyZaplatyCB"><g:message code="activity.wymianaUmowyZaplaty.name"/></label>
                         </div>
                         <div class="checkBoxBlock">
                             <!-- 'Aneks' to dawne dzialanie 'Zmiana prowizji' -->
                             <g:checkBox id="zmianaProwizjiCB" name="zmianaProwizji" data-selected="${(processInstance?.activities*.code)?.contains('zmianaProwizji')}"/>
                             <label for="zmianaProwizjiCB"> <g:message code="activity.zmianaProwizji.name"/></label>
                         </div>
                     </div>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="zmianaWarunkowDccCB" name="zmianaWarunkowDcc" data-selected="${(processInstance?.activities*.code)?.contains('zmianaWarunkowDcc')}"/>
                     <label for="zmianaWarunkowDccCB"><g:message code="activity.zmianaWarunkowDcc.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodanieAneksuKosztyPlusCB" name="dodanieAneksuKosztyPlus" data-selected="${(processInstance?.activities*.code)?.contains('dodanieAneksuKosztyPlus')}"/>
                     <label for="dodanieAneksuKosztyPlusCB"><g:message code="activity.dodanieAneksuKosztyPlus.name"/></label>
                 </div>


                 <div class="checkBoxBlock">

                     <a class="expander expanderSub expanded" href="#"><g:message code="activity.zmianaOplatyZaNajemIDodFunkc.name"/></a>

                     <div class="content nesting-sub2" style="display: block;">
                         <div class="checkBoxBlock">
                             <g:checkBox id="wymianaUmowyNajmuCB" name="wymianaUmowyNajmu" data-selected="${(processInstance?.activities*.code)?.contains('wymianaUmowyNajmu')}"/>
                             <label for="wymianaUmowyNajmuCB"><g:message code="activity.wymianaUmowyNajmu.name"/></label>
                         </div>

                         <div class="checkBoxBlock">
                             <g:checkBox id="aneksCB" name="aneks" data-selected="${(processInstance?.activities*.code)?.contains('aneks')}"/>
                             <label for="aneksCB"> <g:message code="activity.aneks.name"/></label>
                         </div>
                     </div>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="zmianaTabeliOplatDodatkowychCB" name="zmianaTabeliOplatDodatkowych" data-selected="${(processInstance?.activities*.code)?.contains('zmianaTabeliOplatDodatkowych')}"/>
                     <label for="zmianaTabeliOplatDodatkowychCB"><g:message code="activity.zmianaTabeliOplatDodatkowych.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="zmianaWarunkowPrepaidCB" name="zmianaWarunkowPrepaid" data-selected="${(processInstance?.activities*.code)?.contains('zmianaWarunkowPrepaid')}"/>
                     <label for="zmianaWarunkowPrepaidCB"><g:message code="activity.zmianaWarunkowPrepaid.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="zmianaOkresuLojalnosciowegoCB" name="zmianaOkresuLojalnosciowego" data-selected="${(processInstance?.activities*.code)?.contains('zmianaOkresuLojalnosciowego')}"/>
                     <label for="zmianaOkresuLojalnosciowegoCB"><g:message code="activity.zmianaOkresuLojalnosciowego.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="promocyjneObnizenieNajmuCB" name="promocyjneObnizenieNajmu" data-selected="${(processInstance?.activities*.code)?.contains('promocyjneObnizenieNajmu')}"/>
                     <label for="promocyjneObnizenieNajmuCB"> <g:message code="activity.promocyjneObnizenieNajmu.name"/></label>
                 </div>
             </div>
         </div>

         <div id="dodatkoweFuncjonalnosci" class="expendable">
             <a class="expander expandHeader expanded" href="#"><g:message code="activity.dodatkoweFunkcjonalnosci.name"/></a>

             <div class="content nesting-sub1" style="display: block;">

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodaniePrepaidCB" name="dodaniePrepaid" data-selected="${(processInstance?.activities*.code)?.contains('dodaniePrepaid')}"/>
                     <label for="dodaniePrepaidCB"><g:message code="activity.dodaniePrepaid.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodanieDccCB" name="dodanieDcc" data-selected="${(processInstance?.activities*.code)?.contains('dodanieDcc')}"/>
                     <label for="dodanieDccCB"><g:message code="activity.dodanieDcc.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodanieCashBackCB" name="dodanieCashBack" data-selected="${(processInstance?.activities*.code)?.contains('dodanieCashBack')}"/>
                     <label for="dodanieCashBackCB"><g:message code="activity.dodanieCashBack.name"/></label>
                 </div>

                 <div class="checkBoxBlock">
                     <g:checkBox id="dodanieIkoCB" name="dodanieIko" data-selected="${(processInstance?.activities*.code)?.contains('dodanieIko')}"/>
                     <label for="dodanieIkoCB"><g:message code="activity.dodanieIko.name"/></label>
                 </div>


                 <div id="pakietSerwisowy" class="checkBoxBlock">
                     <a class="expander expanderSub expanded" href="#"><g:message code="activity.pakietSerwisowy.name"/></a>

                     <div class="content nesting-sub2" style="display: block;">
                         <div class="checkBoxBlock" id="ekonomiczny">
                             <g:checkBox id="ekonomicznyCB" name="ekonomiczny" data-selected="${(processInstance?.activities*.code)?.contains('ekonomiczny')}"/>
                             <label for="ekonomicznyCB"><g:message code="activity.ekonomiczny.name"/></label>
                         </div>

                         <div class="checkBoxBlock" id="komfort">
                             <g:checkBox id="komfortCB" name="komfort" data-selected="${(processInstance?.activities*.code)?.contains('komfort')}"/>
                             <label for="komfortCB"><g:message code="activity.komfort.name"/></label>
                         </div>

                         <div class="checkBoxBlock" id="prestiz">
                             <g:checkBox id="prestizCB" name="prestiz" data-selected="${(processInstance?.activities*.code)?.contains('prestiz')}"/>
                             <label for="prestizCB"><g:message code="activity.prestiz.name"/></label>
                         </div>
                     </div>
                 </div>
             </div>
         </div>

         <div id="wymianaTerminala" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="wymianaTerminalaCB" name="wymianaTerminala" data-selected="${(processInstance?.activities*.code)?.contains('wymianaTerminala')}"/>
                 <a class="expandHeader expanded" href="#">  <label for="wymianaTerminalaCB"><g:message code="activity.wymianaTerminala.name"/> </label></a>
             </div>
         </div>

         <div id="poprawDane" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="poprawDaneCB" name="poprawDane" data-selected="${(processInstance?.activities*.code)?.contains('poprawDane')}"/>
                 <a class="expandHeader expanded" href="#">  <label for="poprawDaneCB"><g:message code="activity.poprawDane.name"/> </label></a>
             </div>
         </div>

         <div id="odrzucDokumenty" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="odrzucDokumentyCB" name="odrzucDokumenty" data-selected="${(processInstance?.activities*.code)?.contains('odrzucDokumenty')}"/>
                 <a class="expandHeader expanded" href="#"><label for="odrzucDokumentyCB"><g:message code="activity.odrzucDokumenty.name"/> </label></a>
             </div>
         </div>

         <div id="uzupelnijPodpisy" class="expendable">
             <div class="checkBoxBlock">
                 <g:checkBox id="uzupelnijPodpisyCB" name="uzupelnijPodpisy" data-selected="${(processInstance?.activities*.code)?.contains('uzupelnijPodpisy')}"/>
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
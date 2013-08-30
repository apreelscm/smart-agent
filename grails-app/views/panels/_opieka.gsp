<fieldset class="subpanel-fieldset">
    <legend><g:message code="panel.newpoint.care.title"/> </legend>
    <div class="subpanel-fieldset-centercontent" >
	    <ul class="table-list vertical-center">
	        <li>
	            <span class="align-right"><g:message code="panel.newpoint.care.phgain"/></span><span><g:textField readonly="true" style="width: 45px;" name="points[${id}].phPozysk" id="points[${id}].phGain" value="${pointData?.phPozysk}"/></span>
	            <span class="align-right"><g:message code="panel.newpoint.care.bussinesscare" /></span><span><g:textField style="width: 45px;" name="points[${id}].opiekaBiznesowa" id="points[${id}].businessCare" value="${pointData?.opiekaBiznesowa}"/></span>
	            <span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span><span><g:textField style="width: 45px;" name="points[${id}].opiekaSerwisowaI" id="points[${id}].serviceCare1" value="${pointData?.opiekaSerwisowaI}"/></span>
	            <span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span><span><g:textField style="width: 45px;" name="points[${id}].opiekaSerwisowaII" id="points[${id}].serviceCare2" value="${pointData?.opiekaSerwisowaII}"/></span>
	            <span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span><span><g:textField style="width: 45px;" name="points[${id}].opiekaSerwisowaIII" id="points[${id}].serviceCare3" value="${pointData?.opiekaSerwisowaIII}"/></span>
	        </li>
	    </ul>
    </div>
</fieldset>
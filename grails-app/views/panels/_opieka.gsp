<fieldset class="subpanel-fieldset">
    <legend><g:message code="panel.newpoint.care.title"/> </legend>
    <div class="subpanel-fieldset-centercontent" >
	    <ul class="table-list vertical-center">
	        <li>
	            <span class="align-right"><g:message code="panel.newpoint.care.phgain"/></span>
                <span><eumowy:textField style="width: 45px;" name="${panelType}[${id}].phPozysk" id="${panelType}[${id}].phGain" value="${sec.loggedInUserInfo(field: 'nr')}" readonly="true" /></span>

	            <span class="align-right"><g:message code="panel.newpoint.care.bussinesscare" /></span>
                <span><eumowy:textField style="width: 45px;" name="${panelType}[${id}].opiekaBiznesowa" id="${panelType}[${id}].businessCare" value="${sec.loggedInUserInfo(field: 'nr')}" readonly="true" /></span>

	            <span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span>
                <span><g:textField style="width: 45px;" name="${panelType}[${id}].opiekaSerwisowaI" id="${panelType}[${id}].serviceCare1" value="${pointData?.opiekaSerwisowaI}" maxlength="5" readonly="readonly"/></span>

	            <span class="align-right"><g:message code="panel.newpoint.care.servicecare" /></span>
                <span><g:textField style="width: 45px;" name="${panelType}[${id}].opiekaSerwisowaII" id="${panelType}[${id}].serviceCare2" value="${pointData?.opiekaSerwisowaII}" maxlength="5" readonly="readonly"/></span>

	            <span class="align-right"><g:message code="panel.newpoint.care.installer" /></span>
                <span><g:textField style="width: 45px;" name="${panelType}[${id}].opiekaSerwisowaIII" id="${panelType}[${id}].serviceCare3" value="${pointData?.opiekaSerwisowaIII}" maxlength="5"/></span>
	        </li>
	    </ul>
    </div>
</fieldset>
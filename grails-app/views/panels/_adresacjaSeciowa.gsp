<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.staticaddressdata.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<ul class="table-list vertical-center" >
			<li><span class="align-right" ><g:message code="panel.mask" /></span><span><g:textField name="${panelType}[${id}].maska" id="${panelType}[${id}].staticDeviceMask" value="${pointData?.maska}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.gateway" /></span><span><g:textField name="${panelType}[${id}].bramka" id="${panelType}[${id}].staticDeviceGateway" value="${pointData?.bramka}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.ipaddress" /></span><span><g:textField name="${panelType}[${id}].adresIp" id="${panelType}[${id}].staticDeviceIp" value="${pointData?.adresIp}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.techniciancontact" /></span><span><g:textField name="${panelType}[${id}].kontaktInformatykStatyczna" id="${panelType}[${id}].staticDeviceSupportContact" value="${pointData?.kontaktInformatykStatyczna}"/></span></li>
			<li><span class="align-right" ><g:select name="${panelType}[${id}].tytulInformatykStatyczna" from="['', 'Pan','Pani']" valueMessagePrefix="person.title" value="${pointData?.tytulInformatykStatyczna}"  style="width: 60px"/><g:message code="panel.first.name" /></span><span><g:textField name="imieInformatykStatyczna" id="staticDeviceSupportContactName" value="${pointData?.imieInformatykStatyczna}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.last.name" /></span><span><g:textField name="${panelType}[${id}].nazwiskoInformatykStatyczna" id="${panelType}[${id}].staticDeviceSupportContactSurname" value="${pointData?.nazwiskoInformatykStatyczna}"/></span></li>
		</ul>
	</div>
</fieldset>
<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.dynamicaddressdata.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<ul class="table-list vertical-center" >
			<li><span class="align-right" ><g:message code="panel.techniciancontact" /></span><span><g:textField name="${panelType}[${id}].kontaktInformatykDynamiczna" id="${panelType}[${id}].dynamicDeviceSupportContact" value="${pointData?.kontaktInformatykDynamiczna}"/></span></li>
			<li><span class="align-right" ><g:select name="${panelType}[${id}].tytulInformatykDynamiczna" from="['', 'Pan','Pani']" valueMessagePrefix="person.title" value="${pointData?.tytulInformatykDynamiczna}"  style="width: 60px"/><g:message code="panel.first.name" /></span><span><g:textField name="${panelType}[${id}].imieInformatykDynamiczna" id="${panelType}[${id}].dynamicDeviceSupportName" value="${pointData?.imieInformatykDynamiczna}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.last.name" /></span><span><g:textField name="${panelType}[${id}].nazwiskoInformatykDynamiczna" id="${panelType}[${id}].dynamicDeviceSupportSurname" value="${pointData?.nazwiskoInformatykDynamiczna}"/></span></li>
		</ul>
	</div>
</fieldset>
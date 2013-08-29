<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.staticaddressdata.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<ul class="table-list vertical-center" >
			<li><span class="align-right" ><g:message code="panel.mask" /></span><span><g:textField name="points[${id}].maska" id="points[${id}].staticDeviceMask" /></span></li>
			<li><span class="align-right" ><g:message code="panel.gateway" /></span><span><g:textField name="points[${id}].bramka" id="points[${id}].staticDeviceGateway" /></span></li>
			<li><span class="align-right" ><g:message code="panel.ipaddress" /></span><span><g:textField name="points[${id}].adresIp" id="points[${id}].staticDeviceIp" /></span></li>
			<li><span class="align-right" ><g:message code="panel.techniciancontact" /></span><span><g:textField name="points[${id}].kontaktInformatykStatyczna" id="points[${id}].staticDeviceSupportContact" /></span></li>
			<li><span class="align-right" ><select style="width: 50px"></select> <g:message code="panel.first.name" /></span><span><g:textField name="imieInformatykStatyczna" id="staticDeviceSupportContactName" /></span></li>
			<li><span class="align-right" ><g:message code="panel.last.name" /></span><span><g:textField name="points[${id}].nazwiskoInformatykStatyczna" id="points[${id}].staticDeviceSupportContactSurname" /></span></li>
		</ul>
	</div>
</fieldset>
<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.dynamicaddressdata.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<ul class="table-list vertical-center" >
			<li><span class="align-right" ><g:message code="panel.techniciancontact" /></span><span><g:textField name="points[${id}].kontaktInformatykDynamiczna" id="points[${id}].dynamicDeviceSupportContact" /></span></li>
			<li><span class="align-right" ><select style="width: 50px"></select> <g:message code="panel.first.name" /></span><span><g:textField name="points[${id}].imieInformatykDynamiczna" id="points[${id}].dynamicDeviceSupportName" /></span></li>
			<li><span class="align-right" ><g:message code="panel.last.name" /></span><span><g:textField name="points[${id}].nazwiskoInformatykDynamiczna" id="points[${id}].dynamicDeviceSupportSurname" /></span></li>
		</ul>
	</div>
</fieldset>
<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.staticaddressdata.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<ul class="table-list vertical-center" >
			<li><span class="align-right" ><g:message code="panel.mask" /></span><span><g:textField name="points[${id}].maska" id="points[${id}].staticDeviceMask" value="${pointData?.maska}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.gateway" /></span><span><g:textField name="points[${id}].bramka" id="points[${id}].staticDeviceGateway" value="${pointData?.bramka}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.ipaddress" /></span><span><g:textField name="points[${id}].adresIp" id="points[${id}].staticDeviceIp" value="${pointData?.adresIp}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.techniciancontact" /></span><span><g:textField name="points[${id}].kontaktInformatykStatyczna" id="points[${id}].staticDeviceSupportContact" value="${pointData?.kontaktInformatykStatyczna}"/></span></li>
			<li><span class="align-right" ><g:select name="points[${id}].tytulInformatykStatyczna" from="['', 'Pan','Pani']" valueMessagePrefix="person.title" value="${pointData?.tytulInformatykStatyczna}"  style="width: 50px"/><g:message code="panel.first.name" /></span><span><g:textField name="imieInformatykStatyczna" id="staticDeviceSupportContactName" value="${pointData?.imieInformatykStatyczna}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.last.name" /></span><span><g:textField name="points[${id}].nazwiskoInformatykStatyczna" id="points[${id}].staticDeviceSupportContactSurname" value="${pointData?.nazwiskoInformatykStatyczna}"/></span></li>
		</ul>
	</div>
</fieldset>
<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.dynamicaddressdata.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<ul class="table-list vertical-center" >
			<li><span class="align-right" ><g:message code="panel.techniciancontact" /></span><span><g:textField name="points[${id}].kontaktInformatykDynamiczna" id="points[${id}].dynamicDeviceSupportContact" value="${pointData?.kontaktInformatykDynamiczna}"/></span></li>
			<li><span class="align-right" ><g:select name="points[${id}].tytulInformatykDynamiczna" from="['', 'Pan','Pani']" valueMessagePrefix="person.title" value="${pointData?.tytulInformatykDynamiczna}"  style="width: 50px"/><g:message code="panel.first.name" /></span><span><g:textField name="points[${id}].imieInformatykDynamiczna" id="points[${id}].dynamicDeviceSupportName" value="${pointData?.imieInformatykDynamiczna}"/></span></li>
			<li><span class="align-right" ><g:message code="panel.last.name" /></span><span><g:textField name="points[${id}].nazwiskoInformatykDynamiczna" id="points[${id}].dynamicDeviceSupportSurname" value="${pointData?.nazwiskoInformatykDynamiczna}"/></span></li>
		</ul>
	</div>
</fieldset>
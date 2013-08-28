<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.staticaddressdata.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<ul class="table-list vertical-center" >
			<li><span class="align-right" ><g:message code="panel.mask" /></span><span><g:textField name="%ID%maska" id="%ID%staticDeviceMask" /></span></li>
			<li><span class="align-right" ><g:message code="panel.gateway" /></span><span><g:textField name="%ID%bramka" id="%ID%staticDeviceGateway" /></span></li>
			<li><span class="align-right" ><g:message code="panel.ipaddress" /></span><span><g:textField name="%ID%adresIp" id="%ID%staticDeviceIp" /></span></li>
			<li><span class="align-right" ><g:message code="panel.techniciancontact" /></span><span><g:textField name="%ID%kontaktInformatykStatyczna" id="%ID%staticDeviceSupportContact" /></span></li>
			<li><span class="align-right" ><select style="width: 50px"></select> <g:message code="panel.first.name" /></span><span><g:textField name="imieInformatykStatyczna" id="staticDeviceSupportContactName" /></span></li>
			<li><span class="align-right" ><g:message code="panel.last.name" /></span><span><g:textField name="%ID%nazwiskoInformatykStatyczna" id="%ID%staticDeviceSupportContactSurname" /></span></li>
		</ul>
	</div>
</fieldset>
<fieldset class="subpanel-fieldset">
	<legend><g:message code="panel.newpoint.dynamicaddressdata.title" /></legend>
	<div class="subpanel-fieldset-centercontent" >
		<ul class="table-list vertical-center" >
			<li><span class="align-right" ><g:message code="panel.techniciancontact" /></span><span><g:textField name="%ID%kontaktInformatykStatyczna" id="%ID%dynamicDeviceSupportContact" /></span></li>
			<li><span class="align-right" ><select style="width: 50px"></select> <g:message code="panel.first.name" /></span><span><g:textField name="%ID%imieInformatykDynamiczna" id="%ID%dynamicDeviceSupportName" /></span></li>
			<li><span class="align-right" ><g:message code="panel.last.name" /></span><span><g:textField name="%ID%nazwiskoInformatykDynamiczna" id="%ID%dynamicDeviceSupportSurname" /></span></li>
		</ul>
	</div>
</fieldset>
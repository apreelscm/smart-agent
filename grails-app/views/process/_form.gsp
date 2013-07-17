<%@ page import="com.eservice.eumowy.Process" %>



<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'calcNumber', 'error')} ">
	<label for="calcNumber">
		<g:message code="process.calcNumber.label" default="Calc Number" />
		
	</label>
	<g:textField name="calcNumber" value="${processInstance?.calcNumber}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'clientName', 'error')} ">
	<label for="clientName">
		<g:message code="process.clientName.label" default="Client Name" />
		
	</label>
	<g:textField name="clientName" value="${processInstance?.clientName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'clientNip', 'error')} ">
	<label for="clientNip">
		<g:message code="process.clientNip.label" default="Client Nip" />
		
	</label>
	<g:textField name="clientNip" value="${processInstance?.clientNip}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'phFirstName', 'error')} ">
	<label for="phFirstName">
		<g:message code="process.phFirstName.label" default="Ph First Name" />
		
	</label>
	<g:textField name="phFirstName" value="${processInstance?.phFirstName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'phNumber', 'error')} ">
	<label for="phNumber">
		<g:message code="process.phNumber.label" default="Ph Number" />
		
	</label>
	<g:textField name="phNumber" value="${processInstance?.phNumber}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'phSurname', 'error')} ">
	<label for="phSurname">
		<g:message code="process.phSurname.label" default="Ph Surname" />
		
	</label>
	<g:textField name="phSurname" value="${processInstance?.phSurname}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'saleSection', 'error')} ">
	<label for="saleSection">
		<g:message code="process.saleSection.label" default="Sale Section" />
		
	</label>
	<g:textField name="saleSection" value="${processInstance?.saleSection}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'status', 'error')} ">
	<label for="status">
		<g:message code="process.status.label" default="Status" />
		
	</label>
	<g:select name="status" from="${com.eservice.eumowy.Process$ProcessStatus?.values()}" keys="${com.eservice.eumowy.Process$ProcessStatus.values()*.name()}" value="${processInstance?.status?.name()}" noSelection="['': '']"/>
</div>


<%@ page import="com.eservice.eumowy.Test" %>



<div class="fieldcontain ${hasErrors(bean: testInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="test.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${testInstance?.name}"/>
</div>


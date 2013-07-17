
<%@ page import="com.eservice.eumowy.Process" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'process.label', default: 'Process')}" />
		<title>${entityName}</title>
	</head>
	<body>
		<a href="#show-process" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><g:link class="list" action="list"><g:message code="process.list.label" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-process" class="content scaffold-show" role="main">
			<h1>${entityName}</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list process">
			
				<g:if test="${processInstance?.calcNumber}">
				<li class="fieldcontain">
					<span id="calcNumber-label" class="property-label"><g:message code="process.calcNumber.label" default="Calc Number" /></span>
					
						<span class="property-value" aria-labelledby="calcNumber-label"><g:fieldValue bean="${processInstance}" field="calcNumber"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processInstance?.clientName}">
				<li class="fieldcontain">
					<span id="clientName-label" class="property-label"><g:message code="process.clientName.label" default="Client Name" /></span>
					
						<span class="property-value" aria-labelledby="clientName-label"><g:fieldValue bean="${processInstance}" field="clientName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processInstance?.clientNip}">
				<li class="fieldcontain">
					<span id="clientNip-label" class="property-label"><g:message code="process.clientNip.label" default="Client Nip" /></span>
					
						<span class="property-value" aria-labelledby="clientNip-label"><g:fieldValue bean="${processInstance}" field="clientNip"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="process.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${processInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${processInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="process.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${processInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${processInstance?.phFirstName}">
				<li class="fieldcontain">
					<span id="phFirstName-label" class="property-label"><g:message code="process.phFirstName.label" default="Ph First Name" /></span>
					
						<span class="property-value" aria-labelledby="phFirstName-label"><g:fieldValue bean="${processInstance}" field="phFirstName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processInstance?.phNumber}">
				<li class="fieldcontain">
					<span id="phNumber-label" class="property-label"><g:message code="process.phNumber.label" default="Ph Number" /></span>
					
						<span class="property-value" aria-labelledby="phNumber-label"><g:fieldValue bean="${processInstance}" field="phNumber"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processInstance?.phSurname}">
				<li class="fieldcontain">
					<span id="phSurname-label" class="property-label"><g:message code="process.phSurname.label" default="Ph Surname" /></span>
					
						<span class="property-value" aria-labelledby="phSurname-label"><g:fieldValue bean="${processInstance}" field="phSurname"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processInstance?.saleSection}">
				<li class="fieldcontain">
					<span id="saleSection-label" class="property-label"><g:message code="process.saleSection.label" default="Sale Section" /></span>
					
						<span class="property-value" aria-labelledby="saleSection-label"><g:fieldValue bean="${processInstance}" field="saleSection"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="process.status.label" default="Status" /></span>
					
						<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${processInstance}" field="status"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${processInstance?.id}" />

                    <sec:ifAnyGranted roles="ADM_ROLE">
                        <g:actionSubmit class="save" action="accept" value="Zaakceptuj"
                                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                        <g:actionSubmit class="delete" action="reject" value="Odrzuć"
                                        formnovalidate=""
                                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>

                    <sec:ifAnyGranted roles="PH_ROLE">
                        <g:link class="edit" action="edit" id="${processInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>

				</fieldset>

			</g:form>
		</div>
	</body>
</html>

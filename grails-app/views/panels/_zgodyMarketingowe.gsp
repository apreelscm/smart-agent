<%@ page contentType="text/html;charset=UTF-8" %>
<div id="marketingConsentsPanel">
    <fieldset form="panelsForm">
        <div class="belka-glowna"><g:message code="panel.marketingConsents.title"/></div>
        <div class="centre" style="text-align: center; padding-top: 20px; width: 800px">
            <div style="text-align: left; padding-bottom: 10px;">
                <h1><g:message code="panel.marketingConsents.greeting"/></h1>
                <p><g:message code="panel.marketingConsents.description"/></p>
                <p><g:message code="panel.marketingConsents.descriptionAgree"/></p>
                <div class="${hasErrors(bean:data, field:'consentsChannelAll','errorSpan')}">
                    <ul>
                        <li style="padding-left: 0">
                            <label for="consentsChannelAll">
                                <g:checkBox id="consentsChannelAll" name="consentsChannelAll"
                                            checked="${data.consentsChannelAll}"/>
                                <g:message code="panel.marketingConsents.consentsChannelAll"/>
                            </label>
                            <ul>
                                <li>
                                    <label for="consentsChannelClientPortal">
                                        <g:checkBox id="consentsChannelClientPortal" name="consentsChannelClientPortal"
                                                    checked="${data.consentsChannelClientPortal}"/>
                                        <g:message code="panel.marketingConsents.consentsChannelClientPortal"/>
                                    </label>
                                </li>
                                <li>
                                    <label for="consentsChannelEmail">
                                        <g:checkBox id="consentsChannelEmail" name="consentsChannelEmail"
                                                    checked="${data.consentsChannelEmail}"/>
                                        <g:message code="panel.marketingConsents.consentsChannelEmail"/>
                                    </label>
                                </li>
                                <li>
                                    <label for="consentsChannelSMS">
                                        <g:checkBox id="consentsChannelSMS" name="consentsChannelSMS"
                                                    checked="${data.consentsChannelSMS}"/>
                                        <g:message code="panel.marketingConsents.consentsChannelSMS"/>
                                    </label>
                                </li>
                                <li>
                                    <label for="consentsChannelPhone">
                                        <g:checkBox id="consentsChannelPhone" name="consentsChannelPhone"
                                                    checked="${data.consentsChannelPhone}"/>
                                        <g:message code="panel.marketingConsents.consentsChannelPhone"/>
                                    </label>
                                </li>
                            </ul>
                        </li>
                        <li style="padding-left: 0">
                            <label for="consentsChannelNone">
                                <g:checkBox id="consentsChannelNone" name="consentsChannelNone"
                                            checked="${data.consentsChannelNone}"/>
                                <g:message code="panel.marketingConsents.consentsChannelNone"/>
                            </label>
                        </li>
                    </ul>
                </div>
                <p class="small"><g:message code="panel.marketingConsents.descriptionFootnotes"/></p>
            </div>
        </div>
    </fieldset>
</div>
<script>
    EUMOWY.Panels.MarketingConsents('#marketingConsentsPanel').mount()
</script>
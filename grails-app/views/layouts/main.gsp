<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests" />
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${assetPath(src: 'favicon.png')}" type="image/x-icon">

    <g:if test="${params.controller == 'process'}">
        <asset:stylesheet src="default-process.css"/>
    </g:if>
    <g:else>
        <asset:stylesheet src="default.css"/>
    </g:else>
    <asset:stylesheet src="icons.css"/>

    <asset:javascript src="application.js"/>
    <asset:javascript src="eumowy-lib.js" type="module"/>

    <g:if test="${params.controller != 'login' && grails.util.Environment.getCurrentEnvironment() != grails.util.Environment.DEVELOPMENT}">
        <asset:javascript src="apreel/session_utils.js"/>
    </g:if>

    <g:layoutHead/>
</head>

    <body id="mainBody">
        <header id="mainHeader" style="position:relative">
            <figure id="smallLogo"/>
            <sec:ifLoggedIn>
                <div class="userInfoBar">
                    <g:set var="phFullName" value="${sec.loggedInUserInfo(field: 'name')}"/>
                    <g:set var="phId" value="${sec.loggedInUserInfo(field: 'nr')}"/>
                    <span id="userNameLabel">${phFullName}</span>
                    <span id="userIdLabel">${phId}</span>
                </div>
            </sec:ifLoggedIn>

            <sec:ifLoggedIn>
                <ul id="mainMenu">
                    <li><a href="#" class="submit">Menu</a>
                        <ul>
                            <li><a id="saveProcessLink" href="#" style="display: none;"><g:message code="save.label"/></a></li>
                            <li><a id="logoutLink" href="#"><g:message code="logout.label"/></a>
                            </li>
                        </ul></li>
                </ul>
            </sec:ifLoggedIn>
        </header>

        <g:layoutBody/>

        <footer class="rel" id="stopka">
            <build:buildInfo/> ENV: ${grails.util.Environment.getCurrentEnvironment().name}
        </footer>

        <g:if test="${params.controller != 'login'}">
            <p id="clock"/>
        </g:if>

        <div id="loadingDialog" style="display: none;">
            <p><g:message code="loading"/></p>
        </div>

        <div id="confirm-logout-dialog" style="display: none;">
            <p><g:message code="logout.confirm"/></p>
        </div>
    </body>

    <script type="text/javascript">
        var $j = jQuery.noConflict();
        $j(function(){
            $j("#logoutLink").click(function() {
                $j("#confirm-logout-dialog").dialog({
                    resizable: true,
                    height:200,
                    width: 450,
                    modal: true,
                    buttons:
                    {
                        "Tak": function() {
                            $j( this ).dialog( "close" );
                            window.location.href = '<g:createLink controller="logout"/>'

                        },
                        "Nie": function() {
                            $j( this ).dialog( "close" );
                        }
                    }
                })
                return false
            })
        })
    </script>
</html>

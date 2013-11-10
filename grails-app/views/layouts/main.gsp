<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.png')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">
    <g:if test="${params.controller == 'process'}">
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'default-process.css')}" type="text/css">
    </g:if>
    <g:else>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'default.css')}" type="text/css">
    </g:else>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile/mobile.css')}" type="text/css">

    <g:javascript library="jquery" plugin="jquery"/>
    <r:require module="jquery_ui" />


    <r:require module="modernizr"/>
    <g:if test="${params.controller != 'login' && grails.util.Environment.getCurrentEnvironment() != grails.util.Environment.DEVELOPMENT}">
        <r:require module="session_utils"/>
    </g:if>

    <r:require module="expandable_menu"/>
    <r:require module="jquery_ui" />
    <r:require module="jquery_timepicker_pl" />
    <r:require module="jquery_datepicker_pl" />

    <r:layoutResources/>
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
                    <li><a id="saveProcessLink" href="#" style="display: none;">Zapisz</a></li>
                    <li><a id="logoutLink" href="#">Wyloguj</a>
                    </li>
                </ul></li>
        </ul>
    </sec:ifLoggedIn>

</header>

<g:layoutBody/>

<footer class="rel" id="stopka">
    <g:if test="${grails.util.Environment.getCurrentEnvironment() != grails.util.Environment.PRODUCTION}">
        <build:buildInfo/> ENV: ${grails.util.Environment.getCurrentEnvironment().name}
    </g:if>

</footer>

<g:if test="${params.controller != 'login'}">
    <p id="clock"/>
</g:if>

%{--<div id="spinner" class="spinner">
    <g:message code="spinner.alt" default="Proszę czekać..."/>
</div>--}%

<div id="loadingDialog" style="display: none;">
    <p><g:message code="loading" default="Trwa ładowanie danych..."/></p>
</div>

<div id="confirm-logout-dialog"  style="display: none;">
    <p><g:message code="logout.confirm" /></p>
</div>

<g:javascript library="application"/>
<g:javascript>

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

</g:javascript>

<r:layoutResources/>

</body>
</html>
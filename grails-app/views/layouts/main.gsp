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

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'default-properties.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'font-properties.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'layout-properties.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'table-properties.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'eumowy.css')}" type="text/css">

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile/mobile.css')}" type="text/css">

    <r:require module="modernizr" />
    %{--<g:javascript library="prototype" />--}%
    <g:javascript library="jquery" />
    <r:layoutResources />
    <g:layoutHead/>
</head>


<body class="mainBody">

<div class="mainContent">

    <header style="position:relative;" class="mainHeader">

        <figure id="smallLogo"/>

        <sec:ifLoggedIn>
            <div class="userInfoBar">
                %{--   <g:link controller="logout">Wyloguj</g:link>--}%
                <g:set var="phFullName" value="${sec.loggedInUserInfo(field: 'name')}" />
                <g:set var="phId" value="${sec.loggedInUserInfo(field: 'id')}" />
                <span id="userNameLabel">${phFullName}</span>
                <span id="userIdLabel">${phId}</span>

            </div>
        </sec:ifLoggedIn>

    %{--<input class="przycisk-zaloguj" type="button" style="position:relative;bottom: 0;left:100px;">--}%

        <style type="text/css">
            /* menu styles */
        #jsddm
        {	margin: 0;
            padding: 0;
        }

        #jsddm li
        {	float: left;
            list-style: none;
            font: 12px Tahoma, Arial;
            text-align: center;
        }

        #jsddm li a
        {	display: block;
            background: #db7b20;
            padding: 5px 12px;
            text-decoration: none;
            border-right: 1px solid white;
            width: 70px;
            color: #EAFFED;
            white-space: nowrap;
            font-size: 15px;

        }

        #jsddm li a:hover
        {	background: #af631a
        }

        #jsddm li ul
        {	margin: 0;
            padding: 0;
            position: absolute;
            visibility: hidden;
            border-top: 1px solid white}

        #jsddm li ul li
        {
            width: 100px;
            margin-left: 10px;
            display: inline;
            text-align: center;
        }

        #jsddm li ul li a
        {	width: auto;
            color: #EAFFED;
            background: #F48923;
            font-size: 13px;
        }

        #jsddm li ul li a:hover
        {	background: #ff9024
        }
        </style>

        <g:javascript>
            var timeout = 500;
            var closetimer = 0;
            var ddmenuitem = 0;
            var $j = jQuery.noConflict();

            function jsddm_open() {
                jsddm_canceltimer();
                jsddm_close();
                ddmenuitem = $j(this).find('ul').eq(0).css('visibility', 'visible');
            }

            function jsddm_close() {
                if (ddmenuitem) ddmenuitem.css('visibility', 'hidden');
            }

            function jsddm_timer() {
                closetimer = window.setTimeout(jsddm_close, timeout);
            }

            function jsddm_canceltimer() {
                if (closetimer) {
                    window.clearTimeout(closetimer);
                    closetimer = null;
                }
            }

            $j(document).ready(function () {
                $j('#jsddm > li').bind('mouseover', jsddm_open);
                $j('#jsddm > li').bind('mouseout', jsddm_timer);
            });

            document.onclick = jsddm_close;

        </g:javascript>

			<sec:ifLoggedIn>
				<ul id="jsddm"
					style="position: relative; bottom: -5px; left: -10px;">
					<li><a href="#">Menu</a>
						<ul>
							<li><a href="${createLink(action: 'list')}">Zapisz</a></li>
							<li><a href="${createLink(controller: 'logout')}">Wyloguj</a>
							</li>
						</ul>
					</li>
				</ul>
			</sec:ifLoggedIn>

		</header>


    <section class="mainContainer">
        <g:layoutBody/>
    </section>

    %{--  <footer class="footer" id="stopka">
          <div class="pko-bp">
              <a class="spolka" href="http://www.pkobp.pl/firmy-wspolnoty-spoldzielnie/firmy/" target="_blank" title="Spółka należy do Grupy Kapitałowej PKO Banku Polskiego">Spółka należy do Grupy Kapitałowej PKO Banku Polskiego</a>
          </div>
          <div class="bubbleInfo1" style="width: 450px; float: right;">
              <p>Operator serwisu: <b><a href="http://www.eservice.com.pl/" target="_blank">eService S.A.</a></b> Centrum Elektronicznych Usług Płatniczych</p>
              <p><span>Realizacja: <a href="http://www.apreel.pl/" target="_blank" title="apreel">apreel</a></span></p>
          </div>
      </footer>--}%

    <footer class="rel" id="stopka">


  %{--  <div style="float: left;">
            <div class="pko-bp">
                <a class="spolka" href="http://www.pkobp.pl/firmy-wspolnoty-spoldzielnie/firmy/" target="_blank" title="Spółka należy do Grupy Kapitałowej PKO Banku Polskiego">Spółka należy do Grupy Kapitałowej PKO Banku Polskiego</a>
            </div>
        </div>

        <div class="bubbleInfo1" style="width: 450px; float: right; margin-top: 1em">
            <div style="float: right">
                <p class="rel">Operator serwisu: <b><a  class="trigger" href="http://www.eservice.com.pl/" target="_blank">eService S.A.</a></b> Centrum Elektronicznych Usług Płatniczych</p>
            </div>

            <p style="float: right">
                <span>Realizacja: <a href="http://www.apreel.pl/" target="_blank" title="apreel">apreel</a></span>
            </p>
        </div>--}%

    </footer>

    <div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
    <g:javascript library="application"/>
    <r:layoutResources />

</div>

</body>
</html>
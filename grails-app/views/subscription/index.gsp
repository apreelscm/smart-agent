<!DOCTYPE html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="subscription.title" /></title>
    <r:require module="signaturepad" />
</head>
<body>
<r:script>

    jQuery(document).ready(function() {
      jQuery('.sigPad').signaturePad({errorMessageDraw: '<g:message code="subscription.error" />'});
    });

</r:script>


<section id="index-subscription">
    <h1 class="ng linia-bottom">Podpis</h1>

    <h3 style="margin-top: 20px">REPREZENTANT</h3>

    <g:form  action="save" class="sigPad">
        <p>
            <g:checkBox name="agreement"/>
            <label for="agreement">
                <g:message code="subscription.agreement" />
            </label>
        </p>

        <div class="sig sigWrapper" style="margin-top: 20px">
            <div class="typed"></div>
            <canvas id="pad" class="pad" width="600" height="250"></canvas>
            <input type="hidden" name="subscription" class="output">
        </div>


        <fieldset style="margin-top: 20px;">
            <a href="#clear" class="button action clearButton"><g:message code="subscription.clear" /></a>
            <g:submitButton name="Złożono podpis" class="button submit"/>
        </fieldset>

    </g:form>
</section>
</body>

<g:set var="firstNames" value="${[""] + representativesBisnode?.collect {it.firstName}}"/>
<g:set var="lastNames" value="${[""] + representativesBisnode?.collect {it.lastName}}"/>

<g:each in="${0..2}">
    <g:render template="/common/representative" model="['prefix': 'representatives', 'seqNo': it, 'dropdowns': true]"/>
</g:each>

<script type="text/javascript">
    var representatives = {};

    <g:each in="${representativesBisnode}" var="representative" status="i">
        representatives[${i}] = {title: '${representative.title}', fistName: '${representative.firstName}', lastName: '${representative.lastName}', position: '${representative.position}'};
    </g:each>

    jQuery(".imieField, .nazwiskoField").change(function() {
        var $this = jQuery(this),
            selectedOptionNo = $this[0].selectedIndex,
            parentDiv = $this.parent('div'),
            firstNameSelect = parentDiv.find('.imieField'),
            lastNameSelect = parentDiv.find('.nazwiskoField'),
            titleInput = parentDiv.find('.tytulField'),
            positionInput = parentDiv.find('.positionField');

        firstNameSelect[0].selectedIndex = selectedOptionNo;
        lastNameSelect[0].selectedIndex = selectedOptionNo;

        if(selectedOptionNo === 0) {
            positionInput.val('');
            titleInput.val('')
        } else {
            positionInput.val(representatives[selectedOptionNo - 1].position);
            titleInput.val(representatives[selectedOptionNo - 1].title);
        }
    });
</script>
(function () {
    'use strict';

    var $pointIsAcceptedPrepayments = jQuery("input[type=radio][name$='isAcceptedPrepayments']");

    $pointIsAcceptedPrepayments.change(onScoringChange);
})();

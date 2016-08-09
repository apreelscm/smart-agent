(function ($) {
    $(function() {
        var updateDateFrom =  $(".updateDateFromDatepicker"),
            updateDateTo =  $(".updateDateToDatepicker"),
            createDateFrom =  $(".createDateFromDatepicker"),
            createDateTo =  $(".createDateToDatepicker"),
            searchDateFrom =  $("#filterDateFromDF"),
            searchDateTo =  $("#filterDateToDF"),
            reportDialog = $("#reportDialog");

        updateDateFrom.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});
        updateDateTo.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});
        createDateFrom.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});
        createDateTo.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});
        searchDateFrom.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});
        searchDateTo.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});

        updateDateTo.bind('input change',function(e){
            updateDateFrom.datepicker( "option", "maxDate", $(this).datepicker( "getDate" ) );
        });

        updateDateFrom.bind('input change',function(e){
            updateDateTo.datepicker( "option", "minDate", $(this).datepicker( "getDate" ) );
        });

        createDateTo.bind('input change',function(e){
            createDateFrom.datepicker( "option", "maxDate", $(this).datepicker( "getDate" ) );
        });

        createDateFrom.bind('input change',function(e){
            createDateTo.datepicker( "option", "minDate", $(this).datepicker( "getDate" ) );
        });

        searchDateTo.bind('input change',function(e){
            searchDateFrom.datepicker( "option", "maxDate", $(this).datepicker( "getDate" ) );
        });

        searchDateFrom.bind('input change',function(e){
            searchDateTo.datepicker( "option", "minDate", $(this).datepicker( "getDate" ) );
        });

        $("#invalidateCache").on("click", function(e){
            e.preventDefault();
            $.get($(this).attr('href'), function() {
                alert("Cache został wyczyszczony");
            });
        });

        $("#generateReportButton").on("click", function() {
            reportDialog.dialog({
                width: 550,
                modal: true
            });
        });

        $("#generateReport").on('click', function() {
            reportDialog.dialog("close");
        });
    });

}(jQuery));
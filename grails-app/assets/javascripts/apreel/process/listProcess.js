(function ($) {
    $(function() {
        var updateDateFrom =  $(".updateDateFromDatepicker"),
            updateDateTo =  $(".updateDateToDatepicker"),
            createDateFrom =  $(".createDateFromDatepicker"),
            createDateTo =  $(".createDateToDatepicker"),
            reportDialog = $("#reportDialog");

        updateDateFrom.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});
        updateDateTo.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});
        createDateFrom.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});
        createDateTo.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});

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
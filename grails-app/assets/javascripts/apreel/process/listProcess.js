(function ($) {
    $(function() {
        var dateFrom =  $(".dateFromDatepicker"),
            dateTo =  $(".dateToDatepicker"),
            reportDialog = $("#reportDialog");

        dateFrom.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});
        dateTo.datepicker({ dateFormat: 'dd-mm-yy', maxDate: new Date()});

        dateTo.bind('input change',function(e){
            dateFrom.datepicker( "option", "maxDate", $(this).datepicker( "getDate" ) );
        });

        dateFrom.bind('input change',function(e){
            dateTo.datepicker( "option", "minDate", $(this).datepicker( "getDate" ) );
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
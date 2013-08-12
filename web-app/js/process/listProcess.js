(function ($) {

    $(function() {

        var dateFrom =  $("#filterDateFromDF");
        var dateTo =  $("#filterDateToDF");

        dateFrom.datepicker({ dateFormat: 'dd-mm-yy'});
        dateTo.datepicker({ dateFormat: 'dd-mm-yy'});

        dateFrom.datepicker( "setDate", "-30" );
        dateTo.datepicker( "setDate", new Date() );


        dateTo.bind('input change',function(e){
            dateFrom.datepicker( "option", "maxDate", $(this).datepicker( "getDate" ) );
        });

        dateFrom.bind('input change',function(e){
            dateTo.datepicker( "option", "minDate", $(this).datepicker( "getDate" ) );
        });

    }); //end ready

}(jQuery));
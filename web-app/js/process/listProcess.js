(function ($) {

    $(function() {

        var dateFrom =  $("#filterDateFromDF");
        var dateTo =  $("#filterDateToDF");

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
        })


    }); //end ready

}(jQuery));
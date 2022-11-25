jQuery(function($){
    $.datepicker.regional['pl'] = {
        closeText: 'Zakończ',
        prevText: 'Poprz.',
        nextText: 'Nast.',
        currentText: 'Dziś',
        monthNames: ['Styczeń','Luty','Marzec','Kwiecień','Maj','Czerwiec','Lipiec','Sierpień','Wrzesień','Październik','Listopad','Grudzień'],
        monthNamesShort: ['Sty','Lut','Marz','Kwie','Maj','Czerw','Lip','Sie','Wrze','Paź','Lis','Gru'],
        dayNames: ['Niedziela','Poniedzialek','Wtorek','Środa','Czwartek','Piątek','Sobota'],
        dayNamesShort: ['Nie','Pn','Wt','Śr','Czw','Pt','So'],
        dayNamesMin: ['N','Pn','Wt','Śr','Cz','Pt','So'],
        weekHeader: 'Tydz',
        dateFormat: 'yy-mm-dd',
        firstDay: 1,
        isRTL: false,
        showMonthAfterYear: false,
        yearSuffix: ''};
    $.datepicker.setDefaults({
        beforeShow: function(i) { if ($(i).attr('readonly')) { return false; } },
        regional: ['pl']
    });
});
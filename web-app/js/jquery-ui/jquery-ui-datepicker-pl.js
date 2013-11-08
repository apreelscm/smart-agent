jQuery(function($){
    jQuery.datepicker.regional['pl'] = {
        closeText: 'Zakoncz',
        prevText: 'Poprz.',
        nextText: 'Nast.',
        currentText: 'Dziś',
        monthNames: ['Styczeń','Luty','Marzec','Kwiecień','Maj','Czerwiec', 'Lipiec', 'Sierpień', 'Wrzesień', 'Październik', 'Listopad', 'Grudzień'],
        monthNamesShort: ['Sty', 'Lut', 'Marz', 'Kwie', 'Maj', 'Czerw', 'Lip', 'Sie', 'Wrze', 'Paź', 'Lis', 'Gru'],
        dayNames: ['Poniedziałek', 'Wtorek', 'Środa', 'Czwartek', 'Piątek', 'Sobota', 'Niedziela'],
        dayNamesShort: ['Pon', 'Wt', 'Śr', 'Czw', 'Pt', 'Sob', 'Nd'],
        dayNamesMin: ['Pn', 'Wt', 'Śr', 'Cz', 'Pt', 'Sb', 'Nd'],
        weekHeader: 'Tydz',
        dateFormat: 'dd/mm/yy',
        firstDay: 0,
        isRTL: false,
        showMonthAfterYear: false,
        yearSuffix: ''};
    jQuery.datepicker.setDefaults($.datepicker.regional['pl']);
});

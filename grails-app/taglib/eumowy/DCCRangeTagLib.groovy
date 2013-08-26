package eumowy

class DCCRangeTagLib {

    static namespace = "cbd"

    def cbdService;


    Closure dccRange = { attrs ->

//        TODO - odhashowac gdy juz bedzie dzialac
//        def points = cbdService.getZakresUruchomieniaPunktyGrid(attrs.nip)

        log.info("NIP z atrybutow: " + attrs.nip)


        def points = [
                [id: 1, nazwa: 'Sklep spozywczy', ulica: 'Zielona', miejscowosc: 'Lubartów', nr_budynku: 12, kod_pocztowy: '02-123', liczba_pos: '23'],
                [id: 2, nazwa: 'Kwiaciarnia Róża', ulica: 'Zielona', miejscowosc: 'Lubartów', nr_budynku: 12, kod_pocztowy: '02-123', liczba_pos: '6'],
                [id: 3, nazwa: 'Kino Femina', ulica: 'Zielona', miejscowosc: 'Lubartów', nr_budynku: 12, kod_pocztowy: '02-123', liczba_pos: '2']
        ]

        out << '<table class="t"><thead><tr>'
        out << '<td>' + message(code: "panel.dcc.table.full.name") + '</td>'
        out << '<td>' + message(code: "panel.dcc.table.street") + '</td>'
        out << '<td>' + message(code: "panel.dcc.table.city") + '</td>'
        out << '<td>' + message(code: "panel.dcc.table.house.number") + '</td>'
        out << '<td>' + message(code: "panel.dcc.table.code") + '</td>'
        out << '<td>' + message(code: "panel.dcc.table.poz.count") + '</td>'
        out << '<td>&nbsp;</td>'
        out << '</tr></thead><tbody>'
        points.each{
            point ->
                out << '<tr>'
                out << '<td>' + point.nazwa + '</td>'
                out << '<td>' + point.ulica + '</td>'
                out << '<td>' + point.miejscowosc + '</td>'
                out << '<td>' + point.nr_budynku + '</td>'
                out << '<td>' + point.kod_pocztowy + '</td>'
                out << '<td>' + point.liczba_pos + '</td>'
                out << '<td>' + checkBox(name: 'dccRangePOS', value: point.id, checked: attrs.accepted.contains(point.id)) + '</td>'
                out << '</tr>'
        }
        out << '</tbody></table>'
    }
}
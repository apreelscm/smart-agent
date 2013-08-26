package eumowy

class PointAcceptCardTagLib {

    static namespace = "cbd"

    def cbdService;


    Closure dccPointsAcceptedCards = { attrs ->

//        TODO - odhashowac gdy juz bedzie dzialac
//        def points = cbdService.getZakresUruchomieniaPunktyGrid(attrs.nip)

        log.info("NIP z atrybutow: " + attrs.nip)

        def points = [
                [id: 1, nazwa: 'Sklep spozywczy', ulica: 'Zielona', miejscowosc: 'Lubartów', nr_budynku: '5A', kod_pocztowy: '02-123'],
                [id: 2, nazwa: 'Kwiaciarnia Róża', ulica: 'Zielona', miejscowosc: 'Lubartów', nr_budynku: '12', kod_pocztowy: '02-123'],
                [id: 3, nazwa: 'Kino Femina', ulica: 'Zielona', miejscowosc: 'Lubartów', nr_budynku: '93', kod_pocztowy: '02-123']
        ]

        out << '<table class="t"><thead><tr>'
        out << '<td>' + message(code: "panel.label.number") + '</td>'
        out << '<td>' + message(code: "panel.label.full.name") + '</td>'
        out << '<td>' + message(code: "panel.label.street") + '</td>'
        out << '<td>' + message(code: "panel.label.city") + '</td>'
        out << '<td>' + message(code: "panel.label.house.number") + '</td>'
        out << '<td class="min-width-20px">' + message(code: "panel.label.code") + '</td>'
        out << '<td class="min-width-10px">' + message(code: "panel.label.payment.title") + '</td>'
        out << '<td class="min-width-10px">' + message(code: "panel.label.sys.kas") + '</td>'
        out << '<td class="min-width-10px">' + message(code: "panel.label.uta") + '</td>'
        out << '<td>' + message(code: "panel.label.choosen") + '</td>'
        out << '</tr></thead><tbody>'
        points.each{
            point ->
                out << '<tr>'
                out << '<td>' + point.id + '</td>'
                out << '<td>' + point.nazwa + '</td>'
                out << '<td>' + point.ulica + '</td>'
                out << '<td>' + point.miejscowosc + '</td>'
                out << '<td>' + point.nr_budynku + '</td>'
                out << '<td>' + point.kod_pocztowy + '</td>'
                out << '<td>' + checkBox(name: 'akceptacjaKartTytulPlatnosci', value: point.id, checked: attrs.tytulPlatnosci.contains(point.id)) + '</td>'
                out << '<td>' + checkBox(name: 'akceptacjaKartSystemKasowy', value: point.id, checked: attrs.systemKasowy.contains(point.id)) + '</td>'
                out << '<td>' + checkBox(name: 'akceptacjaKartUTA', value: point.id, checked: attrs.uta.contains(point.id)) + '</td>'
                out << '<td>' + checkBox(name: 'akceptacjaKartWybrany', value: point.id, checked: attrs.accepted.contains(point.id)) + '</td>'
                out << '</tr>'
        }
        out << '</tbody></table>'
    }
}


//        tytulPlatnosci="[1,2]" systemKasowy="[2,3]" uta="[123]" accepted="[1,3]

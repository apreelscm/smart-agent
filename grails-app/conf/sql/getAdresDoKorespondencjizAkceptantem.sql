SELECT a.adr_ulica as ulica, a.adr_nr_lokalu as nr_lokal,
a.adr_nr_budynku as nr_budynku, a.adr_miejscowosc as miejscowosc,
a.adr_kod_pocztowy as kod_pocztowy, a.adr_miejscowosc as poczta , a.adr_typ_ulicy as typ_ulicy
FROM CBD_ADM.cbt_adresy a
join CBD_ADM.cbt_klienci m on a.adr_kln_id=m.kln_id
WHERE kln_poziom = 'MRC'
AND kln_nip = :nip
AND kln_status NOT IN ('N', 'O') AND kln_qcards_nr = 1
AND a.adr_rodzaj='KRP' and a.adr_czy_aktywny ='T'
AND EXISTS
(SELECT   1 FROM   CBD_ADM.cbt_klienci o, CBD_ADM.cbt_terminale_pos
WHERE       o.kln_kln_id = m.kln_id AND o.kln_id = tps_kln_id AND tps_status NOT IN ('N', 'C'))
SELECT a.adr_ulica as ulica, a.adr_nr_lokal as nr_lokal,
 a.adr_nr_budynku as nr_budynku, a.adr_miejscowosc as miejscowosc,
  a.adr_kod_pocztowy as kod_pocztowy, a.adr_miejscowosc as poczta ,a.adr_typ_ulicy as typ_ulicy
FROM cbt_adresy a
join cbt_klienci k on a.adr_kln_id=k.kln_id
where k.kln_kln_id in
(
select kln_id from cbt_klienci m
WHERE kln_poziom = 'MRC'
AND kln_nip = :nip
AND kln_status NOT IN ('N', 'O') AND kln_qcards_nr = 1
AND EXISTS
(SELECT   1 FROM   cbt_klienci o, cbt_terminale_pos
WHERE       o.kln_kln_id = m.kln_id AND o.kln_id = tps_kln_id AND tps_status NOT IN ('N', 'C'))
)
and k.kln_poziom='OUT'
and a.adr_rodzaj='SGL' and a.adr_czy_aktywny ='T'
and k.KLN_MID='541919048258302';
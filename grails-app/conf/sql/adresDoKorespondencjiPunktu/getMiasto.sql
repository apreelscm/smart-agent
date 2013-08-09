SELECT a.adr_miejscowosc from cbt_adresy a
join cbt_klienci k on a.adr_kln_id=k.kln_id
where k.kln_kln_id in
(
select kln_id from cbt_klienci m
WHERE kln_poziom = 'MRC'
AND kln_nip = '7292351080'
AND kln_status NOT IN ('N', 'O') AND kln_qcards_nr = 1
AND EXISTS
(SELECT   1 FROM   cbt_klienci o, cbt_terminale_pos
WHERE       o.kln_kln_id = m.kln_id AND o.kln_id = tps_kln_id AND tps_status NOT IN ('N', 'C'))
)
and k.kln_poziom='OUT'
and a.adr_rodzaj='KRP' and a.adr_czy_aktywny ='T'
and k.KLN_MID='541919048258302';
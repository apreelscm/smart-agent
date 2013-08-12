SELECT a.adr_ulica as ulica, a.adr_nr_lokal as nr_lokal,
 a.adr_nr_budynku as nr_budynku, a.adr_miejscowosc as miejscowosc,
  a.adr_kod_pocztowy as kod_pocztowy, a.adr_miejscowosc as poczta ,a.adr_typ_ulicy as typ_ulicy,
  a.adr_tel_kom as telKom, a.adr_tel_stacjonarny as telStac, a.adr_nr_faksu as faks
from cbt_adresy a
join cbt_klienci m on a.adr_kln_id=m.kln_id
WHERE kln_poziom = 'MRC'
AND kln_nip = :nip
AND kln_status NOT IN ('N', 'O') AND kln_qcards_nr = 1
AND EXISTS
(SELECT   1 FROM   cbt_klienci o, cbt_terminale_pos
WHERE       o.kln_kln_id = m.kln_id AND o.kln_id = tps_kln_id AND tps_status NOT IN ('N', 'C'))
and m.kln_poziom='MRC'
and a.adr_rodzaj='SGL' and a.adr_czy_aktywny ='T';

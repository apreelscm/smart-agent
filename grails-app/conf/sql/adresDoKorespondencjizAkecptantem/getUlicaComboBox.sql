SELECT VALUE FROM (SELECT RV_LOW_VALUE VALUE,RV_MEANING   MEANING FROM CG_REF_CODES WHERE RV_DOMAIN = 'LW_TYP_ULICY') i dodatkowo wartość z bazy SELECT adr_typ_ulicy from cbt_adresy a
join cbt_klienci m on a.adr_kln_id=m.kln_id
WHERE kln_poziom = 'MRC'
AND kln_nip = '7292351080'
AND kln_status NOT IN ('N', 'O') AND kln_qcards_nr = 1
AND a.adr_rodzaj='KRP' and a.adr_czy_aktywny ='T'
AND EXISTS
(SELECT   1 FROM   cbt_klienci o, cbt_terminale_pos
WHERE       o.kln_kln_id = m.kln_id AND o.kln_id = tps_kln_id AND tps_status NOT IN ('N', 'C'))
/*jako wartość domyślna*/
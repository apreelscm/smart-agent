SELECT kbk_numer from CBD_ADM.cbt_klienci k
join CBD_ADM.cbt_konta_bankowe a on k.kln_id=a.kbk_kln_id
where k.kln_kln_id in
(
select kln_id from CBD_ADM.cbt_klienci m
WHERE kln_poziom = 'MRC' 
AND kln_nip = :nip
AND kln_status NOT IN ('N', 'O') AND kln_qcards_nr = 1 
AND EXISTS 
(SELECT   1 FROM   CBD_ADM.cbt_klienci o, CBD_ADM.cbt_terminale_pos
WHERE       o.kln_kln_id = m.kln_id AND o.kln_id = tps_kln_id AND tps_status NOT IN ('N', 'C')
and o.kln_mid not like '371%'
and tps_numer_logiczny not like '371%'
)
)
and k.kln_poziom='OUT' 
and a.kbk_czy_aktywne='T'
and a.kbk_rodzaj='PRZ'
and k.KLN_MID='541919048258302'
and k.kln_mid not like '371%'
SELECT kbk_numer from cbt_klienci k
join cbt_konta_bankowe a on k.kln_id=a.kbk_kln_id
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
and a.kbk_czy_aktywne='T'
and a.kbk_rodzaj='PRZ'
and k.KLN_MID='541919048258302';
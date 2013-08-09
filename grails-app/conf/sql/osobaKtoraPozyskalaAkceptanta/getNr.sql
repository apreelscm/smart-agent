select prz_numer_sprzedazowy from cbt_klienci m
join cbt_przedstawicieleh a on a.prz_id=m.kln_prz_id_pozyskany_przez
WHERE m.kln_poziom = 'MRC'
AND m.kln_nip = '7292351080'
AND m.kln_status NOT IN ('N', 'O') AND m.kln_qcards_nr = 1
AND EXISTS
(SELECT   1 FROM   cbt_klienci o, cbt_terminale_pos
WHERE       o.kln_kln_id = m.kln_id AND o.kln_id = tps_kln_id AND tps_status NOT IN ('N', 'C'));
select osb_imie as "imie",osb_nazwisko as "nazwisko", OSB_TYTUL as "tytul"  from CBD_ADM.cbt_klienci m
                                                                                     join CBD_ADM.cbt_osoby a on a.osb_kln_id=m.kln_id
WHERE m.kln_poziom = 'MRC'
  AND m.kln_nip = :nip
  AND m.kln_status NOT IN ('N', 'O') AND m.kln_qcards_nr = 1
  AND EXISTS
    (SELECT   1 FROM   CBD_ADM.cbt_klienci o, CBD_ADM.cbt_terminale_pos
     WHERE       o.kln_kln_id = m.kln_id AND o.kln_id = tps_kln_id AND tps_status NOT IN ('N', 'C')
       and o.kln_mid not like '371%'
       and tps_numer_logiczny not like '371%'
    )
  and m.kln_poziom='MRC'
  and a.osb_rodzaj='RP1'
  and m.kln_mid not like '371%'

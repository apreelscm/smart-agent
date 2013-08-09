SELECT   k.kln_nazwa,
         a.adr_ulica,
         a.adr_miejscowosc,
         a.adr_nr_budynku,
         adr_kod_pocztowy
  FROM      cbt_adresy a
         JOIN
            cbt_klienci k
         ON a.adr_kln_id = k.kln_id
 WHERE   k.kln_kln_id IN
               (SELECT   kln_id
                  FROM   cbt_klienci m
                 WHERE       kln_poziom = 'MRC'
                         AND kln_nip = '7292351080'
                         AND kln_status NOT IN ('N', 'O')
                         AND kln_qcards_nr = 1
                         AND EXISTS
                               (SELECT   1
                                  FROM   cbt_klienci o, cbt_terminale_pos
                                 WHERE       o.kln_kln_id = m.kln_id
                                         AND o.kln_id = tps_kln_id
                                         AND tps_status NOT IN ('N', 'C')))
         AND k.kln_poziom = 'OUT'
         AND a.adr_rodzaj = 'SGL'
         AND A.adr_czy_aktywny = 'T'
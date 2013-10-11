SELECT
         k.kln_id as "id",
         k.kln_nazwa as "nazwa",
         a.adr_ulica as "ulica",
         a.adr_miejscowosc as "miejscowosc",
         a.adr_nr_budynku as "nr_budynku",
         adr_kod_pocztowy as "kod_pocztowy",
         (SELECT   COUNT ( * )
            FROM   CBD_ADM.cbt_terminale_pos
           WHERE   tps_status NOT IN ('N', 'C') AND tps_kln_id = k.kln_id)
            as "liczba_pos"
  FROM      CBD_ADM.cbt_adresy a
         JOIN
            CBD_ADM.cbt_klienci k
         ON a.adr_kln_id = k.kln_id
 WHERE   k.kln_kln_id IN
               (SELECT   kln_id
                  FROM   CBD_ADM.cbt_klienci m
                 WHERE       kln_poziom = 'MRC'
                         AND kln_nip =:nip
                         AND kln_status NOT IN ('N', 'O')
                         AND kln_qcards_nr = 1
                         AND EXISTS
                               (SELECT   1
                                  FROM   CBD_ADM.cbt_klienci o, CBD_ADM.cbt_terminale_pos
                                 WHERE       o.kln_kln_id = m.kln_id
                                         AND o.kln_id = tps_kln_id
                                         AND tps_status NOT IN ('N', 'C')))
         AND k.kln_poziom = 'OUT'
         AND a.adr_rodzaj = 'SGL'
         AND a.adr_czy_aktywny = 'T'
         AND k.kln_id = :cbdid
SELECT   tps_numer_logiczny as "numer_logiczny"
  FROM CBD_ADM.cbt_klienci m
            JOIN
               CBD_ADM.cbt_klienci k
            ON k.kln_kln_id = m.kln_id
         JOIN
            CBD_ADM.cbt_terminale_pos tp
         ON tp.tps_kln_id = k.kln_id
 WHERE   m.kln_poziom = 'MRC'
         AND m.kln_nip = :nip
         AND m.kln_status NOT IN ('N', 'O')
         AND m.kln_qcards_nr = 1
         AND tp.tps_status NOT IN ('N', 'C')
         AND k.kln_poziom = 'OUT'
         AND NVL(tp.tps_oplata_kwota, 0) > 0
         AND tps_oplata_data_od IS NOT NULL

SELECT   COUNT (1) ile, tps_oplata_pos top
    FROM   CBD_ADM.cbt_terminale_pos, CBD_ADM.cbt_klienci o, CBD_ADM.cbt_klienci m
   WHERE       tps_kln_id = o.kln_id
           AND o.kln_kln_id = m.kln_id
           AND m.kln_nip = :nip
           AND tps_status NOT IN ('N', 'C')
           AND m.kln_qcards_nr = 1
		   AND o.kln_mid not like '371%'
		   AND tps_numer_logiczny not like '371%'
		   AND m.kln_mid not like '371%'
GROUP BY   tps_oplata_pos
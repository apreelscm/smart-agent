SELECT *
FROM cbt_klienci o, cbt_klienci m, cbt_terminale_pos
WHERE rownum <= 1 AND EXISTS
        (SELECT 1
         FROM cbt_terminale_pos t
         WHERE tps_kln_id = o.kln_id AND tps_funkcje LIKE '%CB%' AND tps_status NOT IN ('N', 'C')
		 and tps_numer_logiczny not like '371%'
        )
 AND o.kln_mid = :mid AND m.kln_id = o.kln_kln_id and m.kln_mid not like '371%'
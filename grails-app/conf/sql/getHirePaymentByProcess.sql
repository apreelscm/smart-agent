SELECT
count (*) as "ile"
FROM cbt_klienci o, cbt_terminale_pos
WHERE o.kln_id = tps_kln_id
AND tps_status NOT IN ('N', 'C')
AND kln_nip = :nip
AND o.kln_qcards_nr =1
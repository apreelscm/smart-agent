SELECT NVL(MAX (umw_data_podpisania),sysdate) as "dataAneksowanejUmowyPos"
FROM cbt_umowy
WHERE umw_typ = 'ATP'
  AND umw_kln_id IN
    (SELECT kln_id
    FROM cbt_klienci m
    WHERE kln_poziom = 'MRC'
      AND kln_nip = :nip
      AND kln_status = 'Q'
      AND kln_qcards_nr = 1
      AND EXISTS
        (SELECT 1
         FROM cbt_klienci o, cbt_terminale_pos
         WHERE o.kln_kln_id = m.kln_id
            AND o.kln_id = tps_kln_id
            AND tps_status NOT IN ('N', 'C')))
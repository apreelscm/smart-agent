SELECT k.kln_id AS "id",
  (k.kln_nazwa || ' ' || a.adr_ulica || ' ' || a.adr_nr_budynku || ', ' || a.adr_miejscowosc || ' ' || adr_kod_pocztowy) AS "value"
FROM CBD_ADM.cbt_adresy a
JOIN CBD_ADM.cbt_klienci k
ON a.adr_kln_id       = k.kln_id
WHERE k.kln_nip       = :nip
AND k.kln_status NOT IN ('N', 'O')
AND k.kln_qcards_nr   = 1
AND EXISTS
  (SELECT 1
  FROM CBD_ADM.cbt_terminale_pos
  WHERE k.kln_id      = tps_kln_id
  AND tps_status NOT IN ('N', 'C')
  )
AND k.kln_poziom      = 'OUT'
AND a.adr_rodzaj      = 'SGL'
AND A.adr_czy_aktywny = 'T'
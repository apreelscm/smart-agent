SELECT k.kln_id     AS "id",
  k.kln_nip         AS "nip",
  k.kln_nazwa       AS "nazwa",
  a.adr_ulica       AS "ulica",
  a.adr_miejscowosc AS "miejscowosc",
  a.adr_nr_budynku  AS "nr_budynku",
  adr_kod_pocztowy  AS "kod_pocztowy",
  (SELECT COUNT ( * )
  FROM CBD_ADM.cbt_terminale_pos
  WHERE tps_status NOT IN ('N', 'C')
  AND tps_kln_id        = k.kln_id
  ) AS "liczba_pos"
FROM CBD_ADM.cbt_adresy a
JOIN CBD_ADM.cbt_klienci k
ON a.adr_kln_id     =k.kln_id
WHERE k.kln_poziom  ='OUT'
AND a.adr_rodzaj    ='SGL'
AND kln_nip         =:nip
AND kln_status NOT IN ('N', 'O')
AND kln_qcards_nr   = 1
AND EXISTS
  (SELECT 1
  FROM CBD_ADM.cbt_terminale_pos
  WHERE tps_kln_id    = k.kln_id
  AND tps_status NOT IN ('N','C')
  )
AND a.adr_czy_aktywny = 'T'
ORDER BY k.kln_id
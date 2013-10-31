SELECT
  o.kln_id "point_id",
  o.kln_nazwa nazwa_punktu,
  max(CBD_ADM.getAdres (o.kln_id, 'SGL')) adres_posadowienia,
  md.smt_nazwa as typ,
  COUNT (1) ile,
  tps_oplata_pos "oplata_za_pos"
FROM CBD_ADM.cbt_terminale_pos t, CBD_ADM.cbt_klienci o, CBD_ADM.cbt_klienci m, CBD_ADM.cbt_sl_modele_terminali md
WHERE t.tps_kln_id = o.kln_id
  AND o.kln_kln_id = m.kln_id
  AND md.smt_id = tps_smt_id
  AND m.kln_nip = :nip
  AND tps_status NOT IN ('N', 'C')
GROUP BY o.kln_nazwa, tps_oplata_pos, md.smt_nazwa, o.kln_id
SELECT  
  ss.tps_id as "pos_id",
  o.kln_id as "point_id",
  o.kln_nazwa nazwa_punktu,
  CBD_ADM.getAdres (o.kln_id, 'SGL') adres_posadowienia,
  tps_numer_logiczny tid,
  tps_oplata_pos oplata_za_pos,
  (select smt_nazwa from cbt_sl_modele_terminali where smt_id = tps_smt_id) rodzaj_terminala,
  1 as "terminal_count"
FROM   cbt_terminale_pos ss, cbt_klienci o, cbt_klienci m
  WHERE       tps_kln_id = o.kln_id
    AND o.kln_kln_id = m.kln_id
    AND m.kln_nip = :nip
    AND tps_status NOT IN ('N', 'C')
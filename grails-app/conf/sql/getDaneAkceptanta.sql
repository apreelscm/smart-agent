SELECT KLN_NAZWA as nazwa, KLN_REGON as regon FROM cbt_klienci m
WHERE kln_poziom = 'MRC' AND kln_nip = :nip AND kln_status NOT IN ('N', 'O') AND kln_qcards_nr = 1
  AND EXISTS (SELECT   1 FROM   cbt_klienci o, cbt_terminale_pos
    WHERE o.kln_kln_id = m.kln_id AND o.kln_id = tps_kln_id AND tps_status NOT IN ('N', 'C'));
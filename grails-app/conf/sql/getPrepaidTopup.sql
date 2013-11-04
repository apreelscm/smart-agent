SELECT   umw_kln_id, upp_wh_topup, upp_wh_evoucher
  FROM   cbt_umowy u1, cbt_umowy_pp
 WHERE       umw_typ = 'PRP'
         AND umw_stan = 'Z'
         AND umw_data_podpisania IS NOT NULL
         AND umw_id = upp_umw_id(+)
         AND umw_data_podpisania =
               (SELECT   max(u2.umw_data_podpisania)
                  FROM   cbt_umowy u2
                 WHERE       u2.umw_typ = 'PRP'
                         AND u2.umw_stan = 'Z'
                         AND u2.umw_data_podpisania IS NOT NULL
                         AND u2.umw_kln_id = u1.umw_kln_id)
         AND NVL (upp_wh_topup, 'T') = 'T'
         AND umw_kln_id IN
                  (SELECT   kln_id
                     FROM   cbt_klienci
                    WHERE       kln_nip = :NIP
                            AND kln_poziom = 'MRC'
                            AND kln_status NOT IN ('N', 'O')
                            AND kln_qcards_nr = 1)
         AND NOT EXISTS
               (SELECT   1
                  FROM   cbt_wypowiedzenia, cbt_umowy u2
                 WHERE       u2.umw_id = wyp_umw_id
                         AND u2.umw_kln_id = u1.umw_kln_id
                         AND wyp_data_wygasniecia < SYSDATE
                         AND wyp_status IN ('R', 'H')
                         AND wyp_poziom ='MRC')

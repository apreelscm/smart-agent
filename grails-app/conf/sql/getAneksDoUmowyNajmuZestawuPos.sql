SELECT MAX(umw_data_podpisania) as "dataAneksowanejUmowyPos"
FROM CBD_ADM.cbt_umowy
WHERE umw_typ = 'ATP'
      AND umw_kln_id IN
          (SELECT kln_id
           FROM CBD_ADM.cbt_klienci
           WHERE kln_poziom = 'MRC'
                 AND kln_nip = :nip
                 AND kln_status = 'Q')
SELECT BNK_ID as id, BNK_NAZWA_SKROCONA as name
FROM CBD_ADM.cbt_banki
WHERE bnk_nr_oddzialu = :num
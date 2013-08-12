select bnk_nazwa from cbt_banki
where substr(:nrRachunku,3,8)=bnk_nr_oddzialu
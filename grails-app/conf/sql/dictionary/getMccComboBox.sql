select slm_kod as "code", (slm_kod || ' - ' || slm_nazwa) as "description" from cbd_adm.cbt_sl_mcc
where slm_kod is not null order by upper(slm_nazwa) asc
select
       slm_kod as "code",
       (slm_kod || ' - ' || slm_nazwa) as "description",
       slm_poziom_ryzyka as "risk"
from cbd_adm.cbt_sl_mcc
where slm_kod is not null order by upper(slm_nazwa) asc
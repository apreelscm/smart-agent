select distinct(slownik) as "value" from eumowy.kalkulatortypurzadzen k
join cbd_adm.cbt_sl_kalk_slownik slo on slo.ksl_kod=k.typ
where ksl_kod like 'AMORT%'
and k.medium=:medium
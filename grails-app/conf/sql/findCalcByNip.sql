select 
poleapreel,
case
when  KSP_NAZWA='S_MCC' then trim(eumowy.split(nvl(KAP_WARTOSC,'BRAK'),1,'-'))
else
nvl(KAP_WARTOSC,'BRAK')
end as wartoscApreel
from EUMOWY.mapowaniekalkulatora mk
join CBD_ADM.CBT_KALK_SZABLON_POLE ksp on mk.polekalkulator=ksp.ksp_nazwa
join CBD_ADM.CBT_KALK_SZABLON ks on ks.KAS_ID=ksp.KSP_KAS_ID
join CBD_ADM.CBT_KALK_POLE kp on kp.KAP_KSP_ID =ksp.ksp_id
join CBD_ADM.cbt_kalk k on k.kak_id=kp.KAP_KAK_ID
where KAK_ID in
(
  Select max(kak_id) as kak_id from CBD_ADM.cbt_kalk where kak_nip=:nip
  and kak_status='Zaakceptowany'
)
and KSP_NAZWA<>'S_PAKIET_SERWIS_1'
union all
select pole,wartosc from table(CAST(eumowy.GetKalkulatorStawkaPlaska(:nip) AS EUMOWY.KALKULATOR))
union all 
select pole,wartosc from table(CAST(eumowy.GetKalkulatorSerwis(:nip) AS EUMOWY.KALKULATOR))
union all
select pole,wartosc from table(CAST(eumowy.GetKalkulatorZero(:nip) AS EUMOWY.KALKULATOR))
union all
select pole,wartosc from table(CAST(eumowy.GetKalkulatorPrepaid(:nip) AS EUMOWY.KALKULATOR))
union all
select pole,wartosc from table(CAST(eumowy.GetKalkulatorPromocyjne(:nip) AS EUMOWY.KALKULATOR))
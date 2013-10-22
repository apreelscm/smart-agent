-- wykonywany z poziomu ownera CBD_ADM
grant select, update on adm_uzytkownicy to EUMOWY_APP;
grant select, update on adm_uzytkownicy_web to EUMOWY_APP;
grant select on adm_role to EUMOWY_APP;
grant select on cbt_klienci to EUMOWY_APP;
grant select on cbt_klienci to EUMOWY;
grant select on cbt_klienci_pp to EUMOWY_APP;
grant select on cbt_umowy to EUMOWY_APP;
grant select on cbt_pracownicy to EUMOWY_APP;
grant select on cbt_przedstawicieleh to EUMOWY_APP;
grant select on cbt_struktury to EUMOWY_APP;
grant select on cbt_adresy to EUMOWY_APP;
grant select on cbt_terminale_pos to EUMOWY_APP;
grant select on cbt_terminale_pos_pozycje to EUMOWY_APP;
grant select on cbt_terminale_zestawy to EUMOWY_APP;
grant select on cbt_banki to EUMOWY_APP;
grant select on cbt_konta_bankowe to EUMOWY_APP;
grant select on cbt_osoby to EUMOWY_APP;
grant select on cbt_sl_kraje to EUMOWY_APP;
grant select on cbt_sl_wojewodztwa to EUMOWY_APP;
grant select on cbt_sl_powiaty to EUMOWY_APP;
grant select on cbt_sl_gminy to EUMOWY_APP;
grant select on cbt_sl_kalk_slownik to EUMOWY_APP;
grant select on cbt_sl_kalk_slownik to EUMOWY;
grant select on cg_ref_codes to EUMOWY_APP;

grant select on CBT_KALK_SZABLON to EUMOWY_APP;
grant select on CBT_KALK_SZABLON to EUMOWY;
grant select on CBT_KALK_SZABLON_POLE to EUMOWY_APP;
grant select on CBT_KALK_SZABLON_POLE to EUMOWY;
grant select on CBT_KALK_POLE to EUMOWY_APP;
grant select on CBT_KALK_POLE to EUMOWY;
grant select on cbt_kalk to EUMOWY_APP;
grant select on cbt_kalk to EUMOWY;
grant select on CBT_SL_MCC to EUMOWY_APP;
grant select on CBT_SL_MCC to EUMOWY;
grant select on CBT_SL_MIASTA to EUMOWY_APP

grant execute on ADM_WEB to EUMOWY_APP;
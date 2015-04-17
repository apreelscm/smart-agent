select KDD_RODZAJ_UMOWY from cbd_adm.cbt_klienci_dane_dodatkowe
where KDD_KLN_ID = :clientId and KDD_RODZAJ_UMOWY in ('P', 'E')
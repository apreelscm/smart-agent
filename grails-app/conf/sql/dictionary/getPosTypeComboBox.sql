select distinct(ktu_slownik) as "value" from eumowy.kalkulator_typ_urzadzen k
where k.ktu_medium=:medium
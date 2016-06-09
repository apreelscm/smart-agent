select min(tps_oplata_pos) from cbt_terminale_pos 
where tps_status not in ('N','C')
and tps_numer_logiczny not like '371%' 
and tps_kln_id in
	(select kln_id from cbt_klienci 
		where kln_status in ('Q','K') 
		and kln_qcards_nr = 1 
		and kln_nip =:nip
		and kln_mid not like '371%'
	)
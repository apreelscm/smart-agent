-- do wykonania tylko na produkcji z uwagi na to ze skrypty do wersji 1.0.0 zostaly wdrozone przed ostatecznymi poprawkami skryptow

-- usuniecie nieuzywanych parametrow
delete from EUMOWY.app_parameters where name != 'SUBSCRIPTIONS_PATH_BLACKPREFIX'

-- usuniecie nieuzywanych paneli
delete * from EUMOWY.panel where id in (1,2,8,18,20,23,25,28,40,41,43,49)

-- usuniecie zbednych kolumn przeniesionych do subscription_definition
alter table EUMOWY.SIGNATURE drop (ph_subscription_page_number, ph_subscriptionx, ph_subscriptiony, subscription_page_number, subscriptionx, subscriptiony, management_subscription1, management_subscription2);

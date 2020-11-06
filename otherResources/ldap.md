- pod jakim adresem jest dostępna testowa usługa ? czy jest to http://192.168.3.192:8080/microLDAP wspominana w dokumentacji?
- w zalaczonej dokumentacji sa przyklady klas obiektow dla metod autentykacji, czy jest dostepna jakas
biblioteka kliencka dla serwisu microLDAP ?
- jaka jest rola atrybutów role oraz extensionRole z User w odpowiedzi wywołania authAdUser ? Czy metoda zwraca informacje o rolach np. EUM_ADMINISTRATOR, EUM_ZRD (w modelu nie ma listy)? czy należy pobrać je niezależnie z adm_uzytkownicy_web?
- uczy prawnienia do dostępu funkcjonalności aplikacji powinny bazować na tabeli adm_uzytkownicy_web i powiązanych z nią tabel z rolami/uprawnieniami – tak jak do tej pory to się odbywa dla aplikacji e-kalkulator i e-umowa. 

- czy w procesie logowania ma się zmienić login na domenowy, czy

Login użytkownika domeny można pobrać z adm_uzytkownicy_web z pola auw_windows_login. Domena – domyślna wartość eservice.
- co to dla mnie oznacza ?

- prośba o zlecenie wystawienia testowej usługi w tunelu do apreel, prosba o informacje pod jakim adresem TODO


ad 3.
w takim razie czekam na dalsze info, problem jaki tu obecnie widze to taki ze eumowy wykorzystuja 
dostarczona od eservice biblioteke (cbd-core-security v. 2.6) i serwis do logowania oraz pobran rol, niemniej pobranie samych rol odbywa sie 
po loginie i jesli login domenowy jest inny niz obecnie uzywany AUW_LOGIN vs AUW_WINDOWS_LOGIN w adm_uzytkownicy_web.
Metoda do pobrania rol uzywa call Adm_WEB.FIND_ROLES (?, :username , :appcode) ale nie po loginie domenowym.
Bedziemy wiec albo musieli dostac nowa funckje wyszukujaca role po :ldapLogin, albo zmodyfikowac dostarczona
UserService ze wspomnianej biblioteki w celu pobrania zwyklego loginu na podstawie loginu domenowego.

- do tej pory przy logowniu rownoczesnie byl przy tworzeniu kontekstu uzytkownika pobierany nr sprzedazowy za pomoca wywolania GET_NUMER_SPRZEDAZOWY (auwId number)
- do tej pory kontekst usera opieral sie na id usera auwId ktory jest wykorzystywany przy wywolaniu metody acceptUmowa (rowniez w starym serwisie bisnode searchMerchantData)


Dokumentacja
Aktualna dokumentacje mozna wygenerowac pod adresem
http://uat-eumowy.apreel.net:8080/microLDAP/jsondoc-ui.html#
wprowadzajac url uslugi 
http://uat-eumowy.apreel.net:8080/microLDAP/jsondoc

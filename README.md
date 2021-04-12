# 1. Frameworki / narzędzia

### Development

* [Grails v2.4.2](https://grails.github.io/grails2-doc/2.4.2/guide/single.html)
* [Java 1.7](https://www.oracle.com/pl/java/technologies/javase/javase7-archive-downloads.html)
* [Oracle Database](https://www.oracle.com/pl/database/)

# 2. Uruchamianie projektu

1. Zainstalować Jave w wersji 1.7
2. Aktywować plugin Grails (Intellij IDEA)
3. Zainstalować Grails (najlepiej przy użyciu [SDKMAN!](https://sdkman.io))
4. Skonfigurować Grails SDK dla projektu
   ![Grails SDK](./readme/grails_sdk.png)
5. Skopiować pliki z katalogu `otherResources/tomcat/lib` do `$USER_HOME/.grails`
6. Skopiować plik `otherResources/conf.properties` do `/opt/settings`
7. Przekopiowac katalog `otherResources/pdf_templates` do lokalizacji `/opt/eumowy`
8. Uruchomić projekt z argumentami `-Dgrails.env=development -Dgrails.reload.enabled=true run-app`
   ![Run Config](./readme/run_config.png)

# 3. Linki

* [Jak nazywać commity](https://seesparkbox.com/foundry/semantic_commit_messages)

# 4. Inne informacje

## Namiary na bazę danych

URL: `jdbc:oracle:thin:@10.9.192.59:1521:cbd_dev`

Username: `EUMOWY_APP`

Hasło: `Ahfopcvy$aU3`

## Konta użytkowników

| Login        | Hasło           | Rola     |
| -----------  | --------------- | -------- |
| askonieczny  | ssddRTY235689%% | PH       |
| mmelissa     | QQww123@456@789 | Admin    |

## Przydatne zapytania SQL

* Wszystkie możliwe sygnatury dla aktywności
  ```
  SELECT act.CODE, act_sig.NUMBER_OF_LIST, sig.NAME, sig.DESCRIPTION, act_sig.REQUIRED_ACTIVITIES, act_sig.MANDATORY
  FROM ACTIVITY_SIGNATURES act_sig
     JOIN ACTIVITY act ON act_sig.ACTIVITY_ID = act.ID
     JOIN SIGNATURE sig on act_sig.SIGNATURE_ID = sig.ID
  WHERE sig.ACTIVE = 1
  ORDER BY act_sig.NUMBER_OF_LIST;
  ```

* Usunięcie powiązania sygnatury z aktywnością
  ```
  DELETE FROM EUMOWY.ACTIVITY_SIGNATURES
     WHERE ACTIVITY_ID IN (SELECT ID FROM ACTIVITY WHERE CODE IN ('dodanieDcc', 'logoKalkulatorSesja')) AND
     SIGNATURE_ID = (SELECT ID FROM SIGNATURE WHERE NAME = 'AP/UW/AWU/1.000/21-01-01') AND
     REQUIRED_ACTIVITIES = 'nowaUmowa';
  ```

* Dodanie nowego panelu do sygnatury
  ```
  INSERT INTO EUMOWY.PANEL (ID, VERSION, NAME, ORDER_NO) VALUES(EUMOWY.PANEL_SEQ.nextval, 0, 'nazwaPanelu', 485);

  INSERT INTO EUMOWY.SIGNATURE_PANEL(ID, VERSION, PANEL_ID, SIGNATURE_ID)
  VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL WHERE NAME = 'nazwaPanelu'), (SELECT ID FROM EUMOWY.SIGNATURE WHERE NAME = 'virtualNowaUmowa'));
  ```

* Podmiana sygnatur
  ```
  v_syg_source := 'AP/UPZ/ZSNT1/1.004/18-07-20';
  v_syg_dest := 'AP/UPZ/ZSNT1/1.005/20-02-28';

  v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
  update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT11.00520-02-28.pdf' where name = v_syg_dest;
  update eumowy.signature set active = 0 where name = v_syg_source;
  ```

* Zmiana lokalizacji podpisów na wydruku
  
  Lokalizację podpisów określa się metodą "chybił trafił" na podstawie wielokretnego wykonania testów z klasy `PdfIntegrTests.groovy` używając różnych koordynatów
  ```
  DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT1/1.005/20-02-28');

  INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
  VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT1/1.005/20-02-28'), 'PH', null, 1, 390, 382, 59, 28);
  ```

## LDAP
Aktualna dokumentacje mozna wygenerowac pod adresem
http://uat-eumowy.apreel.net:8080/microLDAP/jsondoc-ui.html#
wprowadzajac url uslugi
http://uat-eumowy.apreel.net:8080/microLDAP/jsondoc

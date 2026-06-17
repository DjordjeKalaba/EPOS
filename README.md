**EPOS Billing Platform**

Mikroservisna aplikacija za upravljanje klijentima i fakturisanje zasnovana na multitenancy arhitekturi. Sistem omogućava registraciju novih EPOS klijenata, administraciju njihovih korisnika, kreiranje i generisanje faktura u PDF formatu, kao i automatsku naplatu korišćenja EPOS servisa.

Implementirano je automatsko aktiviranje novih klijenata, kreiranje zasebnih baza podataka i konfiguracija sistema za svakog zakupca (tenant). Svaki registrovani klijent može upravljati svojim šifrarnikom klijenata kroz CRUD operacije, kreirati fakture, pregledati istoriju faktura i preuzimati ih kao PDF dokumente.

Arhitektura sistema zasnovana je na mikroservisima koji međusobno komuniciraju putem Message Queue mehanizma, dok se komunikacija između web aplikacije i mikroservisa odvija preko REST API-ja. Autentifikacija i autorizacija korisnika realizovane su korišćenjem JWT tokena.

Sistem takođe sadrži modul za obračun korišćenja servisa koji na kraju obračunskog perioda automatski generiše fakture za EPOS klijente na osnovu broja izvršenih operacija (upravljanje klijentima i kreiranje faktura).

**Ključne funkcionalnosti**

Registracija i aktivacija novih EPOS klijenata
Multitenancy podrška sa izolovanim podacima za svakog klijenta
CRUD upravljanje klijentima
Kreiranje, pregled i čuvanje faktura
Generisanje faktura u PDF formatu
JWT autentifikacija i autorizacija
Komunikacija mikroservisa putem Message Queue sistema
REST API za komunikaciju sa web aplikacijom
Evidencija korišćenja servisa
Automatsko generisanje mjesečnih faktura za EPOS klijente
Obračun troškova na osnovu korišćenja sistema

**Tehnologije**

Spring Boot
RESTful API
Message Queue
JWT Authentication
Relaciona baza podataka
PDF Generator
Mikroservisna arhitektura

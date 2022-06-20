
># -= PostEr =-
#### *Demo változat - Külterületek*
## Csomagküldő-nyilvántartó webszolgáltatás
***
***
###
A PostEr egy modulárisan kiegészíthető, és egyszerűen továbbfejleszthető csomagküldés nyilvántartást szimuláló
többrétegű, alapvetően Java, és MariaDB technológiákat használó alkalmazás.

#### Támogatott alkalmazás szerver:
- Apache Tomcat

#### Támogatott adatbázis szerver:
- MYSQL
- MariaDB (default)

##
### Perzisztencia réteg (Persistence)
***
Kettő fő entitásból áll, a címzettből, és a csomagból, melyek között kétirányú, rendre 1-n kapcsolat áll fenn.
Az adatbázis-kezelő réteg megvalósítására Spring Data JPA technológia került alkalmazásra.

#### Entitások:
* ##### Címzett - Addressee
   
  `(Id, Név, Irányítószám, Település, Házszám, Csomagok)`

  Nem lehetnek null, illetőleg üres .
  
* ##### Csomag - Parcel
  `(Id, Küldő Id, Küldés ideje, Csomag TÍPUSA, Címzett)`
  * Nem lehetnek null, illetőleg üres.
  * A feladás időpontja _-lokális rendszeridő alapján-_ nem mutathat a jövőbe.
  * A Küldő ID előre definiált minta szerint validált.
  

Az entitások között kétirányú, egy-több kapcsolat áll fent, tehát
egy címzettnek lehet több csomagja, de egy csomag csak egy címzetthez kerülhet.

#### Enum
* Attribútum:
  * int capacity
* Példányok:
  * OVERSIZE(10), HUGE(8), NORMAL(5), MINI(2)
##

### Szolgáltatás -Üzleti logika- réteg (Service)
***
#### Feltétel: Logisztikai szállítási kapacitás: 10 egység.
`Csomag szolgáltatások - ParcelService`
* Lekérdezhetőség (listázás) az összes csomag vonatkozásában, amely az adatbázisban rögzített.
* Új csomag létrehozása (felvétele), amely a csomag adatbázis-táblába mentésre kerül. *(Pl. Postán maradó. Rögzítésre kerül
az adatbázis nyilvántartásába címzett nélkül.)*
* Lekérdheztősége egy adott csomagnak a csomag ID alapján.
* Törlése ID alapján egy adott csomagnak.

`Címzett szolgáltatások - AddresseeService`
* Lekérdezhetőség az összes címzet és csomagja (összes) vonatkozásában.
* Új címzett csomag felvétele (létrehozás), amely az adatbázisba-táblákba mentésre kerül.
* Lekérdezhetősége egy adott címzettnek a címzett ID alapján.
* Törlése ID alapján egy adott címzettnek csomagjaival együtt.
* Új csomag felvétele egy már meglévő címzetthez ID alapján, amely a csomag adatbázis-táblában is mentésre kerül,
a címzetthez rendelve.
* Címzetlen csomag továbbításra megcímzése (címzetthez hozzárendelése), amely a vonatkozó adatbázis-táblákba mentésre kerül.
##

### Web réteg (Presentacion)
***
#### API végpontok: ***[Swagger Ui](http://localhost:8080/v3/api-docs)***

`AddresseeController` végpontja `/api/addresses`

| HTTP metódus |             Végpont             | Leírás                                      |
|:------------:|:-------------------------------:|:--------------------------------------------|
|     GET      |       `"/api/addresses"`        | Összes címzett listázása                    |
|     POST     |                                 | Címzett csomag hozzáadása                   |
|     GET      |     `"/api/addresses/{id}"`     | Lekérdezés címzett ID alapján               |
|    DELETE    |                                 | Törlés címzett ID alapján                   |
|     POST     | `"/api/addresses/{id}/parcels"` | Új csomag hozzáadása címzetthez             |
|     PUT      |                                 | Címzettlen csomag címzetthez hozzárendelése |

###
`ParcelController` végpontja `/api/parcels`:

| HTTP metódus |        Végpont        | Leírás                                      |
|:------------:|:---------------------:|---------------------------------------------|
|     GET      |   `"/api/parcels"`    | Összes csomag lisátzása                    |
|     POST     |                       | Csomag felvétele                   |
|     GET      | `"/api/parcels/{id}"` | Lekérdezés csomag ID alapján               |
|    DELETE    |                       | Törlés csomag ID alapján                   |
###

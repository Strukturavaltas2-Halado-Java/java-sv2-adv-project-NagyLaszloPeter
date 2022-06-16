
># -= PostEr =-
#### *Demo változat*
### Csomagküldő-nyilvántartó

A PostEr egy modulárisan kiegészíthető, és egyszerűen továbbfejleszthető csomagküldés nyilvántartást szimuláló
többrétegű, alapvetően Java, és MariaDB technológiákat használó alkalmazás.

Támogatott alkalmazás szerver:
- Apache Tomcat

Támogatott adatbázis szerver:
- MYSQL
- MariaDB (default)

### Perzisztencia réteg (Persistence):
Kettő fő entitásból áll. Az adatbázis-kezelő réteg megvalósítására Spring Data JPA technológia került alkalmazásra.

### *Entitások:*
* Címzett (Id, Név, Irányítószám, Település, Házszám, Csomagok) - *[Addressee.class]*
* Csomag (Id, Küldő Id, Küldés ideje, Csomag TÍPUSA, Címzett)  -  *[Parcel.class]*

Az entitások között kétirányú, egy-több kapcsolat áll fent, tehát
egy címzettnek lehet több csomagja, de egy csomag csak egy címzetthez kerülhet.


### Szolgáltatás -Üzleti logika- réteg (Service)



### Web réteg (Presentacion): 
`AddresseeController` végpontja: `/api/addresses`

`PackageController` végpontja: `/api/packages`










Hamtelligence Scraper
=====================

Sources
-------

| Source        | Class | URL           | In DB ?  |
| ------------- | ----- | ------------- | -------- |
| UK Repeaters  | scraper.ukrepeater.RepeaterLists | http://www.ukrepeater.net | Yes |
| DMR Registrations | scraper.Dmr | http://www.dmr-marc.net/cgi-bin/trbo-database/datadump.cgi | Yes + MotoTRBO files |
| DX Cluster |  |  | No |
| APRS |  |  | No |

Station Data Format
-------------------

Station
 - callsign (String)
 - names (List<Name>)
 - dmr_ids (Set<Long>)
 - locations (List<GeoLocation>)
 - addresses (List<Address>)
 - attributes (Map<String, StationAttribute>)
 - locator (String)
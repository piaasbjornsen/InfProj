[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2165/gr2165)


## RESTAPI beskrivelse av kjøring
For å kjøre rest-tjenesten: 

# mvn install 
i hovedmappe (eliteserien)

# mvn jetty:run -D"jetty.port="8999" 
i integrationtests - mappa. Dette skal gjøre slik at serveren kjører og den kan aksesseres i nettleser på adressen: http://localhost:8999/eliteserien/

Deretter åpner man en ny terminal (mens server-terminalen står på og kjører serveren), går inn i fxui-mappa og kjører kommando:

# mvn javafx:run -Premoteapp

nå skal appen kjøre og kunne legge til team, slette team og legge til poeng. Tjenesten fungerer ikke perfekt enda, men det skal være mulig å utføre disse metodene i hvertfall.



## Group gr2165 repository 
 
This is a student project made by Group 65 with members Pia Asbjørnsen, Dorthea Munch-Ellingsen Selnes, Yngvil Skomedal and Ida-Sofie Pettersen. With this application you will see the status of the norwegian football league "Eliteserien" with teams, points and placement. You can also add point to teams after matches are played. 

## running the program

The project is in the folder "eliteserien" and is possible to build with maven. 

To run the program you will first have to run 'mvn install' in root folder (eliteserien) and in core-folder. Then run the command 'mvn javafx:run' in the fxui-folder for the application to run. 

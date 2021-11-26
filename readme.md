[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2165/gr2165)



## Group gr2165 repository 
 
This is a student project made by Group 65 with members Pia Asbjørnsen, Dorthea Munch-Ellingsen Selnes, Yngvil Skomedal and Ida-Sofie Pettersen. 

With this application the user will get access to a SportsTable where user can add match results, add and delete Teams and make new SportsTables. The user can save Tables in local file or in localhost server. 


## running the program

The project is in the folder "SportsTable" and is possible to build with maven. 

### Running LocalApp:
To run local app:

Command: "mvn install" in SportsTable folder.
Command: "mvn javafx:run" in fxui folder.

### running RemoteApp:
To run remote app:

command: "mvn install" in SportsTable folder. 
command: "mvn jetty:run -D"jetty.port="8999"" in integrationtests folder.

While server is running:
Open a new terminal
command: "mvn javafx:run -Premoteapp" in fxui folder.

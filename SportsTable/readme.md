
## Eliteserien

This project is a two-module application with a domenelayer, UI-layer and persistence layer using json. There are tests for all layers. The project is configured using maven. 

The application shows a table of the Norwegian football League "Eliteserien". There is a overview of the teams in the league and their points and placement. The user can add new match results and get an updated table with the new scores and placements of the teams, which will be saved in a json-file located in the users home folder. When the user restart the application after adding match results, they will see their saved version of the table.

## Example-image

This is how the table should look when its complete:

![Exampleimage](docs/images/eliteserienexample.PNG)

## Architecture

![Architecture](docs/images/architecture.png)

## How the code is organized

The project is organized with a maven setup:

# *core* module:
The core module contains classes with logic that organize and handle all data the application uses and needs to run. The core-module containts the domene layer (src/main/java/core) and the persistence layer(src/main/java/json). These folders are independent from eachother. The core-module is also independent from the fxui-module.

**src/main/java** contains source code and persistence.
**src/main/resources** contains resources (only one json-file for now).

# *fxui* module:
This module contains all classes and logic needed to show and use the data from the core-layer. The start-class use the fxml-file from resources to show output. The fxml-file uses the controller to handle input and output from core-module. 
**src/main/java** contains the controller and start-class.
**src/main/resources** contains the fxml file.

Both modules also have test-folders with the same structure as src-folders.
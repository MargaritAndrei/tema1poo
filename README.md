The classes are made using classic inheriternce.

Entity: Base class defining common attributes (name, mass) and contains the static method Entity.round() that replaces the round method specified in the statement.

Abstract Classes (Animal, Soil, Air, Plant): These classes define the respective entity, but not a specific type of that entity like Carnivores or Algae.

Cell: Contains the five unique entity types. It is responsible for most of the interactions between the entities.

Entity Subclasses (FloweringPlants, Herbivores, ForestSoil, TropicalAir, etc): These are the specific implementations. They have the common logic (e.g., Plant.grow(), Animal.move()) and their unique logic (categoryOxygen(), isPredator(), calculateQuality(), etc).

The Simulation constructor creates the simulation from the input. It creates unique instances for the entities and assigns them to the appropriate cells. Command handler methods in Simulation (dispatchCommand) call the specialized entity methods (air.handleWeatherEvent, soil.addSpecificFieldsToJson, etc).

The Main class implements a for-loop that simulates every single timestamp between commands most notably when the robot is charging to ensure the environment is up to date.


The implementation solves some of the tasks that were a bit poorly explained in the statement:

The animal movement algorithm (Animal.move) now integrates feeding/hunting logic. To respect the maximum 1 animal per cell rule, a predator attempts to eat its prey on the target cell before moving there (processMoveInteraction). This prevents two animal being on the same cell and fixes the logic. The feeding algorithm had an issue that made an animal eat itself that was fixed eventually.

The code incorporates logic to manage the lastInteractionTimestamp for water/soil interactions, ensuring that the delays specified in the requirements are correctly applied after TerraBot scans an entity.

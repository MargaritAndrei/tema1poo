package simulation;

import fileio.SimulationInput;
import fileio.AirInput;
import fileio.PairInput;
import fileio.SoilInput;
import fileio.WaterInput;
import fileio.CommandInput;
import fileio.AnimalInput;
import fileio.PlantInput;
import simulation.air.Air;
import simulation.air.MountainAir;
import simulation.air.DesertAir;
import simulation.air.PolarAir;
import simulation.air.TemperateAir;
import simulation.air.TropicalAir;
import simulation.animal.Animal;
import simulation.animal.Parasites;
import simulation.animal.Detritivores;
import simulation.animal.Carnivores;
import simulation.animal.Omnivores;
import simulation.animal.Herbivores;
import simulation.plant.Plant;
import simulation.plant.Algae;
import simulation.plant.Ferns;
import simulation.plant.FloweringPlants;
import simulation.plant.GymnospermsPlants;
import simulation.plant.Mosses;
import simulation.soil.DesertSoil;
import simulation.soil.ForestSoil;
import simulation.soil.GrasslandSoil;
import simulation.soil.Soil;
import simulation.soil.SwampSoil;
import simulation.soil.TundraSoil;
import simulation.water.Water;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Simulation {
    private Table map;
    private TerraBot robot;
    private List<Animal> animalList;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final double GOOD_QUALITY_THRESHOLD = 70.0;
    private static final double MODERATE_QUALITY_THRESHOLD = 40.0;
    public Simulation(final SimulationInput simulationData) {
        String[] dimensions = simulationData.getTerritoryDim().split("x");
        int height = Integer.parseInt(dimensions[0]);
        int width = Integer.parseInt(dimensions[1]);
        map = new Table(height, width);
        robot = new TerraBot(simulationData.getEnergyPoints());
        animalList = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell emptyCell = new Cell(null, null, null, null, null, i, j);
                map.setCell(i, j, emptyCell);
            }
        }
        for (SoilInput soilInput : simulationData.getTerritorySectionParams().getSoil()) {
            for (PairInput section : soilInput.getSections()) {
                Cell cell = map.getCell(section.getX(), section.getY());
                Soil soil = null;
                switch (soilInput.getType()) {
                    case "ForestSoil":
                        soil = new ForestSoil(soilInput.getName(), soilInput.getMass(),
                                soilInput.getNitrogen(), soilInput.getWaterRetention(),
                                soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                                soilInput.getLeafLitter());
                        break;
                    case "DesertSoil":
                        soil = new DesertSoil(soilInput.getName(), soilInput.getMass(),
                                soilInput.getNitrogen(), soilInput.getWaterRetention(),
                                soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                                soilInput.getSalinity());
                        break;
                    case "GrasslandSoil":
                        soil = new GrasslandSoil(soilInput.getName(), soilInput.getMass(),
                                soilInput.getNitrogen(), soilInput.getWaterRetention(),
                                soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                                soilInput.getRootDensity());
                        break;
                    case "SwampSoil":
                        soil = new SwampSoil(soilInput.getName(), soilInput.getMass(),
                                soilInput.getNitrogen(), soilInput.getWaterRetention(),
                                soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                                soilInput.getWaterLogging());
                        break;
                    case "TundraSoil":
                        soil = new TundraSoil(soilInput.getName(), soilInput.getMass(),
                                soilInput.getNitrogen(), soilInput.getWaterRetention(),
                                soilInput.getSoilpH(), soilInput.getOrganicMatter(),
                                soilInput.getPermafrostDepth());
                        break;
                    default:
                }
                cell.setSoil(soil);
            }
        }
        for (AirInput airInput : simulationData.getTerritorySectionParams().getAir()) {
            for (PairInput section : airInput.getSections()) {
                Cell cell = map.getCell(section.getX(), section.getY());
                Air air = null;
                switch (airInput.getType()) {
                    case "TropicalAir":
                        air = new TropicalAir(airInput.getName(), airInput.getMass(),
                                airInput.getHumidity(), airInput.getTemperature(),
                                airInput.getOxygenLevel(), airInput.getCo2Level());
                        break;
                    case "PolarAir":
                        air = new PolarAir(airInput.getName(), airInput.getMass(),
                                airInput.getHumidity(), airInput.getTemperature(),
                                airInput.getOxygenLevel(), airInput.getIceCrystalConcentration());
                        break;
                    case "TemperateAir":
                        air = new TemperateAir(airInput.getName(), airInput.getMass(),
                                airInput.getHumidity(), airInput.getTemperature(),
                                airInput.getOxygenLevel(), airInput.getPollenLevel());
                        break;
                    case "DesertAir":
                        air = new DesertAir(airInput.getName(), airInput.getMass(),
                                airInput.getHumidity(), airInput.getTemperature(),
                                airInput.getOxygenLevel(), airInput.getDustParticles());
                        break;
                    case "MountainAir":
                        air = new MountainAir(airInput.getName(), airInput.getMass(),
                                airInput.getHumidity(), airInput.getTemperature(),
                                airInput.getOxygenLevel(), airInput.getAltitude());
                        break;
                    default:
                }
                cell.setAir(air);
            }
        }
        for (PlantInput plantInput : simulationData.getTerritorySectionParams().getPlants()) {
            for (PairInput section : plantInput.getSections()) {
                Cell cell = map.getCell(section.getX(), section.getY());
                Plant plant = null;
                switch (plantInput.getType()) {
                    case "Algae":
                        plant = new Algae(plantInput.getName(), plantInput.getMass());
                        break;
                    case "Ferns":
                        plant = new Ferns(plantInput.getName(), plantInput.getMass());
                        break;
                    case "FloweringPlants":
                        plant = new FloweringPlants(plantInput.getName(), plantInput.getMass());
                        break;
                    case "GymnospermsPlants":
                        plant = new GymnospermsPlants(plantInput.getName(), plantInput.getMass());
                        break;
                    case "Mosses":
                        plant = new Mosses(plantInput.getName(), plantInput.getMass());
                        break;
                    default:
                }
                cell.setPlant(plant);
            }
        }
        for (WaterInput waterInput : simulationData.getTerritorySectionParams().getWater()) {
            for (PairInput section : waterInput.getSections()) {
                Water water = new Water(waterInput.getName(), waterInput.getMass(),
                        waterInput.getType(), waterInput.getSalinity(), waterInput.getPH(),
                        waterInput.getPurity(), waterInput.getTurbidity(),
                        waterInput.getContaminantIndex(), waterInput.isFrozen());
                Cell cell = map.getCell(section.getX(), section.getY());
                cell.setWater(water);
            }
        }
        for (AnimalInput animalInput : simulationData.getTerritorySectionParams().getAnimals()) {
            for (PairInput section : animalInput.getSections()) {
                Cell cell = map.getCell(section.getX(), section.getY());
                Animal animal = null;
                switch (animalInput.getType()) {
                    case "Carnivores":
                        animal = new Carnivores(animalInput.getName(), animalInput.getMass());
                        break;
                    case "Detritivores":
                        animal = new Detritivores(animalInput.getName(), animalInput.getMass());
                        break;
                    case "Herbivores":
                        animal = new Herbivores(animalInput.getName(), animalInput.getMass());
                        break;
                    case "Omnivores":
                        animal = new Omnivores(animalInput.getName(), animalInput.getMass());
                        break;
                    case "Parasites":
                        animal = new Parasites(animalInput.getName(), animalInput.getMass());
                        break;
                    default:
                }
                animal.setX(section.getX()); animal.setY(section.getY());
                animalList.add(animal); cell.setAnimal(animal);
            }
        }
    }
    /**
     Proceseaza interactiunile automate din mediu de la timestamp-ul curent.
     */
    public void evolveEnvironment(final int currentTimestamp) {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Cell currentCell = map.getCell(i, j);
                currentCell.processEvolution(currentTimestamp);
            }
        }
        for (Animal animal : animalList) {
            if (animal.scanned) {
                int oldX = animal.getX();
                int oldY = animal.getY();
                Cell newCell = animal.move(map, oldX, oldY, currentTimestamp);
                if (newCell != null) {
                    int newX = newCell.getX();
                    int newY = newCell.getY();
                    map.getCell(oldX, oldY).setAnimal(null);
                    map.getCell(newX, newY).setAnimal(animal);
                    animal.setX(newX);
                    animal.setY(newY);
                }
            }
        }
    }
    private String interpretQuality(final double score) {
        if (score >= GOOD_QUALITY_THRESHOLD) {
            return "good";
        }
        if (score >= MODERATE_QUALITY_THRESHOLD) {
            return "moderate";
        }
        return "poor";
    }
    private ObjectNode createMessageNode(final String message) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("message", message);
        return node;
    }
    private ObjectNode createErrorNode(final String message) {
        return createMessageNode(message);
    }
    /**
     Proceseaza comanda robotului primita din input.
     */
    public ObjectNode dispatchCommand(final CommandInput cmd, final int currentTimestamp) {
        switch (cmd.getCommand()) {
            case "moveRobot":
                return handleMoveRobot(currentTimestamp);
            case "rechargeBattery":
                return handleRechargeBattery(cmd, currentTimestamp);
            case "scanObject":
                return handleScanObject(cmd, currentTimestamp);
            case "learnFact":
                return handleLearnFact(cmd, currentTimestamp);
            case "improveEnvironment":
                return handleImproveEnvironment(cmd, currentTimestamp);
            case "changeWeatherConditions":
                return handleChangeWeather(cmd, currentTimestamp);
            case "getEnergyStatus":
                return getEnergyStatus(currentTimestamp);
            case "printKnowledgeBase":
                return printKnowledgeBase(currentTimestamp);
            case "printEnvConditions":
                return printEnvConditions(currentTimestamp);
            case "printMap":
                return printMap(currentTimestamp);
            default:
                return createErrorNode("ERROR: Unknown command.");
        }
    }

    private ObjectNode createSoilNode(final Soil soil) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", soil.getClass().getSimpleName());
        node.put("name", soil.getName());
        node.put("mass", soil.getMass());
        node.put("nitrogen", soil.getNitrogen());
        node.put("waterRetention", soil.getWaterRetention());
        node.put("soilpH", soil.getSoilpH());
        node.put("organicMatter", soil.getOrganicMatter());
        node.put("soilQuality", soil.calculateQuality());
        soil.addSpecificFieldsToJson(node);
        return node;
    }
    private ObjectNode createAirNode(final Air air, final int timestamp) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", air.getClass().getSimpleName());
        node.put("name", air.getName());
        node.put("mass", air.getMass());
        node.put("humidity", air.getHumidity());
        node.put("temperature", air.getTemperature());
        node.put("oxygenLevel", air.getOxygenLevel());
        node.put("airQuality", air.airQualityScore(timestamp));
        air.addSpecificFieldsToJson(node);
        return node;
    }

    private ObjectNode createPlantNode(final Plant plant) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", plant.getClass().getSimpleName());
        node.put("name", plant.getName());
        node.put("mass", plant.getMass());
        return node;
    }

    private ObjectNode createAnimalNode(final Animal animal) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", animal.getClass().getSimpleName());
        node.put("name", animal.getName());
        node.put("mass", animal.getMass());
        return node;
    }

    private ObjectNode createWaterNode(final Water water) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", water.getType());
        node.put("name", water.getName());
        node.put("mass", water.getMass());
        return node;
    }

    /**
     Proceseaza miscarea robotului.
     */
    public ObjectNode handleMoveRobot(final int currentTimestamp) {
        String message = robot.move(this.map, currentTimestamp);
        return createMessageNode(message);
    }
    /**
     Proceseaza incarcarea bateriei robotului.
     */
    public ObjectNode handleRechargeBattery(final CommandInput cmd, final int currentTimestamp) {
        String message = robot.rechargeBattery(cmd.getTimeToCharge(), currentTimestamp);
        return createMessageNode(message);
    }
    /**
     Proceseaza comanda scanobject.
     */
    public ObjectNode handleScanObject(final CommandInput cmd, final int currentTimestamp) {
        Cell currentCell = map.getCell(robot.getX(), robot.getY());
        String message = robot.scanObject(currentCell, cmd.getColor(), cmd.getSmell(),
                cmd.getSound(), currentTimestamp);
        return createMessageNode(message);
    }
    /**
     Proceseaza comanda learnfact.
     */
    public ObjectNode handleLearnFact(final CommandInput cmd, final int currentTimestamp) {
        String message = robot.learnFact(cmd.getSubject(), cmd.getComponents(), currentTimestamp);
        return createMessageNode(message);
    }
    /**
     Proceseaza comanda improveenvironment.
     */
    public ObjectNode handleImproveEnvironment(final CommandInput cmd, final int currentTimestamp) {
        Cell currentCell = map.getCell(robot.getX(), robot.getY());
        String message = robot.improveEnvironment(currentCell, cmd.getImprovementType(),
                cmd.getName(), currentTimestamp);
        return createMessageNode(message);
    }
    /**
     Proceseaza schimbarea vremii.
     */
    public ObjectNode handleChangeWeather(final CommandInput cmd, final int currentTimestamp) {
        boolean affected = false;
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Cell cell = map.getCell(i, j);
                if (cell != null && cell.getAir() != null
                        && cell.getAir().handleWeatherEvent(cmd, currentTimestamp)) {
                    affected = true;
                }
            }
        }
        if (!affected) {
            return createErrorNode("ERROR: The weather change does not affect the environment."
                    + " Cannot perform action");
        }
        return createMessageNode("The weather has changed.");
    }
    /**
     Proceseaza comanda getenergystatus.
     */
    public ObjectNode getEnergyStatus(final int currentTimestamp) {
        if (robot.isCharging(currentTimestamp)) {
            return createErrorNode("ERROR: Robot still charging. Cannot perform action");
        }
        String message = "TerraBot has " + robot.getEnergy() + " energy points left.";
        return createMessageNode(message);
    }
    /**
     Proceseaza comanda printknowledgebase.
     */
    public ObjectNode printKnowledgeBase(final int currentTimestamp) {
        if (robot.isCharging(currentTimestamp)) {
            return createErrorNode("ERROR: Robot still charging. Cannot perform action");
        }

        ObjectNode output = MAPPER.createObjectNode();
        ArrayNode factsArray = MAPPER.createArrayNode();

        Map<String, List<String>> db = robot.getDatabase();
        for (Map.Entry<String, List<String>> entry : db.entrySet()) {
            String topic = entry.getKey();
            List<String> facts = entry.getValue();

            ObjectNode factNode = MAPPER.createObjectNode();
            factNode.put("topic", topic);

            ArrayNode subjectArray = MAPPER.createArrayNode();
            for (String fact : facts) {
                subjectArray.add(fact);
            }
            factNode.set("facts", subjectArray);
            factsArray.add(factNode);
        }
        output.set("output", factsArray);
        return output;
    }
    /**
     Proceseaza comanda printenvconditions.
     */
    public ObjectNode printEnvConditions(final int currentTimestamp) {
        if (robot.isCharging(currentTimestamp)) {
            return createErrorNode("ERROR: Robot still charging. Cannot perform action");
        }

        ObjectNode output = MAPPER.createObjectNode();
        Cell cell = map.getCell(robot.getX(), robot.getY());
        ObjectNode data = MAPPER.createObjectNode();
        data.set("soil", createSoilNode(cell.getSoil()));

        if (cell.getPlant() != null) {
            data.set("plants", createPlantNode(cell.getPlant()));
        }
        if (cell.getAnimal() != null) {
            data.set("animals", createAnimalNode(cell.getAnimal()));
        }
        if (cell.getWater() != null) {
            data.set("water", createWaterNode(cell.getWater()));
        }
        data.set("air", createAirNode(cell.getAir(), currentTimestamp));
        output.set("output", data);
        return output;
    }
    /**
     Proceseaza comanda printMap.
     */
    public ObjectNode printMap(final int currentTimestamp) {
        if (robot.isCharging(currentTimestamp)) {
            return createErrorNode("ERROR: Robot still charging. Cannot perform action");
        }
        ObjectNode output = MAPPER.createObjectNode();
        ArrayNode mapArray = MAPPER.createArrayNode();
        for (int j = 0; j < map.getWidth(); j++) {
            for (int i = 0; i < map.getHeight(); i++) {
                Cell cell = map.getCell(i, j);
                ObjectNode cellNode = MAPPER.createObjectNode();
                ArrayNode section = MAPPER.createArrayNode();
                section.add(i);
                section.add(j);
                cellNode.set("section", section);
                // System.out.print("X: " + cell.getX() + " Y: " + cell.getY() + " ");
                int objectCount = 0;
                if (cell.getPlant() != null) {
                    // System.out.print("Plant: " + cell.getPlant().getName() + " ");
                    objectCount++;
                }
                if (cell.getAnimal() != null) {
                    // System.out.print("Animal: " + cell.getAnimal().getName() + " ");
                    objectCount++;
                }
                if (cell.getWater() != null) {
                    // System.out.print("Water: " + cell.getWater().getName());
                    objectCount++;
                }
                // System.out.println();
                cellNode.put("totalNrOfObjects", objectCount);

                cellNode.put("airQuality",
                        interpretQuality(cell.getAir().airQualityScore(currentTimestamp)));
                cellNode.put("soilQuality", interpretQuality(cell.getSoil().calculateQuality()));

                mapArray.add(cellNode);
            }
        }
        output.set("output", mapArray);
        return output;
    }
}

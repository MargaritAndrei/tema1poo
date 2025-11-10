package simulation;

import fileio.*;
import simulation.air.*;
import simulation.animal.*;
import simulation.plant.*;
import simulation.soil.*;
import simulation.water.Water;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Simulation {
    private Table map;
    private TerraBot robot;
    private List<Animal> animalList;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public Simulation(SimulationInput simulationData) {
        String[] dimensions = simulationData.territoryDim.split("x");
        int height = Integer.parseInt(dimensions[0]);
        int width = Integer.parseInt(dimensions[1]);
        map = new Table(height, width);
        robot = new TerraBot(simulationData.energyPoints);
        animalList = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell emptyCell = new Cell(null, null, null, null, null, i, j);
                map.setCell(i, j, emptyCell);
            }
        }
        for (SoilInput soilInput : simulationData.territorySectionParams.soil) {
            switch (soilInput.type) {
                case "ForestSoil":
                    ForestSoil forestSoil = new ForestSoil(soilInput.name, soilInput.mass, soilInput.nitrogen,
                            soilInput.waterRetention, soilInput.soilpH, soilInput.organicMatter, soilInput.leafLitter);
                    for (PairInput section : soilInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setSoil(forestSoil);
                    }
                    break;
                case "DesertSoil":
                    DesertSoil desertSoil = new DesertSoil(soilInput.name, soilInput.mass, soilInput.nitrogen,
                            soilInput.waterRetention, soilInput.soilpH, soilInput.organicMatter, soilInput.salinity);
                    for (PairInput section : soilInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setSoil(desertSoil);
                    }
                    break;
                case "GrasslandSoil":
                    GrasslandSoil grasslandSoil = new GrasslandSoil(soilInput.name, soilInput.mass, soilInput.nitrogen,
                            soilInput.waterRetention, soilInput.soilpH, soilInput.organicMatter, soilInput.rootDensity);
                    for (PairInput section : soilInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setSoil(grasslandSoil);
                    }
                    break;
                case "SwampSoil":
                    SwampSoil swampSoil = new SwampSoil(soilInput.name, soilInput.mass, soilInput.nitrogen,
                            soilInput.waterRetention, soilInput.soilpH, soilInput.organicMatter, soilInput.waterLogging);
                    for (PairInput section : soilInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setSoil(swampSoil);
                    }
                    break;
                case "TundraSoil":
                    TundraSoil tundraSoil = new TundraSoil(soilInput.name, soilInput.mass, soilInput.nitrogen,
                            soilInput.waterRetention, soilInput.soilpH, soilInput.organicMatter, soilInput.permafrostDepth);
                    for (PairInput section : soilInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setSoil(tundraSoil);
                    }
                    break;
            }
        }
        for (AirInput airInput : simulationData.territorySectionParams.air) {
            switch (airInput.type) {
                case "TropicalAir":
                    TropicalAir tropicalAir = new TropicalAir (airInput.name, airInput.mass, airInput.humidity,
                            airInput.temperature, airInput.oxygenLevel, airInput.co2Level);
                    for (PairInput section : airInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setAir(tropicalAir);
                    }
                    break;
                case "PolarAir":
                    PolarAir polarAir = new PolarAir (airInput.name, airInput.mass, airInput.humidity,
                            airInput.temperature, airInput.oxygenLevel, airInput.co2Level);
                    for (PairInput section : airInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setAir(polarAir);
                    }
                    break;
                case "TemperateAir":
                    TemperateAir temperateAir = new TemperateAir (airInput.name, airInput.mass,
                            airInput.humidity, airInput.temperature, airInput.oxygenLevel, airInput.pollenLevel);
                    for (PairInput section : airInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setAir(temperateAir);
                    }
                    break;
                case "DesertAir":
                    DesertAir desertAir = new DesertAir (airInput.name, airInput.mass, airInput.humidity,
                            airInput.temperature, airInput.oxygenLevel, airInput.dustParticles);
                    for (PairInput section : airInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setAir(desertAir);
                    }
                    break;
                case "MountainAir":
                    MountainAir mountainAir = new MountainAir (airInput.name, airInput.mass, airInput.humidity,
                            airInput.temperature, airInput.oxygenLevel, airInput.altitude);
                    for (PairInput section : airInput.sections) {
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setAir(mountainAir);
                    }
                    break;
            }
        }
        for (PlantInput plantInput : simulationData.territorySectionParams.plants) {
            switch (plantInput.type) {
                case "Algae":
                    for (PairInput section : plantInput.sections) {
                        Algae algae = new Algae(plantInput.name, plantInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setPlant(algae);
                    }
                    break;
                case "Ferns":
                    for (PairInput section : plantInput.sections) {
                        Ferns fern = new Ferns(plantInput.name, plantInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setPlant(fern);
                    }
                    break;
                case "FloweringPlants":
                    for (PairInput section : plantInput.sections) {
                        FloweringPlants floweringPlant = new FloweringPlants(plantInput.name, plantInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setPlant(floweringPlant);
                    }
                    break;
                case "GymnospermsPlants":
                    for (PairInput section : plantInput.sections) {
                        GymnospermsPlants gymnospermsPlant = new GymnospermsPlants(plantInput.name, plantInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setPlant(gymnospermsPlant);
                    }
                    break;
                case "Mosses":
                    for (PairInput section : plantInput.sections) {
                        Mosses mosse = new Mosses(plantInput.name, plantInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        cell.setPlant(mosse);
                    }
                    break;
            }
        }
        for (WaterInput waterInput : simulationData.territorySectionParams.water) {
            for (PairInput section : waterInput.sections) {
                Water water = new Water(waterInput.name, waterInput.mass, waterInput.type, waterInput.salinity, waterInput.pH,
                        waterInput.purity, waterInput.turbidity, waterInput.contaminantIndex, waterInput.isFrozen);
                Cell cell = map.getCell(section.x, section.y);
                cell.setWater(water);
            }
        }
        for (AnimalInput animalInput : simulationData.territorySectionParams.animals) {
            switch(animalInput.type) {
                case "Carnivores":
                    for (PairInput section : animalInput.sections) {
                        Carnivores carnivore = new Carnivores(animalInput.name, animalInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        carnivore.setX(section.x);
                        carnivore.setY(section.y);
                        cell.setAnimal(carnivore);
                        animalList.add(carnivore);
                    }
                    break;
                case "Detritivores":
                    for (PairInput section : animalInput.sections) {
                        Detritivores detritivore = new Detritivores(animalInput.name, animalInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        detritivore.setX(section.x);
                        detritivore.setY(section.y);
                        cell.setAnimal(detritivore);
                        animalList.add(detritivore);
                    }
                    break;
                case "Herbivores":
                    for (PairInput section : animalInput.sections) {
                        Herbivores herbivore = new Herbivores(animalInput.name, animalInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        herbivore.setX(section.x);
                        herbivore.setY(section.y);
                        cell.setAnimal(herbivore);
                        animalList.add(herbivore);
                    }
                    break;
                case "Omnivores":
                    for (PairInput section : animalInput.sections) {
                        Omnivores omnivore = new Omnivores(animalInput.name, animalInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        omnivore.setX(section.x);
                        omnivore.setY(section.y);
                        cell.setAnimal(omnivore);
                        animalList.add(omnivore);
                    }
                    break;
                case "Parasites":
                    for (PairInput section : animalInput.sections) {
                        Parasites parasite = new Parasites(animalInput.name, animalInput.mass);
                        Cell cell = map.getCell(section.x, section.y);
                        parasite.setX(section.x);
                        parasite.setY(section.y);
                        cell.setAnimal(parasite);
                        animalList.add(parasite);
                    }
                    break;
            }
        }
    }
    public void evolveEnvironment(int currentTimestamp) {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Cell currentCell = map.getCell(i, j);
                currentCell.processEvolution(currentTimestamp);
            }
        }
        for (Animal animal : animalList) {
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


    private String interpretQuality(double score) {
        if (score >= 70) return "good";
        if (score >= 40) return "moderate";
        return "poor";
    }
    private ObjectNode createMessageNode(String message) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("message", message);
        return node;
    }
    private ObjectNode createErrorNode(String message) {
        return createMessageNode(message);
    }
    public ObjectNode dispatchCommand(CommandInput cmd, int currentTimestamp) {
        switch (cmd.command) {
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

    private ObjectNode createSoilNode(Soil soil) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", soil.getClass().getSimpleName());
        node.put("name", soil.getName());
        node.put("mass", soil.getMass());
        node.put("nitrogen", soil.getNitrogen());
        node.put("waterRetention", soil.getWaterRetention());
        node.put("soilpH", soil.getSoilpH());
        node.put("organicMatter", soil.getOrganicMatter());
        node.put("soilQuality", soil.calculateQuality());

        if (soil instanceof ForestSoil) {
            node.put("leafLitter", ((ForestSoil) soil).getLeafLitter());
        } else if (soil instanceof SwampSoil) {
            node.put("waterLogging", ((SwampSoil) soil).getWaterLogging());
        } else if (soil instanceof DesertSoil) {
            node.put("salinity", ((DesertSoil) soil).getSalinity());
        } else if (soil instanceof GrasslandSoil) {
            node.put("rootDensity", ((GrasslandSoil) soil).getRootDensity());
        } else if (soil instanceof TundraSoil) {
            node.put("permafrostDepth", ((TundraSoil) soil).getPermafrostDepth());
        }
        return node;
    }
    private ObjectNode createAirNode(Air air, int timestamp) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", air.getClass().getSimpleName());
        node.put("name", air.getName());
        node.put("mass", air.getMass());
        node.put("humidity", air.getHumidity());
        node.put("temperature", air.getTemperature());
        node.put("oxygenLevel", air.getOxygenLevel());
        node.put("airQuality", air.airQualityScore(timestamp));

        if (air instanceof TropicalAir) {
            node.put("co2Level", ((TropicalAir) air).getCo2Level());
        } else if (air instanceof PolarAir) {
            node.put("iceCrystalConcentration", ((PolarAir) air).getIceCrystalConcentration());
        } else if (air instanceof TemperateAir) {
            node.put("pollenLevel", ((TemperateAir) air).getPollenLevel());
        } else if (air instanceof DesertAir) {
            node.put("dustParticles", ((DesertAir) air).getDustParticles());
        } else if (air instanceof MountainAir) {
            node.put("altitude", ((MountainAir) air).getAltitude());
        }
        return node;
    }

    private ObjectNode createPlantNode(Plant plant) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", plant.getClass().getSimpleName());
        node.put("name", plant.getName());
        node.put("mass", plant.getMass());
        return node;
    }

    private ObjectNode createAnimalNode(Animal animal) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", animal.getClass().getSimpleName());
        node.put("name", animal.getName());
        node.put("mass", animal.getMass());
        return node;
    }

    private ObjectNode createWaterNode(Water water) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("type", water.getType());
        node.put("name", water.getName());
        node.put("mass", water.getMass());
        node.put("purity", water.getPurity());
        node.put("salinity", water.getSalinity());
        node.put("turbidity", water.getTurbidity());
        node.put("contaminantIndex", water.getContaminantIndex());
        node.put("pH", water.getPh());
        node.put("isFrozen", water.isFrozen);
        return node;
    }


    public ObjectNode handleMoveRobot(int currentTimestamp) {
        String message = robot.move(this.map, currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleRechargeBattery(CommandInput cmd, int currentTimestamp) {
        String message = robot.rechargeBattery(cmd.timeToCharge, currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleScanObject(CommandInput cmd, int currentTimestamp) {
        Cell currentCell = map.getCell(robot.getX(), robot.getY());
        String message = robot.scanObject(currentCell, cmd.color, cmd.smell, cmd.sound, currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleLearnFact(CommandInput cmd, int currentTimestamp) {
        String message = robot.learnFact(cmd.subject, cmd.components, currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleImproveEnvironment(CommandInput cmd, int currentTimestamp) {
        Cell currentCell = map.getCell(robot.getX(), robot.getY());
        String message = robot.improveEnvironment(currentCell, cmd.improvementType, cmd.name, currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleChangeWeather(CommandInput cmd, int currentTimestamp) {
        boolean affected = false;
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Cell cell = map.getCell(i, j);
                if (cell != null && cell.getAir() != null) {
                    Air air = cell.getAir();
                    double value = 0;
                    switch (cmd.type) {
                        case "rainfall":
                            value = cmd.rainfall;
                            if (air instanceof TropicalAir) {
                                affected = true;
                            }
                            break;
                        case "polarStorm":
                            value = cmd.windSpeed;
                            if (air instanceof PolarAir) {
                                affected = true;
                            }
                            break;
                        case "newSeason":
                            value = cmd.season.equalsIgnoreCase("Spring") ? 15 : 0;
                            if (air instanceof TemperateAir) {
                                affected = true;
                            }
                            break;
                        case "desertStorm":
                            value = cmd.desertStorm ? 30 : 0;
                            if (air instanceof DesertAir) {
                                affected = true;
                            }
                            break;
                        case "peopleHiking":
                            value = cmd.numberOfHikers;
                            if (air instanceof MountainAir) {
                                affected = true;
                            }
                            break;
                    }
                    air.applyWeatherChange(cmd.type, value, currentTimestamp);
                }
            }
        }
        if (!affected) {
            return createErrorNode("ERROR: The weather change does not affect the environment. Cannot perform action");
        }
        return createMessageNode("The weather has changed.");
    }


    public ObjectNode getEnergyStatus(int currentTimestamp) {
        if (robot.isCharging(currentTimestamp)) {
            return createErrorNode("ERROR: Robot still charging. Cannot perform action");
        }
        String message = "TerraBot has " + robot.getEnergy() + " energy points left.";
        return createMessageNode(message);
    }
    public ObjectNode printKnowledgeBase(int currentTimestamp) {
        if (robot.isCharging(currentTimestamp)) {
            return createErrorNode("ERROR: Robot still charging. Cannot perform action");
        }

        ObjectNode output = MAPPER.createObjectNode();
        ArrayNode factsArray = MAPPER.createArrayNode();

        Map<String, List<String>> db = robot.getDatabase();

        for (String topic : robot.getInventory()) {
            if (db.containsKey(topic)) {
                ObjectNode factNode = MAPPER.createObjectNode();
                factNode.put("topic", topic);

                ArrayNode subjectArray = MAPPER.createArrayNode();
                for (String fact : db.get(topic)) {
                    subjectArray.add(fact);
                }
                factNode.set("facts", subjectArray);
                factsArray.add(factNode);
            }
        }
        output.set("output", factsArray);
        return output;
    }
    public ObjectNode printEnvConditions(int currentTimestamp) {
        if (robot.isCharging(currentTimestamp)) {
            return createErrorNode("ERROR: Robot still charging. Cannot perform action");
        }

        ObjectNode output = MAPPER.createObjectNode();
        Cell cell = map.getCell(robot.getX(), robot.getY());
        ObjectNode data = MAPPER.createObjectNode();

        if (cell.getSoil() != null) {
            data.set("soil", createSoilNode(cell.getSoil()));
        }
        if (cell.getPlant() != null) {
            data.set("plants", createPlantNode(cell.getPlant()));
        }
        if (cell.getAnimal() != null) {
            data.set("animals", createAnimalNode(cell.getAnimal()));
        }
        if (cell.getWater() != null) {
            data.set("water", createWaterNode(cell.getWater()));
        }
        if (cell.getAir() != null) {
            data.set("air", createAirNode(cell.getAir(), currentTimestamp));
        }
        output.set("output", data);
        return output;
    }
    public ObjectNode printMap(int currentTimestamp) {
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

                int objectCount = 0;
                if (cell.getPlant() != null) objectCount++;
                if (cell.getAnimal() != null) objectCount++;
                if (cell.getWater() != null) objectCount++;
                cellNode.put("totalNrOfObjects", objectCount);

                cellNode.put("airQuality", interpretQuality(cell.getAir().airQualityScore(currentTimestamp)));
                cellNode.put("soilQuality", interpretQuality(cell.getSoil().calculateQuality()));

                mapArray.add(cellNode);
            }
        }
        output.set("output", mapArray);
        return output;
    }
}

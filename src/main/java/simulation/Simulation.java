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
            switch (soilInput.getType()) {
                case "ForestSoil":
                    for (PairInput section : soilInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        ForestSoil forestSoil = new ForestSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(),
                                soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getLeafLitter());
                        cell.setSoil(forestSoil);
                    }
                    break;
                case "DesertSoil":
                    for (PairInput section : soilInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        DesertSoil desertSoil = new DesertSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(),
                                soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getSalinity());
                        cell.setSoil(desertSoil);
                    }
                    break;
                case "GrasslandSoil":
                    for (PairInput section : soilInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        GrasslandSoil grasslandSoil = new GrasslandSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(),
                                soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getRootDensity());
                        cell.setSoil(grasslandSoil);
                    }
                    break;
                case "SwampSoil":
                    for (PairInput section : soilInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        SwampSoil swampSoil = new SwampSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(),
                                soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getWaterLogging());
                        cell.setSoil(swampSoil);
                    }
                    break;
                case "TundraSoil":
                    for (PairInput section : soilInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        TundraSoil tundraSoil = new TundraSoil(soilInput.getName(), soilInput.getMass(), soilInput.getNitrogen(),
                                soilInput.getWaterRetention(), soilInput.getSoilpH(), soilInput.getOrganicMatter(), soilInput.getPermafrostDepth());
                        cell.setSoil(tundraSoil);
                    }
                    break;
            }
        }
        for (AirInput airInput : simulationData.getTerritorySectionParams().getAir()) {
            switch (airInput.getType()) {
                case "TropicalAir":
                    for (PairInput section : airInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        TropicalAir tropicalAir = new TropicalAir (airInput.getName(), airInput.getMass(), airInput.getHumidity(),
                                airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getCo2Level());
                        cell.setAir(tropicalAir);
                    }
                    break;
                case "PolarAir":
                    for (PairInput section : airInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        PolarAir polarAir = new PolarAir (airInput.getName(), airInput.getMass(), airInput.getHumidity(),
                                airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getIceCrystalConcentration());
                        cell.setAir(polarAir);
                    }
                    break;
                case "TemperateAir":
                    for (PairInput section : airInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        TemperateAir temperateAir = new TemperateAir (airInput.getName(), airInput.getMass(),
                                airInput.getHumidity(), airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getPollenLevel());
                        cell.setAir(temperateAir);
                    }
                    break;
                case "DesertAir":
                    for (PairInput section : airInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        DesertAir desertAir = new DesertAir (airInput.getName(), airInput.getMass(), airInput.getHumidity(),
                                airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getDustParticles());
                        cell.setAir(desertAir);
                    }
                    break;
                case "MountainAir":
                    for (PairInput section : airInput.getSections()) {
                        Cell cell = map.getCell(section.getX(), section.getY());
                        MountainAir mountainAir = new MountainAir (airInput.getName(), airInput.getMass(), airInput.getHumidity(),
                                airInput.getTemperature(), airInput.getOxygenLevel(), airInput.getAltitude());
                        cell.setAir(mountainAir);
                    }
                    break;
            }
        }
        for (PlantInput plantInput : simulationData.getTerritorySectionParams().getPlants()) {
            switch (plantInput.getType()) {
                case "Algae":
                    for (PairInput section : plantInput.getSections()) {
                        Algae algae = new Algae(plantInput.getName(), plantInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        cell.setPlant(algae);
                    }
                    break;
                case "Ferns":
                    for (PairInput section : plantInput.getSections()) {
                        Ferns fern = new Ferns(plantInput.getName(), plantInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        cell.setPlant(fern);
                    }
                    break;
                case "FloweringPlants":
                    for (PairInput section : plantInput.getSections()) {
                        FloweringPlants floweringPlant = new FloweringPlants(plantInput.getName(), plantInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        cell.setPlant(floweringPlant);
                    }
                    break;
                case "GymnospermsPlants":
                    for (PairInput section : plantInput.getSections()) {
                        GymnospermsPlants gymnospermsPlant = new GymnospermsPlants(plantInput.getName(), plantInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        cell.setPlant(gymnospermsPlant);
                    }
                    break;
                case "Mosses":
                    for (PairInput section : plantInput.getSections()) {
                        Mosses mosse = new Mosses(plantInput.getName(), plantInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        cell.setPlant(mosse);
                    }
                    break;
            }
        }
        for (WaterInput waterInput : simulationData.getTerritorySectionParams().getWater()) {
            for (PairInput section : waterInput.getSections()) {
                Water water = new Water(waterInput.getName(), waterInput.getMass(), waterInput.getType(),
                        waterInput.getSalinity(), waterInput.getPH(), waterInput.getPurity(),
                        waterInput.getTurbidity(), waterInput.getContaminantIndex(), waterInput.isFrozen());
                Cell cell = map.getCell(section.getX(), section.getY());
                cell.setWater(water);
            }
        }
        for (AnimalInput animalInput : simulationData.getTerritorySectionParams().getAnimals()) {
            switch(animalInput.getType()) {
                case "Carnivores":
                    for (PairInput section : animalInput.getSections()) {
                        Carnivores carnivore = new Carnivores(animalInput.getName(), animalInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        carnivore.setX(section.getX());
                        carnivore.setY(section.getY());
                        cell.setAnimal(carnivore);
                        animalList.add(carnivore);
                    }
                    break;
                case "Detritivores":
                    for (PairInput section : animalInput.getSections()) {
                        Detritivores detritivore = new Detritivores(animalInput.getName(), animalInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        detritivore.setX(section.getX());
                        detritivore.setY(section.getY());
                        cell.setAnimal(detritivore);
                        animalList.add(detritivore);
                    }
                    break;
                case "Herbivores":
                    for (PairInput section : animalInput.getSections()) {
                        Herbivores herbivore = new Herbivores(animalInput.getName(), animalInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        herbivore.setX(section.getX());
                        herbivore.setY(section.getY());
                        cell.setAnimal(herbivore);
                        animalList.add(herbivore);
                    }
                    break;
                case "Omnivores":
                    for (PairInput section : animalInput.getSections()) {
                        Omnivores omnivore = new Omnivores(animalInput.getName(), animalInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        omnivore.setX(section.getX());
                        omnivore.setY(section.getY());
                        cell.setAnimal(omnivore);
                        animalList.add(omnivore);
                    }
                    break;
                case "Parasites":
                    for (PairInput section : animalInput.getSections()) {
                        Parasites parasite = new Parasites(animalInput.getName(), animalInput.getMass());
                        Cell cell = map.getCell(section.getX(), section.getY());
                        parasite.setX(section.getX());
                        parasite.setY(section.getY());
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
        soil.addSpecificFieldsToJson(node);
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
        air.addSpecificFieldsToJson(node);
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
        return node;
    }


    public ObjectNode handleMoveRobot(int currentTimestamp) {
        String message = robot.move(this.map, currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleRechargeBattery(CommandInput cmd, int currentTimestamp) {
        String message = robot.rechargeBattery(cmd.getTimeToCharge(), currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleScanObject(CommandInput cmd, int currentTimestamp) {
        Cell currentCell = map.getCell(robot.getX(), robot.getY());
        String message = robot.scanObject(currentCell, cmd.getColor(), cmd.getSmell(), cmd.getSound(), currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleLearnFact(CommandInput cmd, int currentTimestamp) {
        String message = robot.learnFact(cmd.getSubject(), cmd.getComponents(), currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleImproveEnvironment(CommandInput cmd, int currentTimestamp) {
        Cell currentCell = map.getCell(robot.getX(), robot.getY());
        String message = robot.improveEnvironment(currentCell, cmd.getImprovementType(), cmd.getName(), currentTimestamp);
        return createMessageNode(message);
    }
    public ObjectNode handleChangeWeather(CommandInput cmd, int currentTimestamp) {
        boolean affected = false;
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Cell cell = map.getCell(i, j);
                if (cell != null && cell.getAir() != null &&
                        cell.getAir().handleWeatherEvent(cmd, currentTimestamp)) {
                    affected = true;
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
    public ObjectNode printEnvConditions(int currentTimestamp) {
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

                cellNode.put("airQuality", interpretQuality(cell.getAir().airQualityScore(currentTimestamp)));
                cellNode.put("soilQuality", interpretQuality(cell.getSoil().calculateQuality()));

                mapArray.add(cellNode);
            }
        }
        output.set("output", mapArray);
        return output;
    }
}

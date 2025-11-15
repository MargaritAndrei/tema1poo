package simulation;

import simulation.animal.Animal;
import simulation.plant.Plant;
import simulation.soil.Soil;
import simulation.water.Water;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerraBot {
    public TerraBot(int initialEnergy) {
        energy = initialEnergy;
        x = 0;
        y = 0;
        inventory = new ArrayList<>();
        database = new HashMap<>();
        chargeEndTime = 0;
    }
    private int energy;
    private int x, y;
    private int chargeEndTime;
    private List<String> inventory;
    private Map<String, List<String>> database;
    public int getEnergy() {
        return energy;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public List<String> getInventory() {
        return inventory;
    }
    public Map<String, List<String>> getDatabase() {
        return database;
    }
    public boolean isCharging(int currentTimestamp) {
        return currentTimestamp < chargeEndTime;
    }
    public boolean spendEnergy(int amount) {
        if (energy >= amount) {
            this.energy -= amount;
            return true;
        }
        return false;
    }
    public String move(Table map, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        int dx[] = {0, 1, 0, -1};
        int dy[] = {1, 0, -1, 0};
        int bestX = x;
        int bestY = y;
        int bestScore = Integer.MAX_VALUE;
        // System.out.println("New move");
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            // System.out.println("Direction is " + x + " " + y + " " + newX + " " + newY);
            Cell neighborCell = map.getCell(newX, newY);
            if (neighborCell != null) {
                int currentScore = neighborCell.calculateRobotRiskScore(currentTimestamp);
                // System.out.println(currentScore);
                if (currentScore < bestScore) {
                    bestScore = currentScore;
                    bestX = newX;
                    bestY = newY;
                }
            }
        }
        if (spendEnergy(bestScore)) {
            x = bestX;
            y = bestY;
            return "The robot has successfully moved to position (" + bestX + ", " + bestY + ").";
        }
        return "ERROR: Not enough battery left. Cannot perform action";
    }
    public String rechargeBattery(int timeToCharge, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        chargeEndTime = currentTimestamp + timeToCharge;
        energy += timeToCharge;
        return "Robot battery is charging.";
    }
    public String scanObject(Cell currentCell, String color, String smell,
                             String sound, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        if (!spendEnergy(7)) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        if (color.equals("none") && smell.equals("none") && sound.equals("none")) {
            Water water = currentCell.getWater();
            Soil soil = currentCell.getSoil();
            if (water != null && !water.scanned) {
                water.scanned = true;
                water.setScanTimestamp(currentTimestamp);
                soil.setScanTimestamp(currentTimestamp);
                inventory.add(water.getName());
                return "The scanned object is water.";
            }
        }
        if (color.equals("pink") && smell.equals("sweet") && sound.equals("none")) {
            Plant plant = currentCell.getPlant();
            if (plant != null && !plant.scanned) {
                plant.scanned = true;
                inventory.add(plant.getName());
                return "The scanned object is a plant.";
            }
        }
        if (color.equals("brown") && smell.equals("earthy") && sound.equals("muu")) {
            Animal animal = currentCell.getAnimal();
            if (animal != null && !animal.scanned) {
                animal.scanned = true;
                animal.setLastMoveTimestamp(currentTimestamp);
                inventory.add(animal.getName());
                return "The scanned object is an animal.";
            }
        }
        return "ERROR: Object not found. Cannot perform action";
    }
    public String learnFact(String subject, String components, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        if (!spendEnergy(2)) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        if (inventory.contains(components))  {
            database.computeIfAbsent(components, k -> new ArrayList<>()).add(subject);
            return "The fact has been successfully saved in the database.";
        } else {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }
    }
    public String improveEnvironment(Cell currentCell, String improvementType,
                                     String componentName, int currentTimestamp) {
        if (isCharging(currentTimestamp)) {
            return "ERROR: Robot still charging. Cannot perform action";
        }
        if (!spendEnergy(10)) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        if (!inventory.contains(componentName)) {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }
        if (!database.containsKey(componentName)) {
            return "ERROR: Fact not yet saved. Cannot perform action";
        }
        List<String> facts = database.get(componentName);
        boolean foundMethod = false;
        for (int i = 0; i < facts.size(); i++) {
            if(facts.get(i).startsWith("Method to")) {
                foundMethod = true;
            }
        }
        if (!foundMethod) {
            return "ERROR: Fact not yet saved. Cannot perform action";
        }
        inventory.remove(componentName);
        switch (improvementType) {
            case "plantVegetation":
                currentCell.getAir().addOxygen(0.3);
                return "The " + componentName + " was planted successfully.";
            case "fertilizeSoil":
                currentCell.getSoil().addOrganicMatter(0.3);
                return "The soil was successfully fertilized using " + componentName + ".";
            case "increaseHumidity":
                currentCell.getAir().addHumidity(0.2);
                return "The humidity was successfully increased using " + componentName + ".";
            case "increaseMoisture":
                currentCell.getSoil().addWaterRetention(0.2);
                return "The moisture was successfully increased using " + componentName + ".";
        }
        return "Eroare, esti cam prea bos";
    }
}

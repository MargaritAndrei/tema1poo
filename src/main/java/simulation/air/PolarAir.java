package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public class PolarAir extends Air {
    protected double iceCrystalConcentration;
    public PolarAir(String name, double mass, double humidity,
                    double temperature, double oxygenLevel, double iceCrystalConcentration) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = Entity.round(iceCrystalConcentration);
    }
    @Override
    public boolean handleWeatherEvent(CommandInput cmd, int currentTimestamp) {
        if (cmd.getType().equals("polarStorm")) {
            weatherEffectValue = Entity.round(-cmd.getWindSpeed() * 0.2);
            weatherEffectEndTimestamp = currentTimestamp + 2;
            return true;
        }
        return false;
    }
    @Override
    public double airQualityScore(int currentTimestamp) {
        double score = (oxygenLevel * 2) + (100 - Math.abs(temperature)) - (iceCrystalConcentration * 0.05);
        double baseScore = Math.max(0, Math.min(100, score));
        if (currentTimestamp < weatherEffectEndTimestamp) {
            baseScore += weatherEffectValue;
        }
        double finalScore = Math.max(0, Math.min(100, baseScore));
        return Entity.round(finalScore);
    }
    @Override
    protected double maxScore() {
        return 142;
    }
    public double getIceCrystalConcentration() {
        return iceCrystalConcentration;
    }
    @Override
    public void addSpecificFieldsToJson(ObjectNode node) {
        node.put("iceCrystalConcentration", getIceCrystalConcentration());
    }
}
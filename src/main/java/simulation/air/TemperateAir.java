package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public class TemperateAir extends Air {
    protected double pollenLevel;
    public TemperateAir(String name, double mass, double humidity,
                        double temperature, double oxygenLevel, double pollenLevel) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.pollenLevel = pollenLevel;
    }
    public double getPollenLevel() {
        return pollenLevel;
    }
    @Override
    public boolean handleWeatherEvent(CommandInput cmd, int currentTimestamp) {
        if (cmd.getType().equals("newSeason")) {
            double penalty = cmd.getSeason().equalsIgnoreCase("Spring") ? 15.0 : 0.0;
            weatherEffectValue = Entity.round(-penalty);
            weatherEffectEndTimestamp = currentTimestamp + 2;
            return true;
        }
        return false;
    }
    @Override
    public double airQualityScore(int currentTimestamp) {
        double score = (oxygenLevel * 2) + (humidity * 0.7) - (pollenLevel * 0.1);

        if (currentTimestamp < weatherEffectEndTimestamp) {
            score += this.weatherEffectValue;
        }
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    protected double maxScore() {
        return 84;
    }
    @Override
    public void addSpecificFieldsToJson(ObjectNode node) {
        node.put("pollenLevel", getPollenLevel());
    }
}
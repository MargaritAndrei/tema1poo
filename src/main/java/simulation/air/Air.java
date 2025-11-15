package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public abstract class Air extends Entity {
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected double weatherEffectValue;
    protected int weatherEffectEndTimestamp;
    public Air(String name, double mass, double humidity,
               double temperature, double oxygenLevel) {
        super(name, mass);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
        this.weatherEffectValue = 0.0;
        this.weatherEffectEndTimestamp = 0;
    }
    public double getHumidity() {
        return humidity;
    }
    public double getTemperature() {
        return temperature;
    }
    public double getOxygenLevel() {
        return oxygenLevel;
    }
    public double calculateToxicity(int currentTimestamp) {
        double qualityScore = airQualityScore(currentTimestamp);
        double maxScore = maxScore();
        double normalizedQuality = Math.max(0, Math.min(100, qualityScore));
        double roundedQuality = Entity.round(normalizedQuality);
        double toxicityAQ = 100 * (1 - roundedQuality / maxScore);
        double normalizedToxicity = Math.max(0, Math.min(100, toxicityAQ));
        return Entity.round(normalizedToxicity);
    }
    public void addOxygen(double amount) {
        this.oxygenLevel += amount;
        this.oxygenLevel = Entity.round(this.oxygenLevel);
    }
    public void addHumidity(double amount) {
        this.humidity += amount;
        this.humidity = Entity.round(this.humidity);
    }
    public abstract boolean handleWeatherEvent(CommandInput cmd, int currentTimestamp);
    public abstract double airQualityScore(int currentTimestamp);
    protected abstract double maxScore();
    public double getMaxScore() {
        return maxScore();
    }
    public abstract void addSpecificFieldsToJson(ObjectNode node);
}
package simulation.air;

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
    public double calculateToxicity(int currentTimestamp) {
        double qualityScore = this.airQualityScore(currentTimestamp);
        double maxScore = this.maxScore();
        double normalizedQuality = Math.max(0, Math.min(100, qualityScore));
        double roundedQuality = Entity.round(normalizedQuality);
        double toxicityAQ = 100 * (1 - roundedQuality / maxScore);
        return Entity.round(toxicityAQ);
    }
    public void addOxygen(double amount) {
        this.oxygenLevel += amount;
        this.oxygenLevel = Entity.round(this.oxygenLevel);
    }
    public void addHumidity(double amount) {
        this.humidity += amount;
        this.humidity = Entity.round(this.humidity);
    }
    public abstract void applyWeatherChange(String weatherType, double value, int currentTimestamp);
    public abstract double airQualityScore(int currentTimestamp);
    protected abstract double maxScore();
    public double getmaxScore() {
        return maxScore();
    }
}
package simulation.air;

import simulation.Entity;

public class MountainAir extends Air {
    protected double altitude;
    public MountainAir (String name, double mass, double humidity,
                        double temperature, double oxygenLevel, double altitude) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.altitude = altitude;
    }
    @Override
    public void applyWeatherChange(String weatherType, double value, int currentTimestamp) {
        if ("peopleHiking".equalsIgnoreCase(weatherType)) {
            this.weatherEffectValue = Entity.round(-value * 0.1);
            this.weatherEffectEndTimestamp = currentTimestamp + 2;
        }
    }
    @Override
    public double airQualityScore(int currentTimestamp) {
        double oxygenFactor = oxygenLevel - (altitude / 1000.0 * 0.5);
        double score = (oxygenFactor * 2) + (humidity * 0.6);
        if (currentTimestamp < this.weatherEffectEndTimestamp) {
            score += this.weatherEffectValue;
        }
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    protected double maxScore() {
        return 78;
    }
}
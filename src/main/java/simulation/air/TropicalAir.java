package simulation.air;

import simulation.Entity;

public class TropicalAir extends Air {
    protected double co2Level;
    public TropicalAir(String name, double mass, double humidity,
                       double temperature, double oxygenLevel, double co2Level) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
    }
    @Override
    public void applyWeatherChange(String weatherType, double value, int currentTimestamp) {
        if ("rainfall".equalsIgnoreCase(weatherType)) {
            this.weatherEffectValue = Entity.round(value * 0.3);
            this.weatherEffectEndTimestamp = currentTimestamp + 2;
        }
    }
    @Override
    public double airQualityScore(int currentTimestamp) {
        double score = (oxygenLevel * 2) + (humidity * 0.5) - (co2Level * 0.01);
        if (currentTimestamp < this.weatherEffectEndTimestamp) {
            score += this.weatherEffectValue;
        }
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    protected double maxScore() {
        return 82;
    }

    public double getCo2Level() {
        return co2Level;
    }
}
package simulation.air;

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
    public void applyWeatherChange(String weatherType, double value, int currentTimestamp) {
        if ("newSeason".equalsIgnoreCase(weatherType)) {
            // Formula: normal_air_quality - seasonPenalty
            // Presupunem că 'value' este penalizarea (15 sau 0)
            this.weatherEffectValue = Entity.round(-value);
            this.weatherEffectEndTimestamp = currentTimestamp + 2;
        }
    }
    @Override
    public double airQualityScore(int currentTimestamp) {
        double score = (oxygenLevel * 2) + (humidity * 0.7) - (pollenLevel * 0.1);

        if (currentTimestamp < this.weatherEffectEndTimestamp) {
            score += this.weatherEffectValue;
        }
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    protected double maxScore() {
        return 84;
    }
}
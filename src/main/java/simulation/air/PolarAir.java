package simulation.air;

import simulation.Entity;

public class PolarAir extends Air {
    protected double iceCrystalConcentration;
    public PolarAir(String name, double mass, double humidity,
                    double temperature, double oxygenLevel, double iceCrystalConcentration) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = iceCrystalConcentration;
    }
    @Override
    public void applyWeatherChange(String weatherType, double value, int currentTimestamp) {
        if ("polarStorm".equalsIgnoreCase(weatherType)) {
            // Formula: normal_air_quality - (windSpeed * 0.2)
            this.weatherEffectValue = Entity.round(-value * 0.2);
            this.weatherEffectEndTimestamp = currentTimestamp + 2;
        }
    }
    @Override
    public double airQualityScore(int currentTimestamp) {
        double score = (oxygenLevel * 2) + (100 - Math.abs(temperature)) - (iceCrystalConcentration * 0.05);
        if (currentTimestamp < this.weatherEffectEndTimestamp) {
            score += this.weatherEffectValue;
        }
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    protected double maxScore() {
        return 142;
    }
}
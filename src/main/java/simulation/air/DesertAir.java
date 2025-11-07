package simulation.air;

import simulation.Entity;

public class DesertAir extends Air {
    protected double dustParticles;
    public DesertAir(String name, double mass, double humidity,
                     double temperature, double oxygenLevel, double dustParticles) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
    }
    @Override
    public void applyWeatherChange(String weatherType, double value, int currentTimestamp) {
        if ("desertStorm".equalsIgnoreCase(weatherType)) {
            // Formula: normal_air_quality - (desertStorm ? 30 : 0)
            // Presupunem că 'value' este 30 dacă e furtună, 0 altfel
            this.weatherEffectValue = Entity.round(-value);
            this.weatherEffectEndTimestamp = currentTimestamp + 2;
        }
    }
    @Override
    public double airQualityScore(int currentTimestamp) {
        double score = (oxygenLevel * 2) - (dustParticles * 0.2) - (temperature * 0.3);

        if (currentTimestamp < this.weatherEffectEndTimestamp) {
            score += this.weatherEffectValue;
        }

        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    protected double maxScore() {
        return 65;
    }
}
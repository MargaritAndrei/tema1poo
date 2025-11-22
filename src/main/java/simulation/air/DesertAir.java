package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public class DesertAir extends Air {
    protected double dustParticles;
    protected boolean desertStorm;

    public DesertAir(String name, double mass, double humidity,
                     double temperature, double oxygenLevel, double dustParticles) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.dustParticles = dustParticles;
        this.desertStorm = false;
    }

    public double getDustParticles() {
        return dustParticles;
    }

    public boolean isDesertStorm() {
        return desertStorm;
    }

    @Override
    public boolean handleWeatherEvent(CommandInput cmd, int currentTimestamp) {
        if (cmd.getType().equals("desertStorm")) {
            desertStorm = cmd.isDesertStorm();

            double penalty = -(desertStorm ? 30.0 : 0.0);
            weatherEffectValue = Entity.round(penalty);
            weatherEffectEndTimestamp = currentTimestamp + 2;
            return true;
        }
        return false;
    }

    @Override
    public double airQualityScore(int currentTimestamp) {
        if (currentTimestamp >= weatherEffectEndTimestamp) {
            desertStorm = false;
        }
        double score = (oxygenLevel * 2) - (dustParticles * 0.2) - (temperature * 0.3);
        double baseScore = Math.max(0, Math.min(100, score));
        if (currentTimestamp < weatherEffectEndTimestamp) {
            baseScore += weatherEffectValue;
        }
        double finalScore = Math.max(0, Math.min(100, baseScore));
        return Entity.round(finalScore);
    }

    @Override
    protected double maxScore() {
        return 65;
    }

    @Override
    public void addSpecificFieldsToJson(ObjectNode node) {
        node.put("desertStorm", desertStorm);
    }
}
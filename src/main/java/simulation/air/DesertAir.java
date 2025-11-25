package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public final class DesertAir extends Air {
    private static final double DESERT_STORM_PENALTY = 30.0;
    private static final double DUST_PARTICLES_WEIGHT = 0.2;
    private static final double TEMPERATURE_WEIGHT = 0.3;
    private static final double MAX_SCORE_LIMIT = 100.0;
    private static final double MAX_DESERT_AIR_SCORE = 65.0;

    private final double dustParticles;
    private boolean desertStorm;

    public DesertAir(final String name, final double mass, final double humidity,
                     final double temperature, final double oxygenLevel,
                     final double dustParticles) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.dustParticles = Entity.round(dustParticles);
        this.desertStorm = false;
    }

    /**
     * Retrieves the current dust particles level in the air.
     * @return The dust particles level.
     */
    public double getDustParticles() {
        return dustParticles;
    }

    /**
     * Checks if a desert storm weather event is currently active.
     * @return True if a desert storm is active, false otherwise.
     */
    public boolean isDesertStorm() {
        return desertStorm;
    }

    /**
     * Handles a weather event change.
     *
     * @param cmd              The command input containing weather details.
     * @param currentTimestamp The current timestamp of the simulation.
     * @return True if the weather changed successfully, false otherwise.
     */
    @Override
    public boolean handleWeatherEvent(final CommandInput cmd, final int currentTimestamp) {
        if (cmd.getType().equals("desertStorm")) {
            desertStorm = cmd.isDesertStorm();
            final double penalty = -(desertStorm ? DESERT_STORM_PENALTY : 0.0);
            weatherEffectValue = Entity.round(penalty);
            weatherEffectEndTimestamp = currentTimestamp + 2;
            return true;
        }
        return false;
    }
    /**
     * Calculates the air quality score.
     *
     * @param currentTimestamp The current timestamp of the simulation.
     * @return The calculated air quality score.
     */
    @Override
    public double airQualityScore(final int currentTimestamp) {
        if (currentTimestamp >= weatherEffectEndTimestamp) {
            desertStorm = false;
        }
        double score = (oxygenLevel * 2) - (dustParticles * DUST_PARTICLES_WEIGHT)
                - (temperature * TEMPERATURE_WEIGHT);
        double baseScore = Math.max(0, Math.min(MAX_SCORE_LIMIT, score));
        if (currentTimestamp < weatherEffectEndTimestamp) {
            baseScore += weatherEffectValue;
        }
        double finalScore = Math.max(0, Math.min(MAX_SCORE_LIMIT, baseScore));
        return Entity.round(finalScore);
    }
    /**
     * Returns the maximum specific score based on the air type.
     *
     * @return The maximum score value.
     */
    @Override
    protected double maxScore() {
        return MAX_DESERT_AIR_SCORE;
    }
    /**
     * Adds specific fields to the JSON output based on the air type.
     *
     * @param node The JSON ObjectNode to which fields will be added.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("desertStorm", desertStorm);
    }
}

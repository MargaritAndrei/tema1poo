package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public final class PolarAir extends Air {
    protected double iceCrystalConcentration;

    private static final double POLAR_STORM_WIND_MULTIPLIER = 0.2;
    private static final int POLAR_STORM_DURATION = 2;
    private static final double OXYGEN_WEIGHT = 2.0;
    private static final double ICE_CRYSTAL_WEIGHT = 0.05;
    private static final double MAX_AIR_SCORE = 100.0;
    private static final double MIN_AIR_SCORE = 0.0;
    private static final double BASE_MAX_SCORE = 142.0;

    public PolarAir(final String name, final double mass, final double humidity,
                    final double temperature, final double oxygenLevel,
                    final double iceCrystalConcentration) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.iceCrystalConcentration = Entity.round(iceCrystalConcentration);
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
        if (cmd.getType().equals("polarStorm")) {
            weatherEffectValue = Entity.round(-cmd.getWindSpeed() * POLAR_STORM_WIND_MULTIPLIER);
            weatherEffectEndTimestamp = currentTimestamp + POLAR_STORM_DURATION;
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
        double score = (oxygenLevel * OXYGEN_WEIGHT)
                + (MAX_AIR_SCORE - Math.abs(temperature))
                - (iceCrystalConcentration * ICE_CRYSTAL_WEIGHT);
        double baseScore = Math.max(MIN_AIR_SCORE, Math.min(MAX_AIR_SCORE, score));
        if (currentTimestamp < weatherEffectEndTimestamp) {
            baseScore += weatherEffectValue;
        }
        double finalScore = Math.max(MIN_AIR_SCORE, Math.min(MAX_AIR_SCORE, baseScore));
        return Entity.round(finalScore);
    }
    /**
     * Returns the maximum specific score based on the air type.
     *
     * @return The maximum score value.
     */
    @Override
    protected double maxScore() {
        return BASE_MAX_SCORE;
    }
    public double getIceCrystalConcentration() {
        return iceCrystalConcentration;
    }
    /**
     * Adds specific fields to the JSON output based on the air type.
     *
     * @param node The JSON ObjectNode to which fields will be added.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("iceCrystalConcentration", getIceCrystalConcentration());
    }
}

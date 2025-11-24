package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public final class MountainAir extends Air {
    private static final double HIKER_PENALTY_MULTIPLIER = 0.1;
    private static final double ALTITUDE_NORMALIZATION_FACTOR = 1000.0;
    private static final double ALTITUDE_OXYGEN_PENALTY = 0.5;
    private static final double HUMIDITY_WEIGHT = 0.6;
    private static final double MAX_SCORE_LIMIT = 100.0;
    private static final double MAX_MOUNTAIN_AIR_SCORE = 78.0;
    private final double altitude;

    public MountainAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel, final double altitude) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.altitude = Entity.round(altitude);
    }
    public double getAltitude() {
        return altitude;
    }

    /**
     * Returneaza true daca se schimba vremea, altfel false.
     */
    @Override
    public boolean handleWeatherEvent(final CommandInput cmd, final int currentTimestamp) {
        if (cmd.getType().equals("peopleHiking")) {
            // Folosind constanta
            this.weatherEffectValue = Entity.round(-cmd.getNumberOfHikers()
                    * HIKER_PENALTY_MULTIPLIER);
            this.weatherEffectEndTimestamp = currentTimestamp;
            return true;
        }
        return false;
    }
    /**
     * Calculeaza calitatea aerului.
     */
    @Override
    public double airQualityScore(final int currentTimestamp) {
        final double oxygenFactor = oxygenLevel - (altitude / ALTITUDE_NORMALIZATION_FACTOR
                * ALTITUDE_OXYGEN_PENALTY);
        double score = (oxygenFactor * 2) + (humidity * HUMIDITY_WEIGHT);
        double baseScore = Math.max(0, Math.min(MAX_SCORE_LIMIT, score));
        if (currentTimestamp < weatherEffectEndTimestamp) {
            baseScore += weatherEffectValue;
        }
        double finalScore = Math.max(0, Math.min(MAX_SCORE_LIMIT, baseScore));
        return Entity.round(finalScore);
    }
    /**
     * Returneaza maxScore.
     */
    @Override
    protected double maxScore() {
        return MAX_MOUNTAIN_AIR_SCORE;
    }
    /**
     * Adauga campurile specifice in output.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("altitude", getAltitude());
    }
}

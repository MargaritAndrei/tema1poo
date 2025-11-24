package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public final class TemperateAir extends Air {
    private static final double SPRING_SEASON_PENALTY = 15.0;
    private static final double HUMIDITY_WEIGHT = 0.7;
    private static final double POLLEN_WEIGHT = 0.1;
    private static final double MAX_SCORE_LIMIT = 100.0;
    private static final double MAX_TEMPERATE_AIR_SCORE = 84.0;
    private final double pollenLevel;

    public TemperateAir(final String name, final double mass, final double humidity,
                        final double temperature, final double oxygenLevel,
                        final double pollenLevel) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.pollenLevel = Entity.round(pollenLevel);
    }
    public double getPollenLevel() {
        return pollenLevel;
    }

    /**
     * Returneaza true daca se schimba vremea, altfel false.
     */
    @Override
    public boolean handleWeatherEvent(final CommandInput cmd, final int currentTimestamp) {
        if (cmd.getType().equals("newSeason")) {
            final double penalty = cmd.getSeason().equalsIgnoreCase("Spring")
                ? SPRING_SEASON_PENALTY : 0.0;
            weatherEffectValue = Entity.round(-penalty);
            weatherEffectEndTimestamp = currentTimestamp + 2;
            return true;
        }
        return false;
    }

    /**
     * Calculeaza calitatea aerului.
     */
    @Override
    public double airQualityScore(final int currentTimestamp) {
        double score = (oxygenLevel * 2) + (humidity * HUMIDITY_WEIGHT)
                - (pollenLevel * POLLEN_WEIGHT);
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
        return MAX_TEMPERATE_AIR_SCORE;
    }

    /**
     * Adauga campurile specifice in output.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("pollenLevel", getPollenLevel());
    }
}

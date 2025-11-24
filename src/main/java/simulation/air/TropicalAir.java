package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public final class TropicalAir extends Air {
    private static final double RAINFALL_EFFECT_MULTIPLIER = 0.3;
    private static final double HUMIDITY_WEIGHT = 0.5;
    private static final double CO2_WEIGHT = 0.01;
    private static final double MAX_SCORE_LIMIT = 100.0;
    private static final double MAX_TROPICAL_AIR_SCORE = 82.0;

    private final double co2Level;

    public TropicalAir(final String name, final double mass, final double humidity,
                       final double temperature, final double oxygenLevel, final double co2Level) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.co2Level = Entity.round(co2Level);
    }
    public double getCo2Level() {
        return co2Level;
    }
    /**
     * Returneaza true daca se schimba vremea, altfel false.
     */
    @Override
    public boolean handleWeatherEvent(final CommandInput cmd, final int currentTimestamp) {
        if (cmd.getType().equals("rainfall")) {
            // Folosind constanta
            weatherEffectValue = Entity.round(cmd.getRainfall() * RAINFALL_EFFECT_MULTIPLIER);
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
        double score = (oxygenLevel * 2) + (humidity * HUMIDITY_WEIGHT) - (co2Level * CO2_WEIGHT);
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
        return MAX_TROPICAL_AIR_SCORE;
    }
    /**
     * Adauga campurile specifice in output.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("co2Level", co2Level);
    }
}

package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public abstract class Air extends Entity {
    private static final double MAX_PERCENTAGE = 100.0;

    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected double weatherEffectValue;
    protected int weatherEffectEndTimestamp;

    public Air(final String name, final double mass, final double humidity,
               final double temperature, final double oxygenLevel) {
        super(name, mass);
        this.humidity = Entity.round(humidity);
        this.temperature = Entity.round(temperature);
        this.oxygenLevel = oxygenLevel;
        this.weatherEffectValue = 0.0;
        this.weatherEffectEndTimestamp = 0;
    }

    public final double getHumidity() {
        return humidity;
    }
    public final double getTemperature() {
        return temperature;
    }
    public final double getOxygenLevel() {
        return oxygenLevel;
    }

    /**
     * Calculates the toxicity of the air using the given formula.
     *
     * @param currentTimestamp The current timestamp of the simulation.
     * @return The calculated toxicity value, rounded and normalized.
     */
    public double calculateToxicity(final int currentTimestamp) {
        final double qualityScore = airQualityScore(currentTimestamp);
        final double maxScore = maxScore();
        final double normalizedQuality = Math.max(0, Math.min(MAX_PERCENTAGE, qualityScore));
        final double roundedQuality = Entity.round(normalizedQuality);
        final double toxicityAQ = MAX_PERCENTAGE * (1 - roundedQuality / maxScore);
        final double normalizedToxicity = Math.max(0, Math.min(MAX_PERCENTAGE, toxicityAQ));
        return Entity.round(normalizedToxicity);
    }

    /**
     * Adds oxygen to the air.
     *
     * @param amount The amount of oxygen to add.
     */
    public final void addOxygen(final double amount) {
        oxygenLevel += amount;
        oxygenLevel = Entity.round(oxygenLevel);
    }

    /**
     * Adds humidity to the air.
     *
     * @param amount The amount of humidity to add.
     */
    public final void addHumidity(final double amount) {
        humidity += amount;
        humidity = Entity.round(humidity);
    }

    /**
     * Handles a weather event change.
     *
     * @param cmd              The command input containing weather details.
     * @param currentTimestamp The current timestamp of the simulation.
     * @return True if the weather changed successfully, false otherwise.
     */
    public abstract boolean handleWeatherEvent(CommandInput cmd, int currentTimestamp);

    /**
     * Calculates the air quality score.
     *
     * @param currentTimestamp The current timestamp of the simulation.
     * @return The calculated air quality score.
     */
    public abstract double airQualityScore(int currentTimestamp);

    /**
     * Returns the maximum specific score based on the air type.
     *
     * @return The maximum score value.
     */
    protected abstract double maxScore();
    public final double getMaxScore() {
        return maxScore();
    }
    /**
     * Adds specific fields to the JSON output based on the air type.
     *
     * @param node The JSON ObjectNode to which fields will be added.
     */
    public abstract void addSpecificFieldsToJson(ObjectNode node);
}

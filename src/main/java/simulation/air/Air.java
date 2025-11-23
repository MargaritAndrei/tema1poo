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
     * Calculeaza toxicitatea aerului cu formula data.
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
     * Adauga oxigen la aer.
     */
    public final void addOxygen(final double amount) {
        oxygenLevel += amount;
        oxygenLevel = Entity.round(oxygenLevel);
    }

    /**
     * Adauga umiditate la aer.
     */
    public final void addHumidity(final double amount) {
        humidity += amount;
        humidity = Entity.round(humidity);
    }

    /**
     * Produce schimbarea vremii, returneaza true sau false daca vremea s-a schimbat.
     */
    public abstract boolean handleWeatherEvent(CommandInput cmd, int currentTimestamp);

    /**
     * Calculeaza calitatea aerului.
     */
    public abstract double airQualityScore(int currentTimestamp);

    /**
     * Returneaza scorul specific maxim in functie de tipul de aer.
     */
    protected abstract double maxScore();
    public final double getMaxScore() {
        return maxScore();
    }
    /**
     * Adauga campurile specifice la output in functie de tipul aerului.
     */
    public abstract void addSpecificFieldsToJson(ObjectNode node);
}

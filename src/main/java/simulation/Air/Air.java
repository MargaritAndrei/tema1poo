package simulation.Air;

import simulation.Entity;

public abstract class Air extends Entity {
    public Air(String name, double mass, double humidity,
               double temperature, double oxygenLevel) {
        super(name, mass);
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
    }
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    public double calculateToxicity() {
        double toxicityAQ = 100 * (1 - airQualityScore() / maxScore());
        return Entity.round(toxicityAQ);
    }
    public void addOxygen(double amount) {
        this.oxygenLevel += amount;
        this.oxygenLevel = Entity.round(this.oxygenLevel);
    }
    public void addHumidity(double amount) {
        this.humidity += amount;
        this.humidity = Entity.round(this.humidity);
    }
    public abstract void applyWeatherChange(String weatherType, double value);
    public abstract double airQualityScore();
    protected abstract double maxScore();
    public double getmaxScore() {
        return maxScore();
    }
}
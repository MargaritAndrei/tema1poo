package simulation.air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import simulation.Entity;

public class TropicalAir extends Air {
    protected double co2Level;
    public TropicalAir(String name, double mass, double humidity,
                       double temperature, double oxygenLevel, double co2Level) {
        super(name, mass, humidity, temperature, oxygenLevel);
        this.co2Level = co2Level;
    }
    public double getCo2Level() {
        return co2Level;
    }
    @Override
    public boolean handleWeatherEvent(CommandInput cmd, int currentTimestamp) {
        if (cmd.getType().equals("rainfall")) {
            weatherEffectValue = Entity.round(cmd.getRainfall() * 0.3);
            weatherEffectEndTimestamp = currentTimestamp + 2;
            return true;
        }
        return false;
    }
    @Override
    public double airQualityScore(int currentTimestamp) {
        double score = (oxygenLevel * 2) + (humidity * 0.5) - (co2Level * 0.01);
        if (currentTimestamp < weatherEffectEndTimestamp) {
            score += weatherEffectValue;
        }
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    protected double maxScore() {
        return 82;
    }

    @Override
    public void addSpecificFieldsToJson(ObjectNode node) {
        node.put("co2Level", getCo2Level());
    }
}
package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;

public class DesertSoil extends Soil {
    public DesertSoil(String name, double mass, double nitrogen, double waterRetention,
                      double soilpH, double organicMatter, double salinity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.salinity = Entity.round(salinity);
    }
    protected double salinity;
    public double getSalinity() {
        return salinity;
    }
    @Override
    public double calculateQuality() {
        double score = nitrogen * 0.5 + waterRetention * 0.3 - salinity * 2;
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    public double calculateBlockProbability() {
        double score = (100 - waterRetention + salinity) / 100 * 100;
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    public void addSpecificFieldsToJson(ObjectNode node) {
        node.put("salinity", getSalinity());
    }
}
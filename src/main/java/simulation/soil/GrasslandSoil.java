package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;

public class GrasslandSoil extends Soil {
    protected double rootDensity;
    public GrasslandSoil(String name, double mass, double nitrogen, double waterRetention,
                         double soilpH, double organicMatter, double rootDensity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.rootDensity = rootDensity;
    }
    public double getRootDensity() {
        return rootDensity;
    }
    @Override
    public double calculateQuality() {
        double score = (nitrogen * 1.3) + (organicMatter * 1.5) + (rootDensity * 0.8);
        score = Math.max(0, Math.min(100, score));
        return Entity.round(score);
    }
    @Override
    public double calculateBlockProbability() {
        double score = ((50 - rootDensity) + waterRetention * 0.5) / 75.0 * 100;
        return Entity.round(score);
    }
    @Override
    public void addSpecificFieldsToJson(ObjectNode node) {
        node.put("rootDensity", getRootDensity());
    }
}
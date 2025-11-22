package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;

public class TundraSoil extends Soil {
    protected double permafrostDepth;
    public TundraSoil(String name, double mass, double nitrogen, double waterRetention,
                      double soilpH, double organicMatter, double permafrostDepth) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.permafrostDepth = Entity.round(permafrostDepth);
    }
    public double getPermafrostDepth() {
        return permafrostDepth;
    }
    @Override
    public double calculateQuality() {
        double score = (nitrogen * 0.7) + (organicMatter * 0.5) - (permafrostDepth * 1.5);
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    public double calculateBlockProbability() {
        double score = (50 - permafrostDepth) / 50 * 100;
        double normalizeScore = Math.max(0, Math.min(100, score));
        return Entity.round(normalizeScore);
    }
    @Override
    public void addSpecificFieldsToJson(ObjectNode node) {
        node.put("permafrostDepth", getPermafrostDepth());
    }
}
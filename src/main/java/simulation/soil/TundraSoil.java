package simulation.soil;

import simulation.Entity;

public class TundraSoil extends Soil {
    public TundraSoil(String name, double mass, double nitrogen, double waterRetention,
                      double soilpH, double organicMatter, double permafrostDepth) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.permafrostDepth = permafrostDepth;
    }
    protected double permafrostDepth;
    @Override
    public double calculateQuality() {
        double score = (nitrogen * 0.7) + (organicMatter * 0.5) - (permafrostDepth * 1.5);
        score = Math.max(0, Math.min(100, score));
        return Entity.round(score);
    }
    @Override
    public double calculateBlockProbability() {
        double score = (50 - permafrostDepth) / 50.0 * 100;
        return Entity.round(score);
    }
    public double getPermafrostDepth() {
        return permafrostDepth;
    }
}
package simulation.soil;

import simulation.Entity;

public class ForestSoil extends Soil {
    public ForestSoil(String name, double mass, double nitrogen, double waterRetention,
                      double soilpH, double organicMatter, double leafLitter) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.leafLitter = leafLitter;
    }
    protected double leafLitter;
    @Override
    public double calculateQuality() {
        double score = nitrogen * 1.2 + organicMatter * 2 + waterRetention * 1.5
                + leafLitter * 0.3;
        return Math.max(0, Math.min(100, score));
    }
    @Override
    public double calculateBlockProbability() {
        double score = (waterRetention * 0.6 + leafLitter * 0.4) / 80.0 * 100;
        return Entity.round(score);
    }
    public double getLeafLitter() {
        return leafLitter;
    }
}
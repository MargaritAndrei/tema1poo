package simulation.soil;

import simulation.Entity;

public class GrasslandSoil extends Soil {
    public GrasslandSoil(String name, double mass, double nitrogen, double waterRetention,
                         double soilpH, double organicMatter, double rootDensity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.rootDensity = rootDensity;
    }
    protected double rootDensity;
    @Override
    public double calculateQuality() {
        double score = (nitrogen * 1.3) + (organicMatter * 1.5) + (rootDensity * 0.8);
        return Math.max(0, Math.min(100, score));
    }
    @Override
    public double calculateBlockProbability() {
        double score = ((50 - rootDensity) + waterRetention * 0.5) / 75.0 * 100;
        return Entity.round(score);
    }
    public double getRootDensity() {
        return rootDensity;
    }
}
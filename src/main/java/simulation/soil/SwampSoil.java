package simulation.soil;

import simulation.Entity;

public class SwampSoil extends Soil {
    public SwampSoil(String name, double mass, double nitrogen, double waterRetention,
                     double soilpH, double organicMatter, double waterLogging) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.waterLogging = waterLogging;
    }
    protected double waterLogging;
    @Override
    public double calculateQuality() {
        double score = nitrogen * 1.1 + organicMatter * 2.2 - waterLogging * 5;
        return Math.max(0, Math.min(100, score));
    }
    @Override
    public double calculateBlockProbability() {
        double score = waterLogging * 10;
        return Entity.round(score);
    }
}
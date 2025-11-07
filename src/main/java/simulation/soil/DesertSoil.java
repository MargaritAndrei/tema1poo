package simulation.soil;

import simulation.Entity;

public class DesertSoil extends Soil {
    public DesertSoil(String name, double mass, double nitrogen, double waterRetention,
                      double soilpH, double organicMatter, double salinity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.salinity = salinity;
    }
    protected double salinity;
    @Override
    public double calculateQuality() {
        double score = nitrogen * 0.5 + waterRetention * 0.3 - salinity * 2;
        return Math.max(0, Math.min(100, score));
    }
    @Override
    public double calculateBlockProbability() {
        double score = (100 - waterRetention + salinity) / 100.0 * 100;
        return Entity.round(score);
    }
}
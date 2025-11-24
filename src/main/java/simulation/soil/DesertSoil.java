package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;

public final class DesertSoil extends Soil {
    private static final double NITROGEN_QUALITY_WEIGHT = 0.5;
    private static final double WATER_RETENTION_QUALITY_WEIGHT = 0.3;
    private static final double SALINITY_QUALITY_PENALTY = 2.0;
    private static final double MAX_PERCENTAGE_VALUE = 100.0;

    private static final double MAX_WATER_RETENTION_BASE = 100.0;
    private static final double BLOCK_PROBABILITY_DIVISOR = 100.0;
    private static final double BLOCK_PROBABILITY_MULTIPLIER = 100.0;

    private final double salinity;

    public DesertSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilpH,
                      final double organicMatter, final double salinity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.salinity = Entity.round(salinity);
    }

    public double getSalinity() {
        return salinity;
    }

    /**
     * Calculeaza calitatea solului.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_QUALITY_WEIGHT)
                + (waterRetention * WATER_RETENTION_QUALITY_WEIGHT)
                - (salinity * SALINITY_QUALITY_PENALTY);

        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Calculeaza blockProbability.
     */
    @Override
    public double calculateBlockProbability() {
        double score = (MAX_WATER_RETENTION_BASE - waterRetention + salinity)
                / BLOCK_PROBABILITY_DIVISOR * BLOCK_PROBABILITY_MULTIPLIER;

        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Adauga campurile specifice in json.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("salinity", getSalinity());
    }
}

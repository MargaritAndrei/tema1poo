package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;

public final class GrasslandSoil extends Soil {
    private static final double NITROGEN_QUALITY_WEIGHT = 1.3;
    private static final double ORGANIC_MATTER_QUALITY_WEIGHT = 1.5;
    private static final double ROOT_DENSITY_QUALITY_WEIGHT = 0.8;
    private static final double MAX_PERCENTAGE_VALUE = 100.0;

    private static final double ROOT_DENSITY_BASE_BLOCK = 50.0;
    private static final double WATER_RETENTION_BLOCK_WEIGHT = 0.5;
    private static final double BLOCK_PROBABILITY_DIVISOR = 75.0;
    private static final double BLOCK_PROBABILITY_MULTIPLIER = 100.0;

    private final double rootDensity;

    public GrasslandSoil(final String name, final double mass, final double nitrogen,
                         final double waterRetention, final double soilpH,
                         final double organicMatter, final double rootDensity) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.rootDensity = Entity.round(rootDensity);
    }

    public double getRootDensity() {
        return rootDensity;
    }

    /**
     * Calculeaza calitatea solului.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_QUALITY_WEIGHT)
                + (organicMatter * ORGANIC_MATTER_QUALITY_WEIGHT)
                + (rootDensity * ROOT_DENSITY_QUALITY_WEIGHT);

        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Calculeaza blockProbability.
     */
    @Override
    public double calculateBlockProbability() {
        double score = ((ROOT_DENSITY_BASE_BLOCK - rootDensity)
                + waterRetention * WATER_RETENTION_BLOCK_WEIGHT)
                / BLOCK_PROBABILITY_DIVISOR * BLOCK_PROBABILITY_MULTIPLIER;

        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Adauga campurile specifice tipului de sol in output-ul json.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("rootDensity", getRootDensity());
    }
}

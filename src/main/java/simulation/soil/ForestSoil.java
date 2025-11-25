package simulation.soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import simulation.Entity;

public final class ForestSoil extends Soil {
    private static final double NITROGEN_QUALITY_WEIGHT = 1.2;
    private static final double ORGANIC_MATTER_QUALITY_WEIGHT = 2.0; // Valoare din codul original
    private static final double WATER_RETENTION_QUALITY_WEIGHT = 1.5;
    private static final double LEAF_LITTER_QUALITY_WEIGHT = 0.3;
    private static final double MAX_PERCENTAGE_VALUE = 100.0;

    private static final double WATER_RETENTION_BLOCK_WEIGHT = 0.6;
    private static final double LEAF_LITTER_BLOCK_WEIGHT = 0.4;
    private static final double BLOCK_PROBABILITY_DIVISOR = 80.0;
    private static final double BLOCK_PROBABILITY_MULTIPLIER = 100.0;

    private final double leafLitter;

    public ForestSoil(final String name, final double mass, final double nitrogen,
                      final double waterRetention, final double soilpH,
                      final double organicMatter, final double leafLitter) {
        super(name, mass, nitrogen, waterRetention, soilpH, organicMatter);
        this.leafLitter = Entity.round(leafLitter);
    }

    public double getLeafLitter() {
        return leafLitter;
    }

    /**
     * Calculates the soil quality.
     *
     * @return The calculated soil quality score.
     */
    @Override
    public double calculateQuality() {
        double score = (nitrogen * NITROGEN_QUALITY_WEIGHT) + (organicMatter
                * ORGANIC_MATTER_QUALITY_WEIGHT) + (waterRetention * WATER_RETENTION_QUALITY_WEIGHT)
                + (leafLitter * LEAF_LITTER_QUALITY_WEIGHT);

        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Calculates the probability of blocking the Terrabot.
     *
     * @return The calculated block probability.
     */
    @Override
    public double calculateBlockProbability() {
        double score = (waterRetention * WATER_RETENTION_BLOCK_WEIGHT
                + leafLitter * LEAF_LITTER_BLOCK_WEIGHT)
                / BLOCK_PROBABILITY_DIVISOR * BLOCK_PROBABILITY_MULTIPLIER;
        final double normalizeScore = Math.max(0, Math.min(MAX_PERCENTAGE_VALUE, score));
        return Entity.round(normalizeScore);
    }

    /**
     * Adds specific fields to the JSON output based on the soil type.
     *
     * @param node The JSON ObjectNode to which fields will be added.
     */
    @Override
    public void addSpecificFieldsToJson(final ObjectNode node) {
        node.put("leafLitter", getLeafLitter());
    }
}

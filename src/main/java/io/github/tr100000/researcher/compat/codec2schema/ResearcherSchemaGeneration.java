package io.github.tr100000.researcher.compat.codec2schema;

import io.github.tr100000.codec2schema.api.SchemaExporter;
import io.github.tr100000.codec2schema.api.SchemaGenerationEntrypoint;
import io.github.tr100000.researcher.LockableRecipeTypesList;
import io.github.tr100000.researcher.Research;

public class ResearcherSchemaGeneration implements SchemaGenerationEntrypoint {
    @Override
    public void generate(SchemaExporter exporter) {
        exporter.accept(Research.CODEC, "data", "research.json");
        exporter.accept(LockableRecipeTypesList.CODEC, "data", "lockable_recipe_types.json");
    }
}

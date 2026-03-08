package io.github.tr100000.researcher.compat.codec2schema;

import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.SchemaExporter;
import io.github.tr100000.researcher.LockableRecipeTypesList;
import io.github.tr100000.researcher.Research;

public class ResearcherSchemaPlugin implements Codec2SchemaPlugin {
    @Override
    public void generateSchemas(SchemaExporter exporter) {
        exporter.accept(Research.CODEC, "data", "research.json");
        exporter.accept(LockableRecipeTypesList.CODEC, "data", "lockable_recipe_types.json");
    }
}

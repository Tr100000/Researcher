package io.github.tr100000.researcher.api.data;

import io.github.tr100000.researcher.Research;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ResearchExporter extends BiConsumer<Identifier, Research> { }

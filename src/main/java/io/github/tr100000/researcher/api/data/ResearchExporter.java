package io.github.tr100000.researcher.api.data;

import io.github.tr100000.researcher.Research;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ResearchExporter extends BiConsumer<Identifier, Research> { }

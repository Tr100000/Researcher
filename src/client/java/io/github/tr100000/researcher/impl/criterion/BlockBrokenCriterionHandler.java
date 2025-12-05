package io.github.tr100000.researcher.impl.criterion;

import io.github.tr100000.researcher.ResearchCriterion;
import io.github.tr100000.researcher.criteria.BlockBrokenCriterion;
import net.minecraft.item.Item;

public class BlockBrokenCriterionHandler extends AbstractItemCriterionHandler<BlockBrokenCriterion.Conditions> {
    public BlockBrokenCriterionHandler() {
        super(
                "screen.researcher.criterion.block_broken.before",
                "screen.researcher.criterion.block_broken.after"
        );
    }

    @Override
    public Item getItem(ResearchCriterion<BlockBrokenCriterion.Conditions> criterion) {
        return criterion.conditions().block().asItem();
    }
}

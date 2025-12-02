package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.text.Text;

public final class DamageSourcePredicateHelper {
    private DamageSourcePredicateHelper() {}

    private static final Text DAMAGE_DIRECT_HEADER = ModUtils.getScreenTranslated("predicate.damage.direct_entity_header");
    private static final Text DAMAGE_SOURCE_HEADER = ModUtils.getScreenTranslated("predicate.damage.source_entity_header");
    private static final Text DAMAGE_DIRECT = ModUtils.getScreenTranslated("predicate.damage.must_be_direct");
    private static final Text DAMAGE_INDIRECT = ModUtils.getScreenTranslated("predicate.damage.must_be_indirect");

    public static void tooltip(DamageSourcePredicate predicate, IndentedTextHolder textHolder) {
        predicate.isDirect().ifPresent(mustBeDirect -> textHolder.accept(mustBeDirect ? DAMAGE_DIRECT : DAMAGE_INDIRECT));

        predicate.tags().forEach(tagPredicate -> PredicateHelper.tagTooltip(tagPredicate, textHolder));

        predicate.directEntity().ifPresent(directEntityPredicate -> {
            textHolder.accept(DAMAGE_DIRECT_HEADER);
            textHolder.push(2);
            EntityPredicateHelper.tooltip(directEntityPredicate, textHolder);
            textHolder.push(-2);
        });

        predicate.sourceEntity().ifPresent(sourceEntityPredicate -> {
            textHolder.accept(DAMAGE_SOURCE_HEADER);
            textHolder.push(2);
            EntityPredicateHelper.tooltip(sourceEntityPredicate, textHolder);
            textHolder.push(-2);
        });
    }
}

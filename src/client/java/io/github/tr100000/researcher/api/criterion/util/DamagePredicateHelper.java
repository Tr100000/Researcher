package io.github.tr100000.researcher.api.criterion.util;

import io.github.tr100000.researcher.ModUtils;
import io.github.tr100000.researcher.api.util.IndentedTextHolder;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.text.Text;

public final class DamagePredicateHelper {
    private DamagePredicateHelper() {}

    private static final Text DAMAGE_DEALT = ModUtils.getScreenTranslated("predicate.damage.dealt");
    private static final Text DAMAGE_TAKEN = ModUtils.getScreenTranslated("predicate.damage.taken");
    private static final Text DAMAGE_BLOCKED = ModUtils.getScreenTranslated("predicate.damage.blocked");
    private static final Text DAMAGE_NOT_BLOCKED = ModUtils.getScreenTranslated("predicate.damage.not_blocked");
    private static final Text DAMAGE_DIRECT_HEADER = ModUtils.getScreenTranslated("predicate.damage.direct_entity_header");
    private static final Text DAMAGE_SOURCE_HEADER = ModUtils.getScreenTranslated("predicate.damage.source_entity_header");
    private static final Text DAMAGE_DIRECT = ModUtils.getScreenTranslated("predicate.damage.must_be_direct");
    private static final Text DAMAGE_INDIRECT = ModUtils.getScreenTranslated("predicate.damage.must_be_indirect");

    public static void tooltip(DamagePredicate predicate, IndentedTextHolder textHolder) {
        NumberRangeUtils.tooltip(predicate.dealt(), DAMAGE_DEALT, textHolder);
        NumberRangeUtils.tooltip(predicate.taken(), DAMAGE_TAKEN, textHolder);
        PredicateHelper.optionalBooleanTooltip(predicate.blocked(), DAMAGE_BLOCKED, DAMAGE_NOT_BLOCKED, textHolder);
        predicate.type().ifPresent(t -> tooltip(t, textHolder));
        predicate.sourceEntity().ifPresent(sourceEntityPredicate -> {
            textHolder.accept(DAMAGE_SOURCE_HEADER);
            textHolder.push();
            EntityPredicateHelper.tooltip(sourceEntityPredicate, textHolder);
            textHolder.pop();
        });
    }

    public static void tooltip(DamageSourcePredicate predicate, IndentedTextHolder textHolder) {
        predicate.isDirect().ifPresent(mustBeDirect -> textHolder.accept(mustBeDirect ? DAMAGE_DIRECT : DAMAGE_INDIRECT));

        predicate.tags().forEach(tagPredicate -> PredicateHelper.tagTooltip(tagPredicate, textHolder));

        predicate.directEntity().ifPresent(directEntityPredicate -> {
            textHolder.accept(DAMAGE_DIRECT_HEADER);
            textHolder.push();
            EntityPredicateHelper.tooltip(directEntityPredicate, textHolder);
            textHolder.pop();
        });

        predicate.sourceEntity().ifPresent(sourceEntityPredicate -> {
            textHolder.accept(DAMAGE_SOURCE_HEADER);
            textHolder.push();
            EntityPredicateHelper.tooltip(sourceEntityPredicate, textHolder);
            textHolder.pop();
        });
    }
}

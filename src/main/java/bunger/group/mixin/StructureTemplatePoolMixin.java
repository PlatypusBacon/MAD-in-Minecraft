package bunger.group.mixin;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(StructureTemplatePool.class)
public class StructureTemplatePoolMixin {

    @Mutable @Final @Shadow
    private List<Pair<StructurePoolElement, Integer>> rawTemplates;

    @Mutable @Final @Shadow
    private ObjectArrayList<StructurePoolElement> templates;

    @Inject(
            method = "<init>(Lnet/minecraft/core/Holder;Ljava/util/List;)V",
            at = @At("TAIL")
    )
    private void onInit(
            Holder<StructureTemplatePool> fallback,
            List<Pair<StructurePoolElement, Integer>> templates,
            CallbackInfo ci
    ) {
        Holder<StructureTemplatePool> fb = fallback;
        // Check if this pool is a village houses pool
        // fallback is typically "minecraft:village/plains/terminators" etc.
        // but we can't check the ID here — so we filter and only act if something matches
        String fallbackKey = fb.unwrapKey().map(k -> k.identifier().toString()).orElse("");        if (!fallbackKey.startsWith("minecraft:village/")) {
            return;
        }

        this.rawTemplates = new ArrayList<>(this.rawTemplates);
        this.rawTemplates.removeIf(pair ->
                pair.getFirst() instanceof SinglePoolElement spe &&
                        spe.toString().contains("crafting_table")
        );

        this.templates = new ObjectArrayList<>(this.templates);
        this.templates.removeIf(element ->
                element instanceof SinglePoolElement spe &&
                        spe.toString().contains("crafting_table")
        );
    }
}
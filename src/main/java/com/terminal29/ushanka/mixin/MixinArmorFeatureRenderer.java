package com.terminal29.ushanka.mixin;

import com.sun.istack.internal.Nullable;
import com.terminal29.ushanka.ModInfo;
import com.terminal29.ushanka.item.ItemUshanka;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorFeatureRenderer.class)
public class MixinArmorFeatureRenderer {
    @Inject(method = "method_4174", at = @At(value="HEAD"), cancellable = true)
    private Identifier method_4174(ArmorItem armorItem_1, boolean boolean_1, @Nullable String string_1, CallbackInfoReturnable<Identifier> cir) {
        if (armorItem_1 instanceof ItemUshanka) {
            String textureLoc = "textures/models/armor/" + armorItem_1.getMaterial().getName() + "_layer_" + (boolean_1 ? 2 : 1) + (string_1 == null ? "" : "_" + string_1) + ".png";
            cir.setReturnValue(new Identifier(ModInfo.DISPLAY_NAME, textureLoc));
        }
        return null;
    }
}

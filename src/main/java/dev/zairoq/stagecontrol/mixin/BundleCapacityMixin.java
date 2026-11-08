package dev.zairoq.stagecontrol.mixin;

import org.apache.commons.lang3.math.Fraction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.item.component.BundleContents$Mutable")
public abstract class BundleCapacityMixin {
    private static final Fraction STAGE_CONTROL$BUNDLE_CAPACITY =
            Fraction.getFraction(3, 1);

    @Redirect(
            method = "*",
            at = @At(
                    value = "FIELD",
                    target = "Lorg/apache/commons/lang3/math/Fraction;ONE:Lorg/apache/commons/lang3/math/Fraction;",
                    opcode = Opcodes.GETSTATIC
            ),
            require = 1
    )
    private static Fraction stageControl$increaseBundleCapacity() {
        return STAGE_CONTROL$BUNDLE_CAPACITY;
    }
}

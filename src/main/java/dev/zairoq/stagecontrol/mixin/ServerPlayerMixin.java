package dev.zairoq.stagecontrol.mixin;

import dev.zairoq.stagecontrol.StageControl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTarget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "teleport", at = @At("HEAD"), cancellable = true)
    private void stageControl$blockLockedDimensions(TeleportTarget target,
                                                     CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (target.newLevel().dimension().equals(player.level().dimension())) {
            return;
        }
        if (StageControl.isLocked(target.newLevel().dimension())) {
            player.displayClientMessage(Component.literal("tengan paciencia :)"), false);
            cir.setReturnValue(false);
        }
    }
}

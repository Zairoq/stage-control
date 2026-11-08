package dev.zairoq.stagecontrol.mixin;

import dev.zairoq.stagecontrol.StageControl;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "teleport", at = @At("HEAD"), cancellable = true)
    private void stageControl$blockLockedDimensions(TeleportTransition transition,
                                                     CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (transition.newLevel().dimension().equals(player.level().dimension())) {
            return;
        }
        if (StageControl.isLocked(transition.newLevel().dimension())) {
            player.sendSystemMessage(Component.literal("Dimension Bloqueada"), true);
            cir.setReturnValue(player);
        }
    }
}

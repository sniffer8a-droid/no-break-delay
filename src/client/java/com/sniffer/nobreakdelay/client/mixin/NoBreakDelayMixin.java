package com.sniffer.nobreakdelay.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class NoBreakDelayMixin {
	@Shadow
	private int destroyDelay;

	@Inject(method = {"startDestroyBlock", "continueDestroyBlock"}, at = @At("RETURN"))
	private void nobreakdelay$removeDelay(CallbackInfoReturnable<Boolean> cir) {
		Player player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}

		MobEffectInstance haste = player.getEffect(MobEffects.HASTE);
		if (haste == null || haste.getAmplifier() < 1) {
			return;
		}

		ItemStack tool = player.getMainHandItem();
		int efficiency = EnchantmentHelper.getItemEnchantmentLevel(
			player.level().registryAccess()
				.lookupOrThrow(Registries.ENCHANTMENT)
				.getOrThrow(Enchantments.EFFICIENCY),
			tool
		);

		if (efficiency >= 5) {
			this.destroyDelay = 0;
		}
	}
}

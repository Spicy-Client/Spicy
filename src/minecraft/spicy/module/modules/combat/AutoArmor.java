package spicy.module.modules.combat;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import spicy.events.game.GameTickEvent;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;

public class AutoArmor extends Module
{
    
    public AutoArmor() {
        super("AutoArmor", 0, Category.COMBAT);
    }


    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }

    @SubscribeEvent
    private void onTick(final GameTickEvent event) {
        if (Wrapper.player() != null && (Wrapper.mc().currentScreen == null || Wrapper.mc().currentScreen instanceof GuiInventory || !Wrapper.mc().currentScreen.getClass().getName().contains("inventory"))) {
            int slotID = -1;
            double maxProt = -1.0;
            for (int i = 9; i < 45; ++i) {
                final ItemStack stack = Wrapper.player().inventoryContainer.getSlot(i).getStack();
                if (stack != null) {
                    if (this.canEquip(stack)) {
                        final double protValue = this.getProtectionValue(stack);
                        if (protValue >= maxProt) {
                            slotID = i;
                            maxProt = protValue;
                        }
                    }
                }
            }
            if (slotID != -1) {
                Wrapper.playerController().windowClick(Wrapper.player().inventoryContainer.windowId, slotID, 0, 1, Wrapper.player());
            }
        }
    }
    
    private boolean canEquip(final ItemStack stack) {
        return (Wrapper.player().getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots")) || (Wrapper.player().getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings")) || (Wrapper.player().getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate")) || (Wrapper.player().getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet"));
    }
    
    private double getProtectionValue(final ItemStack stack) {
        return ((ItemArmor)stack.getItem()).damageReduceAmount + (100 - ((ItemArmor)stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4 * 0.0075;
    }

    @Override
    public String getModuleDesc() {
        return "Auto equip armors";
    }
}

package com.gregtechceu.gtceu.common.item.tool.behavior;

import com.gregtechceu.gtceu.api.item.component.forge.IComponentCapability;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.api.misc.forge.VoidFluidHandlerItemStack;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlungerBehavior implements IToolBehavior, IComponentCapability {

    public static final PlungerBehavior INSTANCE = PlungerBehavior.create();

    protected PlungerBehavior() {/**/}

    protected static PlungerBehavior create() {
        return new PlungerBehavior();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(ItemStack itemStack, @NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.FLUID_HANDLER_ITEM) {
            return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(cap, LazyOptional.of(() -> new VoidFluidHandlerItemStack(itemStack) {
                @Override
                public int fill(FluidStack resource, FluidAction doFill) {
                    int result = super.fill(resource, doFill);
                    if (result > 0) {
                        ToolHelper.damageItem(getContainer(), null);
                    }
                    return result;
                }
            }));
        }
        return LazyOptional.empty();
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        IFluidTransfer fluidHandler = FluidTransferHelper.getFluidTransfer(context.getLevel(), context.getClickedPos(), context.getClickedFace());
        if (fluidHandler == null) {
            return InteractionResult.PASS;
        }

        IFluidTransfer handlerToRemoveFrom = fluidHandler;
//                player.isCrouching() ?
//                (fluidHandler instanceof IOFluidTransferList ? ((IOFluidTransferList) fluidHandler).input : null) :
//                (fluidHandler instanceof IOFluidTransferList ? ((IOFluidTransferList) fluidHandler).output : fluidHandler);

        if (handlerToRemoveFrom != null && handlerToRemoveFrom.drain(FluidHelper.getBucket(), true) != null) {
            ToolHelper.onActionDone(context.getPlayer(), context.getLevel(), context.getHand());
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip,
                               @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.gtceu.tool.behavior.plunger"));
    }
}
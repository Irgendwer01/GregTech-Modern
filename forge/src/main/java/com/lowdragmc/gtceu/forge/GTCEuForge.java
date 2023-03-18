package com.lowdragmc.gtceu.forge;

import com.lowdragmc.gtceu.GTCEu;
import com.lowdragmc.gtceu.client.forge.ClientProxyImpl;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(GTCEu.MOD_ID)
public class GTCEuForge {
    public GTCEuForge() {
        GTCEu.init();
        DistExecutor.unsafeRunForDist(() -> ClientProxyImpl::new, () -> CommonProxyImpl::new);
    }

}
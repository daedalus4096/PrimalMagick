package com.verdantartifice.primalmagic.client.toast;

import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.client.Minecraft;

/**
 * Manager class for showing toasts on the client.
 * 
 * @author Daedalus4096
 */
public class ToastManager {
    public static void showResearchToast(ResearchEntry entry) {
        Minecraft.getInstance().getToasts().addToast(new ResearchToast(entry));
    }
}

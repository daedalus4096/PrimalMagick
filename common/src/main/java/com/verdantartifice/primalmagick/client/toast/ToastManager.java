package com.verdantartifice.primalmagick.client.toast;

import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import net.minecraft.client.Minecraft;

/**
 * Manager class for showing toasts on the client.
 * 
 * @author Daedalus4096
 */
public class ToastManager {
    public static void showResearchToast(ResearchEntry entry, boolean isComplete) {
        Minecraft.getInstance().getToasts().addToast(new ResearchToast(entry, isComplete));
    }

    public static void showDisciplineUnlockToast(ResearchDiscipline discipline) {
        Minecraft.getInstance().getToasts().addToast(new DisciplineUnlockToast(discipline));
    }
}

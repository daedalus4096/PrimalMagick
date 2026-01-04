package com.verdantartifice.primalmagick.common.util;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.resources.Identifier;

public class ResourceUtils {
    public static Identifier loc(String name) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, name);
    }
}

package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.datagen.models.IModelTemplateExtender;
import net.minecraft.client.data.models.model.ModelTemplate;

public interface IModelTemplateService {
    IModelTemplateExtender extend(ModelTemplate modelTemplate);
}

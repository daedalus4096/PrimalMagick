package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.datagen.models.IModelTemplateExtender;
import com.verdantartifice.primalmagick.datagen.models.ModelTemplateExtenderForge;
import com.verdantartifice.primalmagick.platform.services.IModelTemplateService;
import net.minecraft.client.data.models.model.ModelTemplate;

public class ModelTemplateServiceForge implements IModelTemplateService {
    @Override
    public IModelTemplateExtender extend(ModelTemplate modelTemplate) {
        return new ModelTemplateExtenderForge(modelTemplate);
    }
}

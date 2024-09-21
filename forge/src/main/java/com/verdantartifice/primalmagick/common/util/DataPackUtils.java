package com.verdantartifice.primalmagick.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jetbrains.annotations.Nullable;

import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class DataPackUtils {
    
    /*
     * All right. 
     * 
     * Inputs:
     *  List of items
     * 
     * Outputs:
     *  Zip file in datapack format containing:
     *   - a pack.mcmeta file including FML extensions for the mod
     *   - subdirectory data/primalmagick/affinities/item
     *   - one file per item named with the itemname containing fields:
     *     - { "type": "item", "set": { "earth": 0, "sun": 0, "moon": 0, "sky": 0, "sea": 0 }, "target": resourceName}
     *       - there's no schema-first model here; there's a deserializer but not a serializer for the datapack format.
     */

    static String itemFilePrefix = "data/primalmagick/affinities/items/";
    static String entityFilePrefix = "data/primalmagick/affinities/entity_types/";

    static String packMCMetaFilename = "pack.mcmeta";
    static String packMCMeta = """
        {
            "pack": {
              "description": "Primal Magick ModPack Base Item Affinities",
              "pack_format": %d
            }
        }
    """.formatted(SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));

    // I'm also assuming that explicitly setting 0 for things is free, so if people use these templates without cleaning them up
    // noone'll care.
    // I'm also also assuming that if you're writing a modpack you don't care about spoilers. 
    // SPOILERS:
    static String itemTemplate = """
            { "type": "item", 
              "set": { 
                "earth": 0, 
                "sun": 0, 
                "moon": 0, 
                "sky": 0, 
                "sea": 0, 
                "blood": 0, 
                "infernal": 0, 
                "void": 0, 
                "hallowed": 0 
              }, 
              "target": "%s"
            }
        """;

    static String entityTemplate = """
            { "type": "entity_type", 
              "values": { 
                "earth": 0, 
                "sun": 0, 
                "moon": 0, 
                "sky": 0, 
                "sea": 0, 
                "blood": 0, 
                "infernal": 0, 
                "void": 0, 
                "hallowed": 0 
              }, 
              "target": "%s"
            }
        """;
    // /SPOILERS. nyah.
    
    public static byte[] ItemsToDataPackTemplate(List<Item> sourceItems, List<EntityType<?>> sourceEntities) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);

        ZipEntry z = new ZipEntry(packMCMetaFilename);
        zos.putNextEntry(z);
        zos.write(packMCMeta.getBytes());
        zos.closeEntry();

        for (Item item : sourceItems) {
            @Nullable
            ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(item);
            if (resourceLocation != null) {
                String target = resourceLocation.toString();
                String filename = target.replace(":", "_")+".json";

                z = new ZipEntry(itemFilePrefix + filename);
                zos.putNextEntry(z);
                zos.write(String.format(itemTemplate, target).getBytes());
                zos.closeEntry();
            }
        }
        
        for (EntityType<?> entityType : sourceEntities) {
            ResourceLocation resourceLocation = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
            if (resourceLocation != null) {
                String target = resourceLocation.toString();
                String filename = target.replace(":", "_") + ".json";
                
                z = new ZipEntry(entityFilePrefix + filename);
                zos.putNextEntry(z);
                zos.write(String.format(entityTemplate, target).getBytes());
                zos.closeEntry();
            }
        }

        zos.close();

        return bos.toByteArray();
     }






}

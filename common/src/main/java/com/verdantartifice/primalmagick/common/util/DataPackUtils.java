package com.verdantartifice.primalmagick.common.util;

import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    static String packName = "primalMagickAffinities";
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
                "primalmagick:earth": 0,
                "primalmagick:sun": 0,
                "primalmagick:moon": 0,
                "primalmagick:sky": 0,
                "primalmagick:sea": 0,
                "primalmagick:blood": 0,
                "primalmagick:infernal": 0,
                "primalmagick:void": 0,
                "primalmagick:hallowed": 0
              }, 
              "target": "%s"
            }
        """;

    static String entityTemplate = """
            { "type": "entity_type", 
              "values": { 
                "primalmagick:earth": 0,
                "primalmagick:sun": 0,
                "primalmagick:moon": 0,
                "primalmagick:sky": 0,
                "primalmagick:sea": 0,
                "primalmagick:blood": 0,
                "primalmagick:infernal": 0,
                "primalmagick:void": 0,
                "primalmagick:hallowed": 0
              }, 
              "target": "%s"
            }
        """;
    // /SPOILERS. nyah.
    
    public static byte[] ItemsToDataPackTemplate(List<Item> sourceItems, List<EntityType<?>> sourceEntities) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);

        ZipEntry z = new ZipEntry(String.format("%s/%s", packName, packMCMetaFilename));
        zos.putNextEntry(z);
        zos.write(packMCMeta.getBytes());
        zos.closeEntry();

        for (Item item : sourceItems) {
            @Nullable
            ResourceLocation resourceLocation = Services.ITEMS_REGISTRY.getKey(item);
            if (resourceLocation != null) {
                String namespace= resourceLocation.getNamespace();
                String path = resourceLocation.getPath();
                String entryPath = String.format("%s/data/%s/affinities/items/%s.json", packName, namespace, path);
                String target = resourceLocation.toString();

                z = new ZipEntry(entryPath);
                zos.putNextEntry(z);
                zos.write(String.format(itemTemplate, target).getBytes());
                zos.closeEntry();
            }
        }
        
        for (EntityType<?> entityType : sourceEntities) {
            ResourceLocation resourceLocation = Services.ENTITY_TYPES_REGISTRY.getKey(entityType);
            if (resourceLocation != null) {
                String namespace= resourceLocation.getNamespace();
                String path = resourceLocation.getPath();
                
                String target = resourceLocation.toString();
                String entryPath = String.format("%s/data/%s/affinities/entity_types/%s.json", packName, namespace, path);
                z = new ZipEntry(entryPath);
                zos.putNextEntry(z);
                zos.write(String.format(entityTemplate, target).getBytes());
                zos.closeEntry();
            }
        }

        zos.close();

        return bos.toByteArray();
     }






}

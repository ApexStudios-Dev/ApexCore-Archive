package xyz.apex.minecraft.apexcore.forge;

import net.minecraft.core.HolderLookup;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.versions.forge.ForgeVersion;

import xyz.apex.minecraft.apexcore.forge.platform.ForgeModPlatform;
import xyz.apex.minecraft.apexcore.shared.ApexCore;
import xyz.apex.minecraft.apexcore.shared.util.ApexTags;

@Mod(ApexCore.ID)
public final class ApexCoreForge extends ForgeModPlatform implements ApexCore
{
    public ApexCoreForge()
    {
        super(null);

        modBus.addListener(this::onGatherData);
    }

    private void onGatherData(GatherDataEvent event)
    {
        var generator = event.getGenerator();

        generator.addProvider(event.includeClient(), new BlockTagsProvider(generator.getPackOutput(), event.getLookupProvider(), ApexCore.ID, event.getExistingFileHelper()) {
            @Override
            protected void addTags(HolderLookup.Provider provider)
            {
                tag(ApexTags.Blocks.IMMOVABLE).addOptionalTag(ApexTags.Blocks.tag(ForgeVersion.MOD_ID, "relocation_not_supported").location());
            }
        });
    }
}

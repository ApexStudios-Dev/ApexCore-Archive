package dev.apexstudios.apexcore.common.registry.builder;

import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class CreativeModeTabBuilder<O extends AbstractRegister<O>, P> extends AbstractBuilder<O, P, CreativeModeTab, CreativeModeTab, DeferredHolder<CreativeModeTab, CreativeModeTab>, CreativeModeTabBuilder<O, P>>
{
    private Function<Style, Style> titleStyle = Function.identity();
    private Supplier<ItemStack> icon = () -> ItemStack.EMPTY;
    private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = (parameters, output) -> { };
    private boolean canScroll = true;
    private boolean alignedRight = false;
    private String backgroundTexture = "items.png";
    private final String translationKey = Util.makeDescriptionId("itemGroup", registryName());

    @ApiStatus.Internal
    public CreativeModeTabBuilder(O owner, P parent, String registrationName, BuilderHelper helper)
    {
        super(owner, parent, Registries.CREATIVE_MODE_TAB, registrationName, DeferredHolder::create, helper);
    }

    public CreativeModeTabBuilder<O, P> titleStyle(UnaryOperator<Style> titleStyle)
    {
        this.titleStyle = this.titleStyle.andThen(titleStyle);
        return this;
    }

    public CreativeModeTabBuilder<O, P> icon(Supplier<ItemStack> icon)
    {
        this.icon = icon;
        return this;
    }

    public CreativeModeTabBuilder<O, P> icon(ItemLike icon)
    {
        return icon(() -> icon.asItem().getDefaultInstance());
    }

    public CreativeModeTabBuilder<O, P> displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator)
    {
        this.displayItemsGenerator = runAfter(displayItemsGenerator, this.displayItemsGenerator);
        return this;
    }

    public CreativeModeTabBuilder<O, P> alignedRight()
    {
        alignedRight = true;
        return this;
    }

    public CreativeModeTabBuilder<O, P> alignedLeft()
    {
        alignedRight = false;
        return this;
    }

    public CreativeModeTabBuilder<O, P> canScroll(boolean canScroll)
    {
        this.canScroll = canScroll;
        return this;
    }

    public CreativeModeTabBuilder<O, P> canScroll()
    {
        return canScroll(true);
    }

    public CreativeModeTabBuilder<O, P> noScroll()
    {
        return canScroll(false);
    }

    public CreativeModeTabBuilder<O, P> backgroundTexture(String backgroundTexture)
    {
        this.backgroundTexture = backgroundTexture;
        return this;
    }

    @Override
    protected CreativeModeTab createValue()
    {
        var builder = PlatformFactory
                .get()
                .creativeModeTabBuilder()
                .icon(icon)
                .displayItems(displayItemsGenerator)
                .backgroundSuffix(backgroundTexture);

        var translationValue = translationValue();

        if(translationValue.isEmpty())
            builder = builder.hideTitle().title(Component.empty());
        else
            builder.title(Component.translatableWithFallback(translationKey, translationValue.get()).withStyle(titleStyle::apply));

        if(!canScroll)
            builder = builder.noScrollBar();
        if(alignedRight)
            builder = builder.alignedRight();

        return builder.build();
    }

    @Override
    protected String translationKeyLookup(CreativeModeTab value)
    {
        return translationKey;
    }

    private static CreativeModeTab.DisplayItemsGenerator runAfter(CreativeModeTab.DisplayItemsGenerator before, CreativeModeTab.DisplayItemsGenerator after)
    {
        return (parameters, output) -> {
            before.accept(parameters, output);
            after.accept(parameters, output);
        };
    }
}

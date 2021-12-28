package dev.linwood.itemmods.pack.custom;

import dev.linwood.itemmods.action.CommandAction;
import dev.linwood.itemmods.pack.asset.PackAsset;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class CustomGenerator<T extends PackAsset> extends PackAsset {
    public CustomGenerator(String name) {
        super(name);
    }

    /**
     * This method is called when the user clicks on the icon to create a new asset from the generator.
     *
     * @param name         The name of the asset.
     * @param createAction The action to execute when the asset should be created.
     * @return The action to execute when the asset should be created.
     */
    public @NotNull
    abstract CommandAction generateCreateAction(String name, Consumer<T> createAction);

    /**
     * This method is called when the gui is opened to create a new asset from a generator.
     *
     * @return The ItemStack to display the generator in the gui.
     */
    public abstract @NotNull ItemStack getIcon();
}

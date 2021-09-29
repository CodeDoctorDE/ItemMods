package dev.linwood.itemmods.actions;

import dev.linwood.api.translations.Translation;
import dev.linwood.api.ui.Gui;
import org.bukkit.entity.Player;

public abstract class CommandAction {
    public void showGui(Player... players) {
        buildGui().show(players);
    }

    public abstract Gui buildGui();

    public abstract Translation getTranslation();
}

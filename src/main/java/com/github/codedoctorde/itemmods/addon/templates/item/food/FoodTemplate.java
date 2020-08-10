package com.github.codedoctorde.itemmods.addon.templates.item.food;

import com.github.codedoctorde.itemmods.ItemMods;
import com.github.codedoctorde.itemmods.api.item.CustomItemTemplate;
import com.github.codedoctorde.itemmods.config.ItemConfig;
import com.github.codedoctorde.api.ui.Gui;
import com.github.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FoodTemplate implements CustomItemTemplate {
    JsonObject templateTranslation = ItemMods.getPlugin().getTranslationConfig().getJsonObject().getAsJsonObject("addon")
            .getAsJsonObject("templates").getAsJsonObject("item").getAsJsonObject("food");
    @NotNull
    @Override
    public ItemStack getIcon(ItemConfig itemConfig) {
        return new ItemStackBuilder(templateTranslation.getAsJsonObject("icon")).build();
    }

    @NotNull
    @Override
    public ItemStack getMainIcon(ItemConfig itemConfig) {
        return new ItemStackBuilder(templateTranslation.getAsJsonObject("main-icon")).format().build();
    }

    @Override
    public boolean isCompatible(ItemConfig itemConfig) {
        return false;
    }

    @Override
    public boolean openConfigGui(ItemConfig itemConfig, Player player) {
        new FoodTemplateGui(this, itemConfig).createGui().open(player);
        return true;
    }

    @NotNull
    @Override
    public String getName() {
        return templateTranslation.get("name").getAsString();
    }

    static class FoodTemplateData {
        private final FoodTemplate template;
        private final ItemConfig itemConfig;
        private int level;
        private float exhaustion;
        private List<PotionEffect> effects = new ArrayList<>();

        private final Gson gson = new Gson();

        FoodTemplateData(FoodTemplate template, ItemConfig itemConfig) {
            this.template = template;
            this.itemConfig = itemConfig;
            JsonObject jsonObject = itemConfig.getTemplateConfig();
            if (jsonObject.has("level") && jsonObject.get("level").isJsonPrimitive())
                this.level = jsonObject.get("level").getAsInt();
            if (jsonObject.has("exhaustion") && jsonObject.get("exhaustion").isJsonPrimitive())
                this.level = jsonObject.get("exhaustion").getAsInt();
            if (jsonObject.has("effects") && jsonObject.get("effects").isJsonArray())
                jsonObject.getAsJsonArray("effects").forEach(element -> {
                    JsonObject effect = (JsonObject) element;
                    PotionEffectType type = PotionEffectType.getByName(effect.get("name").getAsString());
                    if(type != null)
                        effects.add(new PotionEffect(type,
                                effect.get("duration").getAsInt(), effect.get("amplifier").getAsInt()
                        ));
                });
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public float getExhaustion() {
            return exhaustion;
        }

        public void setExhaustion(float exhaustion) {
            this.exhaustion = exhaustion;
        }

        public List<PotionEffect> getEffects() {
            return effects;
        }

        public FoodTemplate getTemplate() {
            return template;
        }

        public ItemConfig getItemConfig() {
            return itemConfig;
        }
        public void save() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("level", level);
            jsonObject.addProperty("exhaustion", exhaustion);
            JsonArray effectsArray = new JsonArray();
            effects.forEach(potionEffect -> {
                JsonObject object = new JsonObject();
                object.addProperty("amplifier", potionEffect.getAmplifier());
                object.addProperty("duration", potionEffect.getDuration());
                effectsArray.add(object);
            });
            jsonObject.add("effects", effectsArray);
            itemConfig.setTemplateConfig(jsonObject);
            ItemMods.getPlugin().saveBaseConfig();
        }
    }
}

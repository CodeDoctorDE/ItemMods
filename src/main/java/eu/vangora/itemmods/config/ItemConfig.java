package eu.vangora.itemmods.config;

import com.gitlab.codedoctorde.api.config.JsonConfigurationArray;
import com.gitlab.codedoctorde.api.config.JsonConfigurationElement;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.config.JsonConfigurationValue;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonElement;
import eu.vangora.itemmods.main.ArmorType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemConfig extends JsonConfigurationElement {
    private String name;
    private ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK);
    private boolean canRename = true;
    private int pickaxe = 0;
    private int shovel = 0;
    private int axe = 0;
    private int hoe = 0;
    private int damage = 0;
    private int speed = 0;
    private boolean boneMeal = false;
    private ArmorType armorType = ArmorType.NONE;
    private List<String> onHelmet = new ArrayList<>();
    private List<String> onChestplate = new ArrayList<>();
    private List<String> onLeggings = new ArrayList<>();
    private List<String> onBoots = new ArrayList<>();
    private List<String> onOffHand = new ArrayList<>();
    private List<String> onMainHand = new ArrayList<>();
    private List<String> onDrop = new ArrayList<>();
    private List<String> onPickup = new ArrayList<>();


    public ItemConfig(JsonElement element) {
        fromElement(element);
    }

    public ItemConfig(String name) {
        this.name = name;
    }


    @Override
    public JsonElement getElement() {
        JsonConfigurationSection config = new JsonConfigurationSection();
        config.setValue(name, "name");
        config.setValue(new ItemStackBuilder(itemStack).serialize(), "itemStack");
        config.setValue(canRename, "rename");
        config.setValue(boneMeal, "bonemeal");
        config.setValue(armorType.name(),"armortype");
        config.setValue((JsonConfigurationElement) onHelmet.stream().map(JsonConfigurationValue::new).collect(Collectors.toCollection(JsonConfigurationArray::new)), "helmet");
        config.setValue((JsonConfigurationElement) onChestplate.stream().map(JsonConfigurationValue::new).collect(Collectors.toCollection(JsonConfigurationArray::new)), "chestplate");
        config.setValue((JsonConfigurationElement) onLeggings.stream().map(JsonConfigurationValue::new).collect(Collectors.toCollection(JsonConfigurationArray::new)), "leggings");
        config.setValue((JsonConfigurationElement) onBoots.stream().map(JsonConfigurationValue::new).collect(Collectors.toCollection(JsonConfigurationArray::new)), "boots");
        config.setValue((JsonConfigurationElement) onMainHand.stream().map(JsonConfigurationValue::new).collect(Collectors.toCollection(JsonConfigurationArray::new)), "mainhand");
        config.setValue((JsonConfigurationElement) onOffHand.stream().map(JsonConfigurationValue::new).collect(Collectors.toCollection(JsonConfigurationArray::new)), "offhand");
        config.setValue((JsonConfigurationElement) onDrop.stream().map(JsonConfigurationValue::new).collect(Collectors.toCollection(JsonConfigurationArray::new)), "drop");
        config.setValue((JsonConfigurationElement) onPickup.stream().map(JsonConfigurationValue::new).collect(Collectors.toCollection(JsonConfigurationArray::new)), "pickup");
        return config.getElement();
    }

    @Override
    public void fromElement(JsonElement element) {
        JsonConfigurationSection config = new JsonConfigurationSection(element.getAsJsonObject());
        itemStack = new ItemStackBuilder(config.getValue("itemStack")).build();
        name = config.getString("name");
        canRename = config.getBoolean("rename");
        boneMeal = config.getBoolean("bonemeal");
        armorType = ArmorType.valueOf(config.getString("armortype"));
        config.getArray("helmet").forEach(helmet -> onHelmet.add(helmet.toConfigValue().getString()));
        config.getArray("chestplate").forEach(chestplate -> onChestplate.add(chestplate.toConfigValue().getString()));
        config.getArray("leggings").forEach(leggings -> onLeggings.add(leggings.toConfigValue().getString()));
        config.getArray("boots").forEach(boots -> onBoots.add(boots.toConfigValue().getString()));
        config.getArray("mainhand").forEach(mainhand -> onMainHand.add(mainhand.toConfigValue().getString()));
        config.getArray("offhand").forEach(offhand -> onOffHand.add(offhand.toConfigValue().getString()));
        config.getArray("drop").forEach(drop -> onDrop.add(drop.toConfigValue().getString()));
        config.getArray("pickup").forEach(pickup -> onPickup.add(pickup.toConfigValue().getString()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isCanRename() {
        return canRename;
    }

    public void setCanRename(boolean canRename) {
        this.canRename = canRename;
    }

    public int getAxe() {
        return axe;
    }

    public void setAxe(int axe) {
        this.axe = axe;
    }

    public int getHoe() {
        return hoe;
    }

    public void setHoe(int hoe) {
        this.hoe = hoe;
    }

    public int getPickaxe() {
        return pickaxe;
    }

    public void setPickaxe(int pickaxe) {
        this.pickaxe = pickaxe;
    }

    public int getShovel() {
        return shovel;
    }

    public void setShovel(int shovel) {
        this.shovel = shovel;
    }

    public boolean isBoneMeal() {
        return boneMeal;
    }

    public void setBoneMeal(boolean boneMeal) {
        this.boneMeal = boneMeal;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArmorType getArmorType() {
        return armorType;
    }

    public void setArmorType(ArmorType armorType) {
        this.armorType = armorType;
    }

    public List<String> getOnBoots() {
        return onBoots;
    }

    public List<String> getOnChestplate() {
        return onChestplate;
    }

    public List<String> getOnDrop() {
        return onDrop;
    }

    public List<String> getOnHelmet() {
        return onHelmet;
    }

    public List<String> getOnLeggings() {
        return onLeggings;
    }

    public List<String> getOnMainHand() {
        return onMainHand;
    }

    public List<String> getOnOffHand() {
        return onOffHand;
    }

    public List<String> getOnPickup() {
        return onPickup;
    }
}

package eu.vangora.itemmods.config;

import com.gitlab.codedoctorde.api.config.JsonConfigurationElement;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonElement;
import eu.vangora.itemmods.main.ArmorType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
}

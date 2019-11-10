package eu.vangora.itemmods.config;

import com.gitlab.codedoctorde.api.config.JsonConfigurationElement;
import com.gitlab.codedoctorde.api.config.JsonConfigurationSection;
import com.gitlab.codedoctorde.api.utils.ItemStackBuilder;
import com.google.gson.JsonElement;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public class BlockConfig extends JsonConfigurationElement {
    private String name;
    private String tag = "";
    private String displayName;
    private BlockData block;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack mainHand;
    private ItemStack offHand;
    private boolean small = false;
    private boolean basePlate = true;
    private boolean invisible = true;
    private boolean marker = false;
    private boolean invulnerable = true;
    private boolean customNameVisible = false;
    private String customName;
    private ItemStack itemStack;


    public BlockConfig(JsonElement element) {
        fromElement(element);
    }

    public BlockConfig(String name) {
        this.name = name;
        this.displayName = name;
        this.tag = "itemmods:" + name;
        this.customName = name;
    }


    @Override
    public JsonElement getElement() {
        JsonConfigurationSection config = new JsonConfigurationSection();
        config.setValue(name, "name");
        config.setValue(tag, "tag");
        config.setValue(displayName, "displayname");
        config.setValue(new ItemStackBuilder(helmet).serialize(), "helmet");
        config.setValue(new ItemStackBuilder(chestplate).serialize(), "chestplate");
        config.setValue(new ItemStackBuilder(leggings).serialize(), "leggings");
        config.setValue(new ItemStackBuilder(boots).serialize(), "boots");
        config.setValue(new ItemStackBuilder(mainHand).serialize(), "mainhand");
        config.setValue(new ItemStackBuilder(offHand).serialize(), "offhand");
        config.setValue(small, "small");
        config.setValue(basePlate, "baseplate");
        config.setValue(invisible, "invisible");
        if (block != null)
            config.setValue(block.getAsString(), "block");
        config.setValue(new ItemStackBuilder(itemStack).serialize(), "itemstack");
        return config.getElement();
    }

    @Override
    public void fromElement(JsonElement element) {
        JsonConfigurationSection config = new JsonConfigurationSection(element.getAsJsonObject());
        helmet = new ItemStackBuilder(config.getValue("helmet")).build();
        chestplate = new ItemStackBuilder(config.getValue("chestplate")).build();
        leggings = new ItemStackBuilder(config.getValue("leggings")).build();
        boots = new ItemStackBuilder(config.getValue("boots")).build();
        mainHand = new ItemStackBuilder(config.getValue("mainhand")).build();
        offHand = new ItemStackBuilder(config.getValue("offhand")).build();
        name = config.getString("name");
        displayName = config.getString("displayname");
        tag = config.getString("tag");
        small = config.getBoolean("small");
        invisible = config.getBoolean("invisible");
        basePlate = config.getBoolean("baseplate");
        itemStack = new ItemStackBuilder(config.getValue("itemstack")).build();
        if (config.containsKey("block"))
            block = Bukkit.createBlockData(config.getString("block"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBasePlate() {
        return basePlate;
    }

    public void setBasePlate(boolean basePlate) {
        this.basePlate = basePlate;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public BlockData getBlock() {
        return block;
    }

    public void setBlock(BlockData block) {
        this.block = block;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public void setMainHand(ItemStack mainHand) {
        this.mainHand = mainHand;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    public void setOffHand(ItemStack offHand) {
        this.offHand = offHand;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isCustomNameVisible() {
        return customNameVisible;
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        this.customNameVisible = customNameVisible;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public boolean isMarker() {
        return marker;
    }

    public void setMarker(boolean marker) {
        this.marker = marker;
    }
}

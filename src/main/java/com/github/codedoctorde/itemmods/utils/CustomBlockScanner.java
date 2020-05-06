package com.github.codedoctorde.itemmods.utils;

import com.github.codedoctorde.itemmods.Main;
import com.github.codedoctorde.itemmods.api.block.CustomBlock;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CodeDoctorDE
 */
public class CustomBlockScanner {
    public List<CustomBlock> getCustomBlocks(Chunk chunk) {
        List<CustomBlock> customBlocks = new ArrayList<>();
        for (BlockState blockState :
                chunk.getTileEntities()) {
            CustomBlock customBlock = Main.getPlugin().getApi().getCustomBlockManager().getCustomBlock(blockState.getLocation());
            if (customBlock != null)
                customBlocks.add(customBlock);
        }
        for (Entity entity :
                chunk.getEntities()) {
            if (entity instanceof ArmorStand) {
                CustomBlock customBlock = Main.getPlugin().getApi().getCustomBlockManager().getCustomBlock(entity.getLocation());
                if (customBlock != null) {
                    boolean exist = false;
                    for (CustomBlock current :
                            customBlocks)
                        if (current.getLocation().distance(entity.getLocation()) > 1)
                            exist = true;
                    if (!exist)
                        customBlocks.add(customBlock);
                }
            }
        }
        return customBlocks;
    }
}

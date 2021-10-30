package dev._2lstudios.mineorm.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.mineorm.common.MineORMPlatform;

public class MineORMBukkit extends JavaPlugin {
    @Override
    public void onEnable() {
        new MineORMPlatform(this.getDataFolder(), this.getLogger());
    }
}

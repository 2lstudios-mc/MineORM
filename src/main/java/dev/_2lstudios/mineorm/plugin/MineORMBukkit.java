package dev._2lstudios.mineorm.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class MineORMBukkit extends JavaPlugin {

    @Override
    public void onEnable() {
        new MineORMPlatform();
    }
}

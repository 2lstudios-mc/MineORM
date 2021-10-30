package dev._2lstudios.mineorm.plugin;

import net.md_5.bungee.api.plugin.Plugin;

public class MineORMBungee extends Plugin {
    @Override
    public void onEnable() {
        new MineORMPlatform(this.getDataFolder(), this.getLogger());
    }
}

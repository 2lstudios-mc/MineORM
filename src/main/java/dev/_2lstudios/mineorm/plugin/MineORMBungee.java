package dev._2lstudios.mineorm.plugin;

import dev._2lstudios.mineorm.common.MineORMPlatform;
import net.md_5.bungee.api.plugin.Plugin;

public class MineORMBungee extends Plugin {
    @Override
    public void onEnable() {
        new MineORMPlatform(this.getDataFolder(), this.getLogger());
    }
}

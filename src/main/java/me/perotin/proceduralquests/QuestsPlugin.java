package me.perotin.proceduralquests;

import me.perotin.proceduralquests.files.FileWrapper;
import org.bukkit.plugin.java.JavaPlugin;

/*
    Quests plugin created January 4th by Perotin ©

 */
public class QuestsPlugin extends JavaPlugin{


    @Override
    public void onEnable(){
        FileWrapper.loadFiles(this);
    }


}

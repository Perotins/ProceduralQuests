package me.perotin.proceduralquests.errors;

import me.perotin.proceduralquests.QuestsPlugin;
import me.perotin.proceduralquests.files.FileWrapper;
import org.bukkit.entity.Player;

public class AlreadyHaveQuestException extends Exception {

    private Player thrower;
    private FileWrapper messages;


    public AlreadyHaveQuestException(Player thrower, QuestsPlugin plugin){
        this.messages = new FileWrapper(FileWrapper.FileType.MESSAGES, plugin);
        this.thrower = thrower;
        // make message interpolate with messages file and take in users name



    }
}

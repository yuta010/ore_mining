package plugin.oremining;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.oremining.command.GameCancelCommand;
import plugin.oremining.command.OreMiningCommand;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        OreMiningCommand oreMiningCommand = new OreMiningCommand(this);
        Bukkit.getPluginManager().registerEvents(oreMiningCommand, this);
        getCommand("oreMining").setExecutor(oreMiningCommand);

        GameCancelCommand gameCancelCommand = new GameCancelCommand(this, oreMiningCommand);
        getCommand("gameCancel").setExecutor(gameCancelCommand);
    }
}

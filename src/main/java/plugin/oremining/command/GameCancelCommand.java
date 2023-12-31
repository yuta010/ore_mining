package plugin.oremining.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.oremining.Main;
import plugin.oremining.data.ExecutingPlayer;

import java.util.List;

/**
 * OreMiningを強制終了させるコマンドです。
 */
public class GameCancelCommand extends BaseCommand {

  private final Main main;
  private final OreMiningCommand oreMiningCommand;

  public GameCancelCommand (Main main, OreMiningCommand oreMiningCommand) {
    this.main = main;
    this.oreMiningCommand = oreMiningCommand;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label, String[] args) {
    List<ExecutingPlayer> executingPlayerList = oreMiningCommand.executingPlayerList;
    boolean executingPlayer = executingPlayerList
        .stream()
        .anyMatch(p -> p.getName().equals(player.getName()));

    if(executingPlayer){
      Bukkit.getScheduler().cancelTasks(main);
      executingPlayerList.clear();
      player.sendMessage("ゲーム強制終了!!");
    } else {
      player.sendMessage("ゲームは実行されていません。");
    }
    return false;
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender sender, Command command, String label, String[] args) {
    return false;
  }
}

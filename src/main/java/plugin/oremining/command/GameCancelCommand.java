package plugin.oremining.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * ゲームを途中で終了する場合に使用するコマンド
 */
public class GameCancelCommand extends BaseCommand implements Listener {

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label, String[] args) {
    return true;
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender sender, Command command, String label, String[] args) {
    return false;
  }
}

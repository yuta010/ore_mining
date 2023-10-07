package plugin.oremining.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * コマンドを実行して動かすプラグイン処理の基底クラス
 */
public abstract class BaseCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player player) {
      return onExecutePlayerCommand(player);
    } else {
      return onExecuteNPCCommand(sender);
    }
  }

  /**
   * コマンド実行者がプレイヤーだった場合に実行
   * @param player　コマンドを実行したプレイヤー
   * @return  処理の実行有無
   */
  public abstract boolean onExecutePlayerCommand(Player player);

  /**
   * コマンド実行者がプレイヤー以外だった場合に実行
   * @param sender  コマンド実行者
   * @return  処理の実行有無
   */
  public abstract boolean onExecuteNPCCommand(CommandSender sender);
}

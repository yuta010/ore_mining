package plugin.oremining.command;

import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class OreMiningCommand implements CommandExecutor, Listener {

  private Player player;
  private int score;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player player) {
      this.player = player;
      player.sendTitle("鉱石採掘ゲームスタート", player.getName(),
          0, 50, 0);

      initialSet(player);

      //チェック用　後々削除予定
      World world = player.getWorld();
      Location playerLocation = player.getLocation();
      world.setBlockData(playerLocation, Material.EMERALD_ORE.createBlockData());
    }
    return false;
  }

  /**
   * ゲーム開始時に体力と空腹度を20に設定し、ダイヤモンドピッケルを装備
   *
   * @param player コマンドを実行したプレイヤー
   */
  private void initialSet(Player player) {
    player.setFoodLevel(20);
    player.setHealth(20);
    PlayerInventory inventory = player.getInventory();
    inventory.setItemInMainHand(new ItemStack(Material.DIAMOND_PICKAXE));
  }

  /**
   * 特定の鉱石を採掘した時に点数を加算します。
   * 石炭鉱石・鉄鉱石10点、金鉱石50点、ダイヤモンド鉱石100点
   * @param e コマンドを実行したプレイヤー
   */
  @EventHandler
  public void PlayerBlockDropItemEvent(BlockDropItemEvent e) {
    Player player = e.getPlayer();
    BlockState blockState = e.getBlockState();

    if (Objects.isNull(player)) {
      return;
    }
    if (Objects.isNull(this.player)) {
      return;
    }

    if (this.player.equals(player)) {
      Material type = blockState.getType();
      if (type == Material.COAL_ORE || type == Material.IRON_ORE) {
        score += 10;
        player.sendMessage("現在のプレイヤーのスコアは" + score + " 点");
      } else if (type == Material.GOLD_ORE) {
        score += 50;
        player.sendMessage("現在のプレイヤーのスコアは" + score + " 点");
      } else if (type == Material.DIAMOND_ORE) {
        score += 100;
        player.sendMessage("現在のプレイヤーのスコアは" + score + " 点");
      }
    }
  }
}

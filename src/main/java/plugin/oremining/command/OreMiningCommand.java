package plugin.oremining.command;

import java.util.Objects;
import org.bukkit.Material;
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

      //ダイヤモンドのピッケルを取得
      PlayerInventory inventory = player.getInventory();
      inventory.setItemInMainHand(new ItemStack(Material.DIAMOND_PICKAXE));
    }
    return false;
  }

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
      if (blockState.getType().equals(Material.COAL_ORE)) {
        score += 10;
        player.sendMessage("プレイヤーは石炭をゲットしました！" + score + " 点");
      } else if (blockState.getType().equals(Material.IRON_ORE)) {
        score += 10;
        player.sendMessage("プレイヤーは鉄をゲットしました！" + score + " 点");
      }
      }
    }
  }

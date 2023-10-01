package plugin.oremining.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class OreMiningCommand implements CommandExecutor, Listener {

  private Player player;
  private int score;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player player){
      this.player = player;
      player.sendMessage("鉱石採掘ゲームが開始されました！！");

      //ダイヤモンドのピッケルを取得
      PlayerInventory inventory = player.getInventory();
      inventory.setItemInMainHand(new ItemStack(Material.DIAMOND_PICKAXE));
    }
    return false;
  }

  @EventHandler
  public void PlayerHarvestBlockEvent(BlockBreakEvent e){
    Player player = e.getPlayer();
    if (this.player.equals(player)){
      if(e.getBlock().getType().equals(Material.COAL_ORE)) {
        score += 10;
        player.sendMessage("プレイヤーは石炭をゲットしました！" + score + "点" );
      }
    }
  }
}

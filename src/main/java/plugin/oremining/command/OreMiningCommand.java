package plugin.oremining.command;

import static org.bukkit.Material.DIAMOND_PICKAXE;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import plugin.oremining.Main;

/**
 * 制限時間内に指定した鉱石を採掘して、スコアを獲得するゲームを起動するコマンドです。
 * スコアは鉱石によって変わり、採掘した鉱石の合計によってスコアが変動します。
 */
public class OreMiningCommand extends BaseCommand implements  Listener {

  private Main main;
  private Player player;
  private int gameTime = 300;
  private int score ;

  public OreMiningCommand(Main main) {
    this.main = main;
  }
  @Override
  public boolean onExecutePlayerCommand(Player player) {
    this.player = player;

    gameTime = 300;

    player.sendTitle("鉱石採掘ゲームスタート",
        player.getName() + "制限時間300秒",
        0, 70, 0);

    initialSet(player);

    gamePlay(player);

    //playerBlockDropItemEventをここに追加予定!!

    return true;
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender sender) {
    return false;
  }

  /**
   * ゲーム開始時に体力と空腹度を20に設定し、ダイヤモンドピッケルを装備*
   * @param player コマンドを実行したプレイヤー
   */
  private void initialSet(Player player) {
    player.setFoodLevel(20);
    player.setHealth(20);
    PlayerInventory inventory = player.getInventory();
    inventory.setItemInMainHand(new ItemStack(DIAMOND_PICKAXE));
  }

  /**
   * ゲームの時間及び最終的なスコアを表示
   * @param player コマンドを実行したプレイヤー
   */
  private void gamePlay(Player player) {
    Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
      if (gameTime <= 0) {
        Runnable.cancel();
        player.sendTitle("ゲームが終了しました。",
            player.getName() + " の点数は" + score + " 点です",
            0, 70, 0);
        score = 0;
        return;
      }
      player.sendMessage("残り時間 " + gameTime + " 秒!");
      gameTime -= 120;
    }, 0, 120 * 20);
  }

  /**
   * 特定の鉱石を採掘した時に点数を加算します。
   * 石炭鉱石・鉄鉱石10点、金鉱石50点、ダイヤモンド鉱石100点
   * @param dropItemEvent イベントを発生させたプレイヤー
   */
  @EventHandler
  public void playerBlockDropItemEvent(BlockDropItemEvent dropItemEvent) {
    Player player = dropItemEvent.getPlayer();
    BlockState blockState = dropItemEvent.getBlockState();
    Material type = blockState.getType();

    if (Objects.isNull(this.player)) {
      return;
    }

    if (this.player.equals(player)) {
      switch (type) {
        case COAL_ORE, IRON_ORE, GOLD_ORE, DIAMOND_ORE -> {
          switch (type) {
            case COAL_ORE, IRON_ORE -> score += 10;
            case GOLD_ORE -> score += 50;
            case DIAMOND_ORE -> score += 100;
          }
          player.sendMessage("現在のスコアは" + score + " 点です。");
        }
      }
    }
  }
  }
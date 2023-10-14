package plugin.oremining.command;

import static org.bukkit.Material.DIAMOND_PICKAXE;

import java.util.ArrayList;
import java.util.List;
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
import plugin.oremining.data.PlayerScore;

/**
 * 制限時間内に指定した鉱石を採掘して、スコアを獲得するゲームを起動するコマンドです。
 * スコアは鉱石によって変わり、採掘した鉱石の合計によってスコアが変動します。
 * スコアの結果はプレイヤー名、スコア、日時などで保存されます。
 */
public class OreMiningCommand extends BaseCommand implements  Listener {

  public static final int GAME_TIME = 300;
  private final Main main;
  List<PlayerScore> playerScoreList = new ArrayList<>();


  public OreMiningCommand(Main main) {
    this.main = main;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player) {

    PlayerScore nowPlayerScore = getPlayerScore(player);

    getPlayerScore(player);

    initialSet(player);

    gameStart(player);

    gamePlay(player, nowPlayerScore);

    return true;
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender sender) {
    return false;
  }

  /**
   * 特定の鉱石を採掘した時にスコアを加算します。 石炭鉱石10点、鉄鉱石100点、金鉱石800点、ダイヤモンド鉱石1000点
   *
   * @param dropItemEvent アイテムを採掘した時に発生するイベント
   */
  @EventHandler
  public void playerBlockDropItemEvent(BlockDropItemEvent dropItemEvent) {
    Player player = dropItemEvent.getPlayer();
    BlockState blockState = dropItemEvent.getBlockState();
    Material type = blockState.getType();

    if (playerScoreList.isEmpty()) {
      return;
    }

    for (PlayerScore playerScore : playerScoreList) {
      if (playerScore.getPlayerName().equals(player.getName())) {
        int score = 0;
        switch (type) {
          case COAL_ORE, IRON_ORE, GOLD_ORE, DIAMOND_ORE -> {
            switch (type) {
              case COAL_ORE -> score += 10;
              case IRON_ORE -> score += 100;
              case GOLD_ORE -> score += 800;
              case DIAMOND_ORE -> score += 1000;
            }

            //ゲーム終了後にスコアが入らないように設定
            if (playerScore.getGameTime() > 0) {
              playerScore.setScore(playerScore.getScore() + score);
              player.sendMessage("現在のスコアは" + playerScore.getScore() + " 点");
            }
          }
        }
      }
    }
  }

  /**
   * 現在実行しているプレイヤーのスコア情報を取得する。
   *
   * @param player コマンドを実行したプレイヤー
   * @return 現在ゲームを実行しているプレイヤーのスコア情報
   */
  private PlayerScore getPlayerScore(Player player) {
    PlayerScore playerScore = new PlayerScore(player.getName());

    if (playerScoreList.isEmpty()) {
      playerScore = addNewPlayer(player);
    } else {
      playerScore = playerScoreList.stream().findFirst().map(ps
          -> ps.getPlayerName().equals(player.getName())
          ? ps
          : addNewPlayer(player)).orElse(playerScore);
    }
    playerScore.setScore(0);
    playerScore.setGameTime(GAME_TIME);
    return playerScore;
  }

  /**
   * 新規のプレイヤー情報をリストに追加されます。
   *
   * @param player コマンドを実行したプレイヤー
   * @return 新規プレイヤー
   */
  private PlayerScore addNewPlayer(Player player) {
    PlayerScore newPlayer = new PlayerScore(player.getName());
    playerScoreList.add(newPlayer);
    return newPlayer;
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
    inventory.setItemInMainHand(new ItemStack(DIAMOND_PICKAXE));
  }

  /**
   * ゲーム開始を知らせる。
   *
   * @param player コマンドを実行したプレイヤー
   */
  private void gameStart(Player player) {
    player.sendTitle("鉱石採掘ゲームスタート",
        player.getName() + "制限時間" + GAME_TIME / 60 + " 分",
        0, 70, 0);
  }

  /**
   * ゲームを実行します。規定の時間内に特定の鉱石を採掘するとスコアが加算されます。合計スコアを時間経過後に表示します。
   *
   * @param player    コマンドを実行したプレイヤー
   * @param nowPlayer プレイヤースコア情報
   */
  private void gamePlay(Player player, PlayerScore nowPlayer) {
    Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
      if (nowPlayer.getGameTime() <= 0) {
        Runnable.cancel();

        player.sendTitle("ゲームが終了しました。",
            nowPlayer.getPlayerName() + " の点数は" + nowPlayer.getScore() + " 点",
            0, 70, 0);
        return;
      }
      if(nowPlayer.getGameTime() == 60
          || nowPlayer.getGameTime() == 120
          || nowPlayer.getGameTime() == 180
          || nowPlayer.getGameTime() == 240 ) {
        player.sendMessage("残り " + nowPlayer.getGameTime() / 60 + " 分\n"
        + "現在のスコア" + nowPlayer.getScore() + " 点");
      }
      nowPlayer.setGameTime(nowPlayer.getGameTime() - 1);
    }, 0, 20);
  }
}
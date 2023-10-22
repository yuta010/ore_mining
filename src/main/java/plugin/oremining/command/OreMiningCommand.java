package plugin.oremining.command;

import static org.bukkit.Material.DIAMOND_PICKAXE;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import plugin.oremining.Main;
import plugin.oremining.data.ExecutingPlayer;
import plugin.oremining.mapper.PlayerScoreMapper;
import plugin.oremining.mapper.data.PlayerScore;

/**
 * 制限時間内に指定した鉱石を採掘して、スコアを獲得するゲームを起動するコマンドです。
 * スコアは鉱石によって変わり、採掘した鉱石の合計によってスコアが変動します。
 * スコアの結果はプレイヤー名、スコア、日時などで保存されます。
 */
public class OreMiningCommand extends BaseCommand implements  Listener {

  public static final int GAME_TIME = 300;
  private final Main main;
  List<ExecutingPlayer> executingPlayerList = new ArrayList<>();
  public static final String List = "list";
  private final SqlSessionFactory sqlSessionFactory;

  public OreMiningCommand(Main main) {
    this.main = main;

    try {
      InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label, String[] args) {
    if (args.length == 1 && List.equals(args[0])){

      try (SqlSession session = sqlSessionFactory.openSession()) {
        PlayerScoreMapper mapper = session.getMapper(PlayerScoreMapper.class);
        List<PlayerScore> playerScoreList = mapper.selectList();
        for(PlayerScore playerScore : playerScoreList){
          player.sendMessage(String.format(playerScore.getId()
              + " | " + playerScore.getPlayerName()
              + " | " + playerScore.getScore()
              + " | " + playerScore.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        }
      }
      return false;
    } else if (args.length == 0) {
      ExecutingPlayer nowExecutingPlayer = getPlayerScore(player);

      getPlayerScore(player);

      initialSet(player);

      gameStart(player);

      gamePlay(player, nowExecutingPlayer);

    } else {
      player.sendMessage("実行できません。ゲームを実行する場合引数はなし。\n"
          + "スコアを表示する場合はlistを入力してください。");
    }
      return true;
  }


  @Override
  public boolean onExecuteNPCCommand(CommandSender sender, Command command, String label, String[] args) {
    return false;
  }

  /**
   * 特定の鉱石を採掘した時にスコアを加算します。 石炭鉱石10点、鉄鉱石100点、金鉱石800点、ダイヤモンド鉱石1000点
   * @param dropItemEvent アイテムを採掘した時に発生するイベント
   */
  @EventHandler
  public void playerBlockDropItemEvent(BlockDropItemEvent dropItemEvent) {
    Player player = dropItemEvent.getPlayer();
    BlockState blockState = dropItemEvent.getBlockState();
    Material type = blockState.getType();

    if (executingPlayerList.isEmpty()) {
      return;
    }

    for (ExecutingPlayer p : executingPlayerList) {
      if (p.getPlayerName().equals(player.getName())) {
        int score = 0;
        switch (type) {
          case COAL_ORE, IRON_ORE, GOLD_ORE, DIAMOND_ORE -> {
            switch (type) {
              case COAL_ORE -> score += 10;
              case IRON_ORE -> score += 100;
              case GOLD_ORE -> score += 800;
              case DIAMOND_ORE -> score += 1000;
            }
          }
        }
        //ゲーム終了後にスコアが入らないように設定
        if (p.getGameTime() > 0 && !(score == 0)) {
          p.setScore(p.getScore() + score);
          player.sendMessage("現在のスコアは" + p.getScore() + " 点");
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
  private ExecutingPlayer getPlayerScore(Player player) {
    ExecutingPlayer executingPlayer = new ExecutingPlayer(player.getName());

    if (executingPlayerList.isEmpty()) {
      executingPlayer = addNewPlayer(player);
    } else {
      executingPlayer = executingPlayerList.stream().findFirst().map(ps
          -> ps.getPlayerName().equals(player.getName())
          ? ps
          : addNewPlayer(player)).orElse(executingPlayer);
    }
    executingPlayer.setScore(0);
    executingPlayer.setGameTime(GAME_TIME);
    removePotionEffect(player);
    return executingPlayer;
  }

  /**
   * 新規のプレイヤー情報をリストに追加されます。
   *
   * @param player コマンドを実行したプレイヤー
   * @return 新規プレイヤー
   */
  private ExecutingPlayer addNewPlayer(Player player) {
    ExecutingPlayer newPlayer = new ExecutingPlayer(player.getName());
    executingPlayerList.add(newPlayer);
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
   * @param player  コマンドを実行したプレイヤー
   * @param nowExecutingPlayer プレイヤースコア情報
   */
  private void gamePlay(Player player, ExecutingPlayer nowExecutingPlayer) {
    Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
      if (nowExecutingPlayer.getGameTime() <= 0) {
        Runnable.cancel();

        player.sendTitle("ゲームが終了しました。",
            nowExecutingPlayer.getPlayerName() + " の点数は" + nowExecutingPlayer.getScore() + " 点",
            0, 70, 0);

        removePotionEffect(player);

        try (SqlSession session = sqlSessionFactory.openSession(true)) {
          PlayerScoreMapper mapper = session.getMapper(PlayerScoreMapper.class);
          mapper.insert(new PlayerScore(
              nowExecutingPlayer.getPlayerName(),
              nowExecutingPlayer.getScore()));
        }
        return;
      }

      switch (nowExecutingPlayer.getGameTime()) {
        case 60, 120, 180, 240 ->
            player.sendMessage("残り " + nowExecutingPlayer.getGameTime() / 60 + " 分\n"
            + "現在のスコア" + nowExecutingPlayer.getScore() + " 点");
      }
      nowExecutingPlayer.setGameTime(nowExecutingPlayer.getGameTime() - 1);
    }, 0, 20);
  }

  /**
   * プレイヤーに設定されている特殊効果を除外します。
   * @param player  コマンドを実行したプレイヤー
   */
  private static void removePotionEffect(Player player) {
    player.getActivePotionEffects()
        .stream()
        .map(PotionEffect::getType)
        .forEach(player::removePotionEffect);
  }
}
package plugin.oremining.data;

import lombok.Getter;
import lombok.Setter;

/**
 *  OreMiningのゲームを実行する際のスコア情報を扱うオブジェクト
 *  プレイヤー名、合計、時間、日時を取り扱うオブジェクト
 */
@Getter
@Setter

public class ExecutingPlayer {
  private String name;
  private int score;
  private int gameTime;

  public ExecutingPlayer(String name) {
    this.name = name;
  }
}

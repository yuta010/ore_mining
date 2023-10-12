package plugin.oremining.data;

import lombok.Getter;
import lombok.Setter;

/**
 *  OreMiningのゲームを実行する際のスコア情報を扱うオブジェクト
 *  プレイヤー名、合計、時間、日時を取り扱うオブジェクト
 */
@Getter
@Setter
public class PlayerScore {
  private String playerName;
  private int score ;
  private int gameTime;
}

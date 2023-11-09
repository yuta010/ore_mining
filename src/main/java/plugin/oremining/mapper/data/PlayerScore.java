package plugin.oremining.mapper.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * プレイヤーのスコア情報を扱うオブジェクトです。
 * DBに存在するテーブルと連動する。
 */
@Getter
@Setter
@NoArgsConstructor
public class PlayerScore {

  private int id;
  private String name;
  private int score;
  private LocalDateTime registeredAt;

  public PlayerScore(String name, int score){
    this.name = name;
    this.score = score;
  }
}

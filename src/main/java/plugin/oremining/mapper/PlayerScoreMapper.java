package plugin.oremining.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import plugin.oremining.mapper.data.PlayerScore;

public interface PlayerScoreMapper {

  @Select("select * from player_score order by score desc limit 3")
  List<PlayerScore> selectList();
  @Insert("insert player_score(player_name, score, registered_at) values(#{playerName},#{score},now())")
  int insert(PlayerScore playerScore);
}

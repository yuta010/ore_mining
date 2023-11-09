package plugin.oremining.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import plugin.oremining.mapper.data.PlayerScore;

import java.util.List;

public interface PlayerScoreMapper {

  @Select("select * from player_score order by score desc limit 3")
  List<PlayerScore> selectList();
  @Insert("insert player_score(name, score, registered_at) values(#{name},#{score},now())")
  int insert(PlayerScore playerScore);
}

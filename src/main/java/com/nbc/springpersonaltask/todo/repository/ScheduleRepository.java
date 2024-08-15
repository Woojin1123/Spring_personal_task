package com.nbc.springpersonaltask.todo.repository;

import com.nbc.springpersonaltask.todo.dto.ScheduleRequestDto;
import com.nbc.springpersonaltask.todo.dto.ScheduleResponseDto;
import com.nbc.springpersonaltask.todo.entity.Schedule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class ScheduleRepository { // DB접근 관련 기능 수행하는 클래스

  private final JdbcTemplate jdbcTemplate;

  public ScheduleRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Schedule save(Schedule schedule) {
    KeyHolder keyholder = new GeneratedKeyHolder();

    String sql = "INSERT INTO schedule (todo,manager_id,pwd,registerDate,updateDate) VALUES(?,?,?,?,?)";
    jdbcTemplate.update(con -> {
          PreparedStatement preparedStatement = con.prepareStatement(sql,
              Statement.RETURN_GENERATED_KEYS);

          preparedStatement.setString(1, schedule.getTodo());
          preparedStatement.setInt(2, schedule.getManagerId());
          preparedStatement.setString(3, schedule.getPwd());
          preparedStatement.setString(4, schedule.getRegisterDate());
          preparedStatement.setString(5, schedule.getUpdateDate());
          return preparedStatement;
        },
        keyholder);
    int id = keyholder.getKey()
        .intValue();
    schedule.setId(id);
    return schedule;
  }


  public List<ScheduleResponseDto> findAll(String managerName, String updateDate, int page,
      int pagesize) {
    String sql = "SELECT s.id, s.todo, s.registerDate, s.updateDate, m.id, m.name " +
        "FROM schedule s " +
        "JOIN manager m on m.id = s.manager_id WHERE true";
    List<String> param = new ArrayList<>();
    if (managerName != null) {
      sql = sql + " " + "AND m.manager_id = ?";
      param.add(managerName.toString());
    }
    if (updateDate != null) {
      sql = sql + " " + "AND s.updateDate LIKE ?";
      param.add(updateDate + "%");
    }
    sql += " ORDER BY s.updateDate DESC";
    sql += " LIMIT ? OFFSET ?";
    List<Integer> pageParam = new ArrayList<>();
    pageParam.add(pagesize);
    pageParam.add(page * pagesize);
    return jdbcTemplate.query(sql, new PreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps) throws SQLException {
        for (int i = 0; i < param.size(); i++) {
          ps.setString(i + 1, param.get(i));
        }
        for (int i = param.size(); i < (param.size() + pageParam.size()); i++) {
          ps.setInt(i + 1, pageParam.get(i - param.size()));
        }
      }
    }, new RowMapper<ScheduleResponseDto>() {
      @Override
      public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("s.id");
        String todo = rs.getString("s.todo");
        int managerId = rs.getInt("m.id");
        String registerDate = rs.getString("s.registerDate");
        String updateDate = rs.getString("s.updateDate");
        String managerName = rs.getString("m.name");
        return new ScheduleResponseDto(id, todo, managerId, registerDate, updateDate, managerName);
      }
    });
  }

  public Schedule findById(int id) {
    // DB 조회
    String sql = "SELECT * FROM schedule WHERE id = ?";

    return jdbcTemplate.query(sql, resultSet -> {
      if (resultSet.next()) {
        Schedule schedule = new Schedule();
        schedule.setId(resultSet.getInt("id"));
        schedule.setTodo(resultSet.getString("todo"));
        schedule.setManagerId(resultSet.getInt("manager_id"));
        schedule.setPwd(resultSet.getString("pwd"));
        schedule.setRegisterDate(resultSet.getString("registerDate"));
        schedule.setUpdateDate(resultSet.getString("updateDate"));
        return schedule;
      } else {
        return null;
      }
    }, id);
  }

  public void update(Schedule schedule, ScheduleRequestDto requestDto) {
    String sql = "UPDATE schedule SET manager_id = ?, todo = ? , updateDate = ? WHERE id = ?";
    jdbcTemplate.update(sql, requestDto.getManagerId(), requestDto.getTodo(), schedule.getUpdateDate(),
        schedule.getId());
  }

  public int delete(ScheduleRequestDto requestDto, Schedule schedule) {
      String sql = "DELETE FROM schedule WHERE id = ?";
      jdbcTemplate.update(sql, schedule.getId());
      return schedule.getId();
  }

  public void setManagerName(ScheduleResponseDto responseDto, int id) {
    responseDto.setManagerName(
        jdbcTemplate.queryForObject(
            "SELECT m.name FROM schedule s JOIN manager m on s.manager_id = m.id WHERE m.id = ? GROUP BY m.name",
            String.class, id));
  }

  public boolean managerExists(int managerId) {
    String sql = "SELECT COUNT(*) FROM manager WHERE id = ?";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, managerId);
    if (count >= 1) {
      return true;
    } else {
      return false;
    }
  }
}

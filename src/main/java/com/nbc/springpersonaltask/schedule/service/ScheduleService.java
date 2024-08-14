package com.nbc.springpersonaltask.schedule.service;

import com.nbc.springpersonaltask.schedule.dto.ScheduleRequestDto;
import com.nbc.springpersonaltask.schedule.dto.ScheduleResponseDto;
import com.nbc.springpersonaltask.schedule.entity.Schedule;
import com.nbc.springpersonaltask.schedule.util.util;
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

public class ScheduleService { // 서비스에서 repository를 이용해 기능 수행

  private final JdbcTemplate jdbcTemplate;

  public ScheduleService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
    ScheduleResponseDto responseDto;
    if (!managerExists(requestDto.getManagerId())) {
      throw new IllegalArgumentException("해당 manager가 존재하지 않습니다.");
    }
    ;
    if (requestDto.getPwd()
        .length() > 64) {
      throw new IllegalArgumentException("비밀번호가 너무 깁니다.");
    } else if (requestDto.getPwd()
        .length() < 8) {
      throw new IllegalArgumentException("비밀번호가 너무 짧습니다.");
    } else {
      Schedule schedule = new Schedule(requestDto);
      schedule.setUpdateDate(util.currentTime());
      schedule.setRegisterDate(util.currentTime());

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
      responseDto = new ScheduleResponseDto(schedule);
      setManagerName(responseDto, schedule.getManagerId());
    }

    return responseDto;
  }

  public List<ScheduleResponseDto> getSchedules(Integer managerId, String updateDate, int page,
      int pagesize) {
    String sql = "SELECT s.id, s.todo, s.registerDate, s.updateDate, m.id, m.name " +
        "FROM schedule s " +
        "JOIN manager m on m.id = s.manager_id WHERE true";
    List<String> param = new ArrayList<>();
    if (managerId != null) {
      sql = sql + " " + "AND s.manager_id = ?";
      param.add(managerId.toString());
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

  public ScheduleResponseDto choiceSchedule(int id) {
    Schedule schedule = findById(id);
    ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
    setManagerName(responseDto, schedule.getManagerId());
    return responseDto;
  }

  public ScheduleResponseDto updateSchedule(int id, ScheduleRequestDto requestDto) {
    Schedule schedule = findById(id);
    String time = util.currentTime();
    if (schedule == null) {
      throw new IllegalArgumentException("해당 ID의 일정이 존재하지 않습니다.");
    } else if (requestDto.getPwd()
        .equals(schedule.getPwd())) {
      String sql = "UPDATE schedule SET manager_id = ?, todo = ? , updateDate = ? WHERE id = ?";
      jdbcTemplate.update(sql, requestDto.getManagerId(), requestDto.getTodo(), time, id);
      schedule.setUpdateDate(time);
      ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
      setManagerName(responseDto, schedule.getManagerId());
      return responseDto;
    } else {
      throw new IllegalArgumentException("비밀번호가 틀립니다");
    }
  }

  public int deleteSchedule(int id, ScheduleRequestDto requestDto) {
    Schedule schedule = findById(id);
    if (schedule != null & requestDto.getPwd()
        .equals(schedule.getPwd())) {
      String sql = "DELETE FROM schedule WHERE id = ?";
      jdbcTemplate.update(sql, id);
      return id;
    } else {
      throw new IllegalArgumentException("해당 ID의 일정이 존재하지 않습니다.");
    }
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

  private void setManagerName(ScheduleResponseDto responseDto, int id) {
    responseDto.setManagerName(
        jdbcTemplate.queryForObject(
            "SELECT m.name FROM schedule s JOIN manager m on s.manager_id = m.id WHERE m.id = ? GROUP BY m.name",
            String.class, id));
  }

  private Schedule findById(int id) {
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
}

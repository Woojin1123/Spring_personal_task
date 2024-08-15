package com.nbc.springpersonaltask.todo.repository;

import com.nbc.springpersonaltask.todo.dto.ManagerResponseDto;
import com.nbc.springpersonaltask.todo.entity.Manager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class ManagerRepository {

  private final JdbcTemplate jdbcTemplate;

  public ManagerRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(Manager manager) {
    KeyHolder keyholder = new GeneratedKeyHolder();
    String sql = "INSERT INTO manager (name,email,registerDate,updateDate) SELECT ?,?,?,?    " +
        "WHERE NOT EXISTS" +
        "(SELECT 1 FROM manager WHERE name = ? AND email = ? )";
    if (jdbcTemplate.update(con -> {
          PreparedStatement preparedStatement = con.prepareStatement(sql,
              Statement.RETURN_GENERATED_KEYS);

          preparedStatement.setString(1, manager.getName());
          preparedStatement.setString(2, manager.getEmail());
          preparedStatement.setString(3, manager.getRegisterDate());
          preparedStatement.setString(4, manager.getUpdateDate());
          preparedStatement.setString(5, manager.getName());
          preparedStatement.setString(6, manager.getEmail());
          return preparedStatement;
        },
        keyholder) == 1) { // jdbctemplate.update()는 영향받은 행의 갯수를 반환하므로 입력에 성공하면 responseDto 반환 수행
      int id = keyholder.getKey()
          .intValue();
      manager.setId(id);
    } else {
      throw new RuntimeException("동일인물이 이미 존재합니다.");
    }

  }

  public List<ManagerResponseDto> findAll() {
    String sql = "SELECT * FROM manager WHERE true";
    return jdbcTemplate.query(sql, new RowMapper<ManagerResponseDto>() {
      @Override
      public ManagerResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String registerDate = rs.getString("registerDate");
        String updateDate = rs.getString("updateDate");
        return new ManagerResponseDto(id, name, email, registerDate, updateDate);
      }
    });
  }


  public ManagerResponseDto findById(int id) {
    String sql = "SELECT * FROM manager WHERE true";
    sql += " AND id = ?";
    return jdbcTemplate.queryForObject(sql, new RowMapper<ManagerResponseDto>() {
      @Override
      public ManagerResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String registerDate = rs.getString("registerDate");
        String updateDate = rs.getString("updateDate");
        return new ManagerResponseDto(id, name, email, registerDate, updateDate);
      }
    }, id);
  }
}

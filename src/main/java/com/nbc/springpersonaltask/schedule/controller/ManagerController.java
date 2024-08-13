package com.nbc.springpersonaltask.schedule.controller;

import com.nbc.springpersonaltask.schedule.dto.ManagerRequestDto;
import com.nbc.springpersonaltask.schedule.dto.ManagerResponseDto;
import com.nbc.springpersonaltask.schedule.entity.Manager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.nbc.springpersonaltask.schedule.controller.ScheduleController.currentTime;

@RestController
@RequestMapping("/todolist")
public class ManagerController {
    private final JdbcTemplate jdbcTemplate;//spring의 bean에 등록되어있어 직접 객체를 생성하지 않아도 스프링 컨테이너에 의해 관리됨

    public ManagerController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/managers")
    public ManagerResponseDto createManager(@RequestBody ManagerRequestDto requestDto) {
        ManagerResponseDto responseDto;
        Manager manager = new Manager(requestDto);
        manager.setUpdateDate(currentTime());
        manager.setRegisterDate(currentTime());
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
            int id = keyholder.getKey().intValue();
            manager.setId(id);

            responseDto = new ManagerResponseDto(manager);
            return responseDto;
        } else {
            throw new RuntimeException("동일인물이 이미 존재합니다.");
        }
    }

    @GetMapping("/managers")
    public List<ManagerResponseDto> getManagers(@RequestParam(name = "managerId", required = false) Integer managerId) {
        String sql = "SELECT * FROM manager WHERE true";
        if (managerId != null) {
            sql += " AND id = ?";
            try {
                return List.of(jdbcTemplate.queryForObject(sql, new RowMapper<ManagerResponseDto>() {
                    @Override
                    public ManagerResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        String registerDate = rs.getString("registerDate");
                        String updateDate = rs.getString("updateDate");
                        return new ManagerResponseDto(id, name, email, registerDate, updateDate);
                    }
                }, managerId));
            }catch(EmptyResultDataAccessException e){
                return null; // 나중에 예외처리 구현
            }
        } else {
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
    }
}

package com.nbc.springpersonaltask.schedule.controller;

import com.nbc.springpersonaltask.schedule.dto.ScheduleRequestDto;
import com.nbc.springpersonaltask.schedule.dto.ScheduleResponseDto;
import com.nbc.springpersonaltask.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/schedule/api")
public class ScheduleController {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostMapping("/schedules") //create니까 Post로 전송
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {

        Schedule schedule = new Schedule(requestDto);

        KeyHolder keyholder = new GeneratedKeyHolder();

        String sql = "INSERT INTO schedule (todo,manager,pwd,registerDate,updateDate) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getTodo());
                    preparedStatement.setString(2, schedule.getManager());
                    preparedStatement.setString(3, schedule.getPwd());
                    preparedStatement.setString(4, schedule.getRegisterDate());
                    preparedStatement.setString(5, schedule.getUpdateDate());
                    return preparedStatement;
                },
                keyholder);

        int id = keyholder.getKey().intValue();
        schedule.setId(id);

        ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);

        return responseDto;
    }
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getSchedules(@RequestParam(name = "manager",required = false) String manager,@RequestParam(name = "updateDate",required = false) String updateDate){
        String sql = "SELECT id,todo,manager,registerDate,updateDate FROM schedule WHERE true";
        List<String> param = new ArrayList<>();
        if(manager != null){
            sql = sql + " " + "AND manager = ?";
            param.add(manager);
        }
        if(updateDate != null){
            sql = sql + " " + "AND updateDate LIKE ?";
            param.add(updateDate+"%");
        }

        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                for(int i = 0; i<param.size(); i++){
                    ps.setString(i+1,param.get(i));
                }
            }
        }, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("id");
                String todo = rs.getString("todo");
                String manager = rs.getString("manager");
                String registerDate = rs.getString("registerDate");
                String updateDate = rs.getString("updateDate");
                return new ScheduleResponseDto(id, todo, manager, registerDate, updateDate);
            }
        });
    }
}

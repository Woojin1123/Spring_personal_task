package com.nbc.springpersonaltask.schedule.controller;

import com.nbc.springpersonaltask.schedule.dto.ScheduleRequestDto;
import com.nbc.springpersonaltask.schedule.dto.ScheduleResponseDto;
import com.nbc.springpersonaltask.schedule.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/todolist")
public class ScheduleController {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostMapping("/schedules") //create니까 Post로 전송
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        ResponseEntity responseEntity;
        ScheduleResponseDto responseDto;
        if(!managerExists(requestDto.getManagerId())){
            throw new IllegalArgumentException("해당 manager가 존재하지 않습니다.");
        };
        if(requestDto.getPwd().length()>64){
            throw new IllegalArgumentException("비밀번호가 너무 깁니다.");
        } else if (requestDto.getPwd().length() < 8) {
            throw new IllegalArgumentException("비밀번호가 너무 짧습니다.");
        }else {
            Schedule schedule = new Schedule(requestDto);
            schedule.setUpdateDate(currentTime());
            schedule.setRegisterDate(currentTime());

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

            int id = keyholder.getKey().intValue();
            schedule.setId(id);

            responseDto = new ScheduleResponseDto(schedule);
            responseEntity = new ResponseEntity(HttpStatus.CREATED);
        }

        return responseDto;
    }

    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getSchedules(@RequestParam(name = "manager", required = false) String manager, @RequestParam(name = "updateDate", required = false) String updateDate) {
        String sql = "SELECT id,todo,manager,registerDate,updateDate FROM schedule WHERE true";
        List<String> param = new ArrayList<>();
        if (manager != null) {
            sql = sql + " " + "AND manager = ?";
            param.add(manager);
        }
        if (updateDate != null) {
            sql = sql + " " + "AND updateDate LIKE ?";
            param.add(updateDate + "%");
        }

        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                for (int i = 0; i < param.size(); i++) {
                    ps.setString(i + 1, param.get(i));
                }
            }
        }, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("id");
                String todo = rs.getString("todo");
                int managerId = rs.getInt("manager_id");
                String registerDate = rs.getString("registerDate");
                String updateDate = rs.getString("updateDate");
                return new ScheduleResponseDto(id, todo, managerId, registerDate, updateDate);
            }
        });
    }

    @PutMapping("/schedules/{id}")
    public ScheduleResponseDto updateSchedule(@PathVariable int id, @RequestBody ScheduleRequestDto requestDto) {
        Schedule schedule = findById(id);
        if (schedule != null & requestDto.getPwd().equals(schedule.getPwd())) {
            String sql = "UPDATE schedule SET manager_id = ?, todo = ? , updateDate = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getManagerId(), requestDto.getTodo(), currentTime(), id);
            schedule.setUpdateDate(currentTime());
            return new ScheduleResponseDto(schedule);
        } else {
            throw new IllegalArgumentException("해당 ID의 일정이 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/schedules/{id}")
    public int deleteSchedule(@PathVariable int id, @RequestBody ScheduleRequestDto requestDto) {
        Schedule schedule = findById(id);
        if (schedule != null & requestDto.getPwd().equals(schedule.getPwd())) {
            String sql = "DELETE FROM schedule WHERE id = ?";
            jdbcTemplate.update(sql,id);
            return id;
        }else{
            throw new IllegalArgumentException("해당 ID의 일정이 존재하지 않습니다.");
        }
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
    public boolean managerExists(int managerId){
        String sql = "SELECT COUNT(*) FROM manager WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class , managerId);
        System.out.println(count.intValue());
        if(count >= 1){
            return true;
        }else {
            return false;
        }
    }

    public static String currentTime() {
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(today);
    }
}

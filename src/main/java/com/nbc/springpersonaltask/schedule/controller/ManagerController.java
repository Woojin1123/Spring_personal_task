package com.nbc.springpersonaltask.schedule.controller;

import com.nbc.springpersonaltask.schedule.dto.ManagerRequestDto;
import com.nbc.springpersonaltask.schedule.dto.ManagerResponseDto;
import com.nbc.springpersonaltask.schedule.entity.Manager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.Statement;

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
                keyholder) == 1) {
            int id = keyholder.getKey().intValue();
            manager.setId(id);

            responseDto = new ManagerResponseDto(manager);
            return responseDto;
        } else {
            throw new RuntimeException("동일인물이 이미 존재합니다.");
        }
    }

    @GetMapping("/managers")
    public ManagerResponseDto getManagers(){
        return null;
    }
}

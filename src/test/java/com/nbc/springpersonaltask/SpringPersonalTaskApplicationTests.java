package com.nbc.springpersonaltask;

import com.nbc.springpersonaltask.todo.util.util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringPersonalTaskApplicationTests {

    @Test
    void contextLoads() {
        String time = util.currentTime();
    }

}

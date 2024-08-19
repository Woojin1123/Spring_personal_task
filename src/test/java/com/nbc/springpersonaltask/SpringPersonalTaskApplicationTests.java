package com.nbc.springpersonaltask;

import com.nbc.springpersonaltask.todo.util.Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringPersonalTaskApplicationTests {

    @Test
    void contextLoads() {
        String time = Util.currentTime();
    }

}

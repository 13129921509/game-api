package com.cai

import com.cai.api.csgo.job.service.ApiService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner)
class ApplicationTests {
    @Autowired
    ApiService jobSvc

    @Test
    void test(){
        jobSvc.refresh()
    }

}

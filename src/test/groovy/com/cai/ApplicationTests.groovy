package com.cai

import com.cai.api.GameApiApplication
import com.cai.api.csgo.job.service.TeamRankService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.testng.Assert

import java.util.stream.IntStream

@SpringBootTest(classes = GameApiApplication.class)
@RunWith(SpringJUnit4ClassRunner)
class ApplicationTests {
    @Autowired
    TeamRankService jobSvc

    @Test
    void test(){
        jobSvc.refresh()
    }

    @Test
    void inputTest(){
        int count = IntStream.builder().add(1).add(2).add(0).add(122).build().count()
        Assert.assertEquals(count, 4)
    }

}

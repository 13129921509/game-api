package com.cai

import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.testng.Assert

import java.util.stream.DoubleStream
import java.util.stream.IntStream

@RunWith(SpringJUnit4ClassRunner)
class StreamTest {
    Logger logger = LoggerFactory.getLogger(StreamTest.class)

    @Test
    void inputTest(){
        int count = IntStream.builder().add(1).add(2).add(0).add(122).build().count() //构造流
        Assert.assertEquals(count, 4)
        double init = 1L
        double value = DoubleStream
                .generate{
                    init*=2
                    return init
                }
                .limit(100)
                .sum() // 无限流
        Assert.assertEquals(value, 2.5353012004564588E30)
        // 这里根据上游的元素扩展出了更多的元素
        IntStream.of(1, 2, 3)
                .flatMap{e->
                    IntStream.rangeClosed(0, e)
                }
                .sorted()
                .forEach{
                    println it
                }

        // 在并行遍历时，forEachOrdered将顺序遍历元素
        IntStream.of(1,5,-9,0,-5,2,5,8).parallel().forEachOrdered{
            logger.error("value : $it")
        };
        int sum = IntStream.range(0, 10).reduce(0, {v1,v2->
            v1 + v2
        });
        sum = IntStream.range(0, 10).reduce({v1,v2->
            v1 + v2
        }).orElse(0)
        println sum
    }
}

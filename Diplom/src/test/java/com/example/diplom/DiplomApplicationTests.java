package com.example.diplom;


import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiplomApplicationTests {

    @Test
    public void test(){
        int x=56;
        int y = 78;
        Assertions.assertEquals(134, x+y);
    }
}

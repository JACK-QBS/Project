package org.example.util;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

public class DBUtilTest {
    @Test
    public void test(){
        Connection c = DBUtil.getConnection();
        Assert.assertNotNull(c);
    }
}

package com.monji.ai_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.TimeZone;

public class ConnectionTest {

    public static void main(String[] args) throws Exception {

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

        System.out.println("JVM Timezone = " + TimeZone.getDefault().getID());

        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5434/pgvector-test",
                "postgres",
                "root"
        );

        System.out.println("Connected = " + con.isValid(2));
    }
}
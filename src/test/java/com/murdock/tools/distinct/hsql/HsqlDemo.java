package com.murdock.tools.distinct.hsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author weipeng 2012-12-21 ÏÂÎç2:23:03
 */
public class HsqlDemo {
    public static void main(String[] args) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:/mydb", "sa", "");
            if (c != null) {
                System.out.println("Connected db success!");
                String sql = "CREATE TABLE TBL_USERS(ID INTEGER, NAME VARCHAR(16), BIRTHDAY DATE);";
                Statement st = c.createStatement();
                st.execute(sql);
                sql = "INSERT INTO TBL_USERS(ID, NAME, BIRTHDAY) VALUES ('1', 'ADMIN', SYSDATE);";
                st.executeUpdate(sql);
                
                
                sql = "SELECT * FROM TBL_USERS;";
                
                ResultSet rs = st.executeQuery(sql);
                
                while (rs.next()) {
                    System.out.println(rs.getObject("ID"));
                    System.out.println(rs.getObject("NAME"));
                    System.out.println(rs.getObject("BIRTHDAY"));
                }
                
                if (st != null) {
                    st.close();
                }
                c.close();
            }
        } catch(Exception e) {
            System.out.println("ERROR:failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }
    }   
}

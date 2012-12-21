package com.murdock.tools.distinct.hsql;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.murdock.tools.distinct.db.MatchDB;

@Ignore
@ContextConfiguration(locations = { "classpath:test-applicationContext.xml" })
public class DBCreatorTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MatchDB matchDB;

    @Test
    public void create() {
        System.out.println(jdbcTemplate);

        jdbcTemplate.update("insert into project_name(name) values('kakkakaka') ");

        System.out.println(jdbcTemplate.queryForMap("select * from project_name", new Object[] {}));
    }
    
    @Test
    public void insert() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("" + i);
        }
        matchDB.insert(sb.toString(), "sdfsdfsdf");
        System.out.println(jdbcTemplate.queryForMap("select * from method_content", new Object[] {}));
        System.out.println(matchDB.dbsize());
    }
    
    @Test
    public void project() {
        System.out.println(matchDB.getProjectNames());
        matchDB.addProject("xxx");
        matchDB.addProject("xxx");
        matchDB.addProject("xxc");
        System.out.println(matchDB.getProjectNames());
        
        matchDB.clean();
        
        System.out.println(matchDB.getProjectNames());
    }
    
    @Test
    public void match() {
        matchDB.insert("aaaaa.2(aaa", "1");
        matchDB.insert("bbbbb.1(bbb", "2");
        matchDB.insert("bbbbb.3(bbc", "3");
        
        System.out.println(matchDB.match("1"));
        System.out.println(matchDB.match("2"));
        System.out.println(matchDB.match("3"));
    }

}

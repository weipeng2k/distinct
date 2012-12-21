package com.murdock.tools.distinct.db.hsql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.murdock.tools.distinct.config.Constant;
import com.murdock.tools.distinct.db.MatchDB;

/**
 * @author weipeng 2012-12-21 下午3:55:55
 */
public class MatchDBImpl extends JdbcDaoSupport implements MatchDB {

    @Override
    public boolean insert(String row) {
        return insert(row, "1");
    }

    @Override
    public boolean insert(String row, String value) {
        if (row != null && !"".equals(row)) {

            try {
                getJdbcTemplate().update("INSERT INTO " + Constant.METHOD_CONTENT_TABLE
                                                 + "(METHOD_BODY, METHOD_NAME) values (?,?)",
                                         new Object[] { row, value });
            } catch (DataIntegrityViolationException ex) {
            }

            return true;
        }

        return false;
    }

    @Override
    public void addProject(String projectName) {
        if (projectName != null) {
            try {
                getJdbcTemplate().update("INSERT INTO " + Constant.PROJECT_NAME_TABLE + "(NAME) values (?)",
                                         new Object[] { projectName });
            } catch (DataIntegrityViolationException ex) {
            }
        }
    }

    @Override
    public List<String> getProjectNames() {

        List<Map<String, Object>> result = getJdbcTemplate().queryForList("SELECT * FROM "
                                                                                  + Constant.PROJECT_NAME_TABLE + ";");

        if (result != null && !result.isEmpty()) {
            List<String> resultList = new ArrayList<String>();
            for (Map<String, Object> item : result) {
                resultList.add(item.get("name").toString());
            }

            return resultList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public int match(String sample) {
        int result = 0;

        if (sample != null && !"".equals(sample)) {
            String pattern = "%" + sample + "(%";

            result = getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM " + Constant.METHOD_CONTENT_TABLE
                                                           + " WHERE METHOD_BODY LIKE '" + pattern + "';");

            // 只有一个关联，需要看一下是不是自己
            if (result == 1) {
                String value = getJdbcTemplate().queryForObject("SELECT METHOD_NAME FROM " + Constant.METHOD_CONTENT_TABLE
                                                                + " WHERE METHOD_BODY LIKE '" + pattern + "';", String.class);

                // 是自己
                if (sample.equals(value)) {
                    result = 0;
                }
            }
        }

        return result;
    }

    @Override
    public int onlineUsers() {
        return 0;
    }

    @Override
    public boolean clean() {
        getJdbcTemplate().update("DELETE FROM " + Constant.PROJECT_NAME_TABLE);
        getJdbcTemplate().update("DELETE FROM " + Constant.METHOD_CONTENT_TABLE);

        return true;
    }

    @Override
    public long dbsize() {
        return getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM " + Constant.METHOD_CONTENT_TABLE);
    }

}

package org.example;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class Sql<T> {
    private StringBuilder query;
    private List<Object> params;

    private SimpleDb simpleDb;

    public Sql(SimpleDb simpleDb) {
        this.simpleDb = simpleDb;
        this.query = new StringBuilder();
        this.params = new ArrayList<>();
    }

    public Sql append(String query) {
        this.query.append(query).append(" ");
        return this;
    }

    public Sql append(String query, Object... args) {
        this.query.append(query).append(" ");
        Collections.addAll(params, args);
        return this;
    }

    public long insert() {
        return simpleDb.runInsertAndGetPK(query.toString(), params.toArray());
    }

    public long update() {
        return simpleDb.runAndGetEffectedNum(query.toString(), params.toArray());
    }

    public long delete() {
        return simpleDb.runAndGetEffectedNum(query.toString(), params.toArray());
    }

    public LocalDateTime selectDatetime() {
        try {
            return simpleDb.runAndGetResult(query.toString(), params.toArray()).getTimestamp(1).toLocalDateTime();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long selectLong() {
        try {
            return simpleDb.runAndGetResult(query.toString(), params.toArray()).getLong(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String selectString() {
        try {
            return simpleDb.runAndGetResult(query.toString(), params.toArray()).getString(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ResultSet -> Map<String, Object> 변환 함수
    public Map<String, Object> convertRsToMap(ResultSet rs) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        ResultSetMetaData rsMeta = rs.getMetaData();
        int rsNum = rsMeta.getColumnCount();

        for (int i = 0; i < rsNum; i++) {
            String columnName = rsMeta.getColumnName(i + 1);
            Object value = rs.getObject(columnName);

            // Timestamp인 경우 LocalDateTime으로 컨버팅
            if (value.getClass().equals(Timestamp.class)) {
                map.put(columnName, ((Timestamp) value).toLocalDateTime());
            }
            else {
                map.put(columnName, rs.getObject(columnName));
            }
        }

        return map;
    }

    public Map<String, Object> selectRow() {
        try {
            return convertRsToMap(simpleDb.runAndGetResult(query.toString(), params.toArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
    public Sql appendIn(String query, List<> args) {
        // TODO: query의 ? 안에 List 내부 삽입
    }

    
    public List<Long> selectLongs() {
        // TODO: SELECT 결과 List<Long>으로 반환
    }

    public T selectRow(Class<T> objectClass) {
        // TODO: SELECT 결과 T로 반환
    }

    public List<T> selectRows(Class<T> objectClass) {
        // TODO: SELECT 결과 List<T>로 반환
    }

     */
}

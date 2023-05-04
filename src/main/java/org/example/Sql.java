package org.example;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /*
    public Sql appendIn(String query, List<> args) {
        // TODO: query의 ? 안에 List 내부 삽입
    }

    
    public List<Long> selectLongs() {
        // TODO: SELECT 결과 List<Long>으로 반환
    }
    
    public String selectString() {
        // TODO: SELECT 결과 String으로 반환
    }

    public Map<String, Object> selectRow() {
        // TODO: SELECT 결과 Map<String,Object> 형태로 반환
    }

    public T selectRow(Class<T> objectClass) {
        // TODO: SELECT 결과 T로 반환
    }

    public List<T> selectRows(Class<T> objectClass) {
        // TODO: SELECT 결과 List<T>로 반환
    }

     */
}

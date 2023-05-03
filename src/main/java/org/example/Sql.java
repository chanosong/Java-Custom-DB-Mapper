package org.example;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    /*
    public Sql appendIn(String query, List<> args) {
        // TODO: query의 ? 안에 List 내부 삽입
    }
    
    public long update() {
        // TODO: 수정된 row 개수 반환
    }
    
    public long delete() {
        // TODO: 삭제된 row 개수 반환
    }
    
    public LocalDateTime selectDatetime() {
        // TODO: 실행된 시각 반환
    }
    
    public long selectLong() {
        // TODO: SELECT 결과 Long으로 반환
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

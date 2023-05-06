package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    public <T> T selectRow(Class<T> classObject) {
        // FIXME: 리플렉션 제대로 적용되지 않는 문제
        try {
            T obj = classObject.getDeclaredConstructor().newInstance();

            Map<String, Object> map = convertRsToMap(simpleDb.runAndGetResult(query.toString(), params.toArray()));

            for (Field field: classObject.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = map.get(field.getName());

                // value가 null일 경우에는 해당 필드에 값을 설정하지 않음
                if (value != null) {
                    String fieldName = field.getName();
                    Method method = classObject.getDeclaredMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
                    method.invoke(obj, value);
                }
            }

            return obj;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Sql appendIn(String query, List<T> args) {
        StringJoiner sj = new StringJoiner(",");
        
        // query의 QuestionMark 개수 변경, params 삽입
        for (T arg : args) {
            sj.add("?");
            params.add(arg);
        }

        // query 변경
        query = query.replace("?", sj.toString());
        this.query.append(query).append(" ");

        return this;
    }


    public List<Long> selectLongs() {
        try {
            return simpleDb.runAndGetResultByList(query.toString(), params.toArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
    public T selectRow(Class<T> objectClass) {
        // TODO: SELECT 결과 T로 반환
    }

    public List<T> selectRows(Class<T> objectClass) {
        // TODO: SELECT 결과 List<T>로 반환
    }

     */
}

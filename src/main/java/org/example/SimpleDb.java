package org.example;

import java.sql.Connection;

public class SimpleDb {
    private boolean devMode;
    private Connection connection;

    // 생성자
    public SimpleDb(String host, String user, String password, String dbName) {

    }

    // 개발 모드 설정
    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void run(String query, Object... args) {
        // TODO: query 실행
    }
    
    public Sql genSql() {
        // TODO: Sql 객체 생성 및 반환
    }


    
    
}

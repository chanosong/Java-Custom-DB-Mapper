package org.example;

import java.sql.*;

public class SimpleDb {
    private boolean devMode;
    private Connection connection;

    // 생성자
    public SimpleDb(String host, String user, String password, String dbName) {
        int port = 3306;

        String url = "jdbc:mariadb://" + host + ":" + port + "/" + dbName
                + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 개발 모드 설정
    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void run(String query, Object... args) {
        try {
            // PreparedStatement 생성
            PreparedStatement psmt = connection.prepareStatement(query);

            // 인자 넣기
            for (int i = 0; i < args.length; i++) {
                psmt.setObject(i + 1, args[i]);
            }

            // 실행
            psmt.executeUpdate();
            //System.out.println("쿼리 실행 완료");
        } catch (Exception e) {
            System.out.println("오류 발생");
            e.printStackTrace();
        }
    }

    public long runInsertAndGetPK(String query, Object... args) {
        long pk = -1L;

        try {
            // PreparedStatement 생성
            PreparedStatement psmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // 인자 넣기
            for (int i = 0; i < args.length; i++) {
                psmt.setObject(i + 1, args[i]);
            }

            // 실행
            psmt.executeUpdate();

            // get PK
            ResultSet rs = psmt.getGeneratedKeys();

            if (rs.next()) {
                pk = rs.getLong(1);
            }
            else {
                System.out.println("생성된 PK가 없음");
            }

            // System.out.println("쿼리 실행 완료");
        } catch (Exception e) {
            System.out.println("오류 발생");
            e.printStackTrace();
        }

        return pk;
    }

    public long runAndGetEffectedNum(String query, Object... args) {
        long updatedRow = 0L;

        try {
            // PreparedStatement 설정
            PreparedStatement psmt = connection.prepareStatement(query);

            // 인자 넣기
            for (int i = 0; i < args.length; i++) {
                psmt.setObject(i + 1, args[i]);
            }

            updatedRow = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update 중 오류 발생");
            e.printStackTrace();
        }

        return updatedRow;
    }

    public Sql genSql() {
        return new Sql(this);
    }

}

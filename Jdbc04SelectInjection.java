package jdbc04;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import config.ConnectionFactory;

/**
 * ⚠ 危険な例:SQLインジェクション脆弱性あり
 * 絶対に本番コードで真似しないこと
 */
public class Jdbc04SelectInjection {

    public static void main(String[] args) {
        // ユーザ入力(例:「検索したい社員名」)
        System.out.print("社員名を入力してください: ");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        // 文字列連結でSQLを組み立てている = インジェクションの穴
        // 例1: userInputを、 "King' OR '1'='1" とすると全件検索されてしまう
        // 例2: ' OR SALARY > 2000 -- とすると、--はコメントとなり後のSQLを無効にする。給与が2000以上の社員が全て表示されてしまう
        // 例3: ' UNION SELECT ... FROM USER_TABLES -- とすると、別テーブルの情報を盗み出すことも可能
        String sql =
                "SELECT EMPLOYEE_ID, LAST_NAME "
                + "FROM EMPLOYEES "
                + "WHERE LAST_NAME = '" + userInput + "' "
                + "ORDER BY EMPLOYEE_ID";

        System.out.println("[実行されるSQL] " + sql);
        System.out.println("----------------------------------------");

        try (Connection connection = ConnectionFactory.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int memberId = resultSet.getInt("EMPLOYEE_ID");
                String name = resultSet.getString("LAST_NAME");

                System.out.printf(
						"EMP_NO=%d, NAME=%s%n",
						memberId, name);
            }
        } catch (SQLException e) {
            System.out.println("SELECT 実行時にエラーが発生しました");
            System.out.println(e.getMessage());
        }
    scanner.close();
    }
}
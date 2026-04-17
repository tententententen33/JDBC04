package jdbc04;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.ConnectionFactory;


public class Jdbc04WhereSafe {

	public static void main(String[] args) {
        int targetId = 111;
        String sql =
        		"SELECT LAST_NAME FROM EMPLOYEES WHERE EMPLOYEE_ID = ?";

        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, targetId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.printf("EMPNO=%d, ENAME=%s%n", targetId, resultSet.getString("LAST_NAME"));
                }
            }
        } catch (SQLException e) {
            System.out.println("検索時にエラーが発生しました");
            System.out.println(e.getMessage());
        }
    }

}

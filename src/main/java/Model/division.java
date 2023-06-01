package Model;

import DAO.JDBC;

import java.sql.*;

public class division {

    public static Integer getDivision(String division) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBC.openConnection().getConnection();
            String query = "SELECT * FROM first_level_divisions WHERE Division = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, division);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Division_ID");
            } else {
                throw new SQLException("No rows found for division: " + division);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
}
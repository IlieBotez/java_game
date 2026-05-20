package PaooGame;

import java.sql.*;
import java.util.ArrayList;

public class LeaderBoardManager {
    private static final String DB_URL = "jdbc:sqlite:leaderboard.db";
    public LeaderBoardManager() {
        initDatabase();
    }
    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // 4 colomn table
            String sql = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nume TEXT NOT NULL," +
                    "scor INTEGER NOT NULL," +
                    "timp INTEGER NOT NULL);";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addScore(String name, int score, int time) {
        String sql = "INSERT INTO leaderboard(nume, scor, timp) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.setInt(3, time);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String[]> getTopScores() {
        ArrayList<String[]> results = new ArrayList<>();

        // descending order for score and ascending order for time
        String sql = "SELECT nume, scor, timp FROM leaderboard " +
                "ORDER BY scor DESC, timp ASC " +
                "LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int rank = 1;
            while (rs.next()) {
                results.add(new String[]{
                        String.valueOf(rank++),
                        rs.getString("nume"),
                        String.valueOf(rs.getInt("scor")),
                        formatTime(rs.getInt("timp"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public String formatTime(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}
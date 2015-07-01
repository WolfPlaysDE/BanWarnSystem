package de.wolfplays.banwarnsystem.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by WolfPlaysDE On 03.04.2015 at 01:11:12
 */
public class MySQLWarnManager {

  public static int maxwarns;

  private static ExecutorService executor;

  static {
    executor = Executors.newCachedThreadPool();
  }

  // Get the warns from a Player
  public static int getWarns(String uuid) {
    try {
      String qry = "SELECT * FROM " + MySQL.TABEL_WARNEDPLAYERS + " WHERE uuid = ?";
      PreparedStatement stmt = MySQL.con.prepareStatement(qry);
      stmt.setString(1, uuid);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        return rs.getInt("warns");
      }
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  // Add Player
  public static void addPlayer(String playername, String uuid) {
    executor.execute(new Runnable() {
      @Override
      public void run() {
        String qry =
            "INSERT INTO " + MySQL.TABEL_WARNEDPLAYERS
                + " (playername, uuid, warns) VALUES (?, ?, ?)";
        try {
          PreparedStatement stmt = MySQL.con.prepareStatement(qry);
          stmt.setString(1, playername);
          stmt.setString(2, uuid);
          stmt.setInt(3, 0);
          stmt.executeUpdate();
          stmt.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    });
  }

  // Delete a Player
  public static void deletePlayer(String uuid) {
    executor.execute(new Runnable() {
      @Override
      public void run() {
        String qry = "DELETE FROM " + MySQL.TABEL_WARNEDPLAYERS + " WHERE uuid = ?";
        try {
          PreparedStatement stmt = MySQL.con.prepareStatement(qry);
          stmt.setString(1, uuid);
          stmt.executeUpdate();
          stmt.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    });
  }

  // Add warns to a Player
  public static void addWarn(String uuid, int warns) {
    executor.execute(new Runnable() {
      @Override
      public void run() {
        int nowwarns = MySQLWarnManager.getWarns(uuid);
        nowwarns += warns;
        String qry = "UPDATE " + MySQL.TABEL_WARNEDPLAYERS + " SET warns = ? WHERE uuid = ?";
        try {
          PreparedStatement stmt = MySQL.con.prepareStatement(qry);
          stmt.setInt(1, nowwarns);
          stmt.setString(2, uuid);
          stmt.executeUpdate();
          stmt.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  // Delete warns from a Player
  public static void deleteWarns(String uuid, int warns) {
    executor.execute(new Runnable() {
      @Override
      public void run() {
        int nowwarns = MySQLWarnManager.getWarns(uuid);
        nowwarns -= warns;
        String qry = "UPDATE " + MySQL.TABEL_WARNEDPLAYERS + " SET warns = ? WHERE uuid = ?";
        try {
          PreparedStatement stmt = MySQL.con.prepareStatement(qry);
          stmt.setInt(1, nowwarns);
          stmt.setString(2, uuid);
          stmt.executeUpdate();
          stmt.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  // Check player in tabel
  public static boolean isInTabel(String uuid) {
    boolean returnStatement = false;
    try {
      String qry = "SELECT * FROM " + MySQL.TABEL_WARNEDPLAYERS + " WHERE uuid = ?";
      PreparedStatement stmt = MySQL.con.prepareStatement(qry);
      stmt.setString(1, uuid);
      ResultSet rs = stmt.executeQuery();
      if (rs.first())
        returnStatement = true;
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return returnStatement;
  }

}

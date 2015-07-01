package de.wolfplays.banwarnsystem.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WolfPlaysDE On 30.03.2015 at 05:24:23
 */
public enum BanUnit {

  SECOND("Sekunde(n)", 1, "sec"), MINUTE("Minute(n)", 60, "min"), HOUR("Stunde(n)", 60 * 60, "hour"), DAY(
      "Tag(e)", 24 * 60 * 60, "day"), WEEK("Woche(n)", 7 * 24 * 60 * 60, "week");

  private String name;
  private int toSecond;
  private String shortcut;

  private BanUnit(String name, int toSecond, String shortcut) {
    this.name = name;
    this.toSecond = toSecond;
    this.shortcut = shortcut;
  }

  public String getName() {
    return name;
  }

  public int getToSecond() {
    return toSecond;
  }

  public String getShortcut() {
    return shortcut;
  }

  public static List<String> getUnitsAsString() {
    List<String> units = new ArrayList<String>();
    for (BanUnit unit : BanUnit.values()) {
      units.add(unit.getShortcut());
    }
    return units;
  }

  public static BanUnit getUnit(String unit) {
    for (BanUnit units : BanUnit.values()) {
      if (units.getShortcut().toLowerCase().equals(unit.toLowerCase())) {
        return units;
      }
    }
    return null;
  }

}

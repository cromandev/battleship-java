package org.scrum.psd.battleship.ascii;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class MainEndToEndTest {
  @ClassRule
  public static final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
  @ClassRule
  public static final TextFromStandardInputStream gameInput = emptyStandardInputStream();

  public static List<Position> playerPositions = Arrays.asList(
      new Position(Letter.A, 1),
      new Position(Letter.A, 2),
      new Position(Letter.A, 3),
      new Position(Letter.A, 4),
      new Position(Letter.A, 5),
      new Position(Letter.B, 1),
      new Position(Letter.B, 2),
      new Position(Letter.B, 3),
      new Position(Letter.B, 4),
      new Position(Letter.C, 1),
      new Position(Letter.C, 2),
      new Position(Letter.C, 3),
      new Position(Letter.D, 1),
      new Position(Letter.D, 2),
      new Position(Letter.D, 3),
      new Position(Letter.E, 1),
      new Position(Letter.E, 2)
  );

  public static List<String> positionToString(List<Position> positions) {
    return positions.stream().map(position -> position.getColumn().name() + position.getRow()).collect(Collectors.toList());
  }
  public static List<String> getPlayerPositionString() {
    return positionToString(playerPositions);
  }
  @Test
  public void testPlayGameShotHits() {
    try {
      List<String> inputLines = getPlayerPositionString();

      inputLines.add("b4") ;

      gameInput.provideLines(inputLines.toArray(new String[0]));

      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
      Assert.assertTrue(systemOutRule.getLog().contains("Yeah ! Nice hit !"));
    }
  }

  @Test
  public void testPlayGameShotMisses() {
    try {
      List<String> inputLines = getPlayerPositionString();

      inputLines.add("f4");
      gameInput.provideLines(inputLines.toArray(new String[0]));


      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
      Assert.assertTrue(systemOutRule.getLog().contains("Miss"));
    }
  }

  @Test
  public void testGameWin() {
    try {
      List<String> inputLines = getPlayerPositionString();

      Main.getDefaultEnemyFleet()
          .stream()
          .forEach(ship ->
              ship.getPositions().stream().forEach(position -> {

                    inputLines.add(position.getColumn().name() + position.getRow());

                  }
              ));

      gameInput.provideLines(inputLines.toArray(new String[0]));
      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().endsWith("YOU ARE THE WINNER!"));
    }
  }


  @Test
  public void testGameLost() {
    try {
      Iterator<Position> iterator = playerPositions.iterator();
      Main.enemyIA=()->{
        return iterator.next();
      };

      final List<String> inputLines = getPlayerPositionString();

      playerPositions.forEach((a) -> inputLines.add("a1"));

      gameInput.provideLines(inputLines.toArray(new String[0]));
      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().endsWith("YOU LOST!"));
    }
  }

  @Test
  public void testCorrectPosition() {
    try {
      gameInput.provideLines(Arrays.asList("a1","a2","b1").toArray(new String[0]));
      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().contains("Invalid position, please try again"));
    }
  }
  @Test
  public void testCorrectPosition2() {
    try {
      gameInput.provideLines(Arrays.asList("a20").toArray(new String[0]));
      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().contains("Invalid position, please try again"));
    }
  }
  @Test
  public void testCorrectPosition3() {
    try {
      gameInput.provideLines(Arrays.asList("a1","a3").toArray(new String[0]));
      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().contains("Invalid position, please try again"));
    }
  }
  @Test
  public void testCorrectPosition4() {
    try {
      gameInput.provideLines(Arrays.asList("a1","b2").toArray(new String[0]));
      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().contains("Invalid position, please try again"));
    }
  }
}

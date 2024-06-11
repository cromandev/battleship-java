package org.scrum.psd.battleship.ascii;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.scrum.psd.battleship.controller.GameController;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class MainEndToEndTest {
  @ClassRule
  public static final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
  @ClassRule
  public static final TextFromStandardInputStream gameInput = emptyStandardInputStream();

  @Test
  public void testPlayGameShotHits() {
    try {
      gameInput.provideLines("a1", "a2", "a3", "a4", "a5", "b1", "b2", "b3", "b4", "c1", "c2", "c3", "d1", "d2", "d3", "e1", "e2", "b4");

      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
      Assert.assertTrue(systemOutRule.getLog().contains("Yeah ! Nice hit !"));
    }
  }

  @Test
  public void testPlayGameShotMisses() {
    try {
      gameInput.provideLines("a1", "a2", "a3", "a4", "a5", "b1", "b2", "b3", "b4", "c1", "c2", "c3", "d1", "d2", "d3", "e1", "e2", "e4");

      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().contains("Welcome to Battleship"));
      Assert.assertTrue(systemOutRule.getLog().contains("Miss"));
    }
  }

  @Test
  public void testGameWin() {
    try {
      List<String> inputLines = new LinkedList<>(Arrays.asList(
          "a1", "a2", "a3", "a4", "a5", "b1", "b2", "b3", "b4", "c1", "c2", "c3", "d1", "d2", "d3", "e1", "e2"
      ));

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
      Assert.assertTrue(systemOutRule.getLog().contains("You are the winner!"));
    }
  }


  @Test
  public void testGameLost() {
    try {
      List<String> inputLines = new LinkedList<>(Arrays.asList(
          "a1", "a2", "a3", "a4", "a5", "b1", "b2", "b3", "b4", "c1", "c2", "c3", "d1", "d2", "d3", "e1", "e2"
      ));
      // Enemy win..

      gameInput.provideLines(inputLines.toArray(new String[0]));
      Main.main(new String[]{});
    } catch (NoSuchElementException e) {
      Assert.assertTrue(systemOutRule.getLog().contains("You lost!"));
    }
  }
}

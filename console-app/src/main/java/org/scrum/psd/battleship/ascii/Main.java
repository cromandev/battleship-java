package org.scrum.psd.battleship.ascii;

import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

import java.util.*;
import java.util.function.Supplier;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class Main {
  private static List<Ship> myFleet;
  private static List<Ship> enemyFleet;

  public static List<Position> computerShots = fillWithAllPositions();
  public static Supplier<Position> enemyIA = Main::getRandomPosition;

  private static final Telemetry telemetry = new Telemetry();

  public static void main(String[] args) {
    telemetry.trackEvent("ApplicationStarted", "Technology", "Java");
    System.out.println(colorize("                                     |__", MAGENTA_TEXT()));
    System.out.println(colorize("                                     |\\/", MAGENTA_TEXT()));
    System.out.println(colorize("                                     ---", MAGENTA_TEXT()));
    System.out.println(colorize("                                     / | [", MAGENTA_TEXT()));
    System.out.println(colorize("                              !      | |||", MAGENTA_TEXT()));
    System.out.println(colorize("                            _/|     _/|-++'", MAGENTA_TEXT()));
    System.out.println(colorize("                        +  +--|    |--|--|_ |-", MAGENTA_TEXT()));
    System.out.println(colorize("                     { /|__|  |/\\__|  |--- |||__/", MAGENTA_TEXT()));
    System.out.println(colorize("                    +---------------___[}-_===_.'____                 /\\", MAGENTA_TEXT()));
    System.out.println(colorize("                ____`-' ||___-{]_| _[}-  |     |_[___\\==--            \\/   _", MAGENTA_TEXT()));
    System.out.println(colorize(" __..._____--==/___]_|__|_____________________________[___\\==--____,------' .7", MAGENTA_TEXT()));
    System.out.println(colorize("|                        Welcome to Battleship                         BB-61/", MAGENTA_TEXT()));
    System.out.println(colorize(" \\_________________________________________________________________________|", MAGENTA_TEXT()));
    System.out.println("");

    InitializeGame();

    StartGame();
  }

  private static void StartGame() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("\033[2J\033[;H");
    System.out.println("                  __");
    System.out.println("                 /  \\");
    System.out.println("           .-.  |    |");
    System.out.println("   *    _.-'  \\  \\__/");
    System.out.println("    \\.-'       \\");
    System.out.println("   /          _/");
    System.out.println("  |      _  /\" \"");
    System.out.println("  |     /_\'");
    System.out.println("   \\    \\_/");
    System.out.println("    \" \"\" \"\" \"\" \"");

    do {
      System.out.println("");
      System.out.println("Player, it's your turn");
      System.out.println("Enter coordinates for your shot:");

      String input = scanner.next();

      while (!checkPositionIsValid(input)) {
        System.out.println("Miss");
        System.out.println("The position chosen is out of bounds you can select another position:");
        input = scanner.next();
      }

      Position position = parsePosition(input);

      boolean isHit = GameController.checkIsHit(enemyFleet, position);
      if (isHit) {
        beep();

        System.out.println("                \\         .  ./");
        System.out.println("              \\      .:\" \";'.:..\" \"   /");
        System.out.println("                  (M^^.^~~:.'\" \").");
        System.out.println("            -   (/  .    . . \\ \\)  -");
        System.out.println("               ((| :. ~ ^  :. .|))");
        System.out.println("            -   (\\- |  \\ /  |  /)  -");
        System.out.println("                 -\\  \\     /  /-");
        System.out.println("                   \\  \\   /  /");
      }

      System.out.println(isHit ? "Yeah ! Nice hit !" : "Miss");
      telemetry.trackEvent("Player_ShootPosition", "Position", position.toString(), "IsHit", Boolean.valueOf(isHit).toString());

      boolean isDestroyed = GameController.checkShipIsDestroyed(enemyFleet, position);
      System.out.println(isDestroyed ? "Ohhh Yeah Baby ! One ship down !" : "The ship is still alive");

      System.out.println("Ships sunk in the enemy fleet:");
      for (Ship ship : enemyFleet) {
        if (ship.isSunk()) {
          System.out.println(ship.getName() + ": " + ship.getPositions().stream().map(pos -> "[X]").reduce(" ", String::concat));
        }
      }

      System.out.println("Ships left in the enemy fleet:");
      for (Ship ship : enemyFleet) {
        if (!ship.isSunk()) {
          System.out.println(ship.getName() + ": " + ship.getPositions().stream().map(pos -> "[ ]").reduce(" ", String::concat));
        }
      }


      position = enemyIA.get();
      isHit = GameController.checkIsHit(myFleet, position);
      System.out.println("");
      System.out.println(String.format("Computer shoot in %s%s and %s", position.getColumn(), position.getRow(), isHit ? "hit your ship !" : "miss"));
      telemetry.trackEvent("Computer_ShootPosition", "Position", position.toString(), "IsHit", Boolean.valueOf(isHit).toString());
      if (isHit) {
        beep();

        System.out.println("                \\         .  ./");
        System.out.println("              \\      .:\" \";'.:..\" \"   /");
        System.out.println("                  (M^^.^~~:.'\" \").");
        System.out.println("            -   (/  .    . . \\ \\)  -");
        System.out.println("               ((| :. ~ ^  :. .|))");
        System.out.println("            -   (\\- |  \\ /  |  /)  -");
        System.out.println("                 -\\  \\     /  /-");
        System.out.println("                   \\  \\   /  /");

        boolean yourShipDestroyed = GameController.checkShipIsDestroyed(myFleet, position);
        System.out.println(yourShipDestroyed ? "Awww your ship was destroyed !" : "Your ship is still alive");

      }

      System.out.println("Ships sunk in your fleet:");
      for (Ship ship : myFleet) {
        if (ship.isSunk()) {
          System.out.println(ship.getName() + ": " + ship.getPositions().stream().map(pos -> "[X]").reduce(" ", String::concat));
        }
      }

      System.out.println("Ships left in your fleet:");
      for (Ship ship : myFleet) {
        if (!ship.isSunk()) {
          System.out.println(ship.getName() + ": " + ship.getPositions().stream().map(pos -> "[ ]").reduce(" ", String::concat));
        }
      }

      boolean iWin = GameController.allShipsDestroyed(enemyFleet);
      boolean isLost = GameController.allShipsDestroyed(myFleet);

      if (iWin) {
        displayGameWinMessage();
        return;
      } else if (isLost) {
        displayGameLoseMessage();
        return;
      }

    } while (true);
  }

  private static void displayGameLoseMessage() {
    System.out.println("                      :::!~!!!!!:.                           ");
    System.out.println("                  .xUHWH!! !!?M88WHX:.                       ");
    System.out.println("                .X*#M@$!!  !X!M$$$$$$WWx:.                   ");
    System.out.println("               :!!!!!!?H! :!$!$$$$$$$$$$8X:                  ");
    System.out.println("              !!~  ~:~!! :~!$!#$$$$$$$$$$8X:                 ");
    System.out.println("             :!~::!H!<   ~.U$X!?R$$$$$$$$MM!                 ");
    System.out.println("             ~!~!!!!~~ .:XW$$$U!!?$$$$$$RMM!                 ");
    System.out.println("               !:~~~ .:!M'T#$$$$WX??#MRRMMM!                 ");
    System.out.println("               ~?WuxiW*`   `'#$$$$8!!!!??!!!                 ");
    System.out.println("             :X- M$$$$       `'T#$T~!8$WUXU~                 ");
    System.out.println("            :%`  ~#$$$m:        ~!~ ?$$$$$$                  ");
    System.out.println("        :!`.-   ~T$$$$8xx.  .xWW- ~''##*'''                  ");
    System.out.println(".....   -~~:<` !    ~?T#$$@@W@*?$$      /`                   ");
    System.out.println("W$@@M!!! .!~~ !!     .:XUW$W!~ `'~:    :                     ");
    System.out.println("#'~~`.:x%`!!  !H:   !WM$$$$Ti.: .!WUn+!`                     ");
    System.out.println(":::~:!!`:X~ .: ?H.!u '$$$B$$$!W:U!T$$M~                      ");
    System.out.println(".~~   :X@!.-~   ?@WTWo('*$$$W$TH$! `                         ");
    System.out.println("Wi.~!X$?!-~    : ?$$$B$Wu('**$RM!                            ");
    System.out.println("$R@i.~~ !     :   ~$$$$$B$$en:``                             ");
    System.out.println("?MXT@Wx.~    :     ~'##*$$$$M~                               ");

    System.out.println("YOU LOST!");
  }

  private static void displayGameWinMessage() {
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣤⣤⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠀⠀⠀⢀⣴⠟⠉⠀⠀⠀⠈⠻⣦⡀⠀⠀⠀⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣷⣀⢀⣾⠿⠻⢶⣄⠀⠀⣠⣶⡿⠶⣄⣠⣾⣿⠗⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⢻⣿⣿⡿⣿⠿⣿⡿⢼⣿⣿⡿⣿⣎⡟⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⡟⠉⠛⢛⣛⡉⠀⠀⠙⠛⠻⠛⠑⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⣧⣤⣴⠿⠿⣷⣤⡤⠴⠖⠳⣄⣀⣹⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣀⣟⠻⢦⣀⡀⠀⠀⠀⠀⣀⡈⠻⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⡿⠉⡇⠀⠀⠛⠛⠛⠋⠉⠉⠀⠀⠀⠹⢧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣾⡟⠀⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠃⠀⠈⠑⠪⠷⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣾⣿⣿⣿⣦⣼⠛⢦⣤⣄⡀⠀⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⡀⠀⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⠀⠀⢀⣠⠴⠲⠖⠛⠻⣿⡿⠛⠉⠉⠻⠷⣦⣽⠿⠿⠒⠚⠋⠉⠁⡞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢦⠀⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⠀⢀⣾⠛⠁⠀⠀⠀⠀⠀⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢣⠀⠀⠀");
    System.out.println("⠀⠀⠀⠀⣰⡿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣑⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡇⠀⠀");
    System.out.println("⠀⠀⠀⣰⣿⣁⠀⠀⠀⠀⠀⠀⠀⠀         ⠀⠀⠀⣷⠀⠀⠀⠀    ⠀⠀⠀⠀⠀⣾⣧⣄⠀⠀⠀⠀⠀⠀⢳⡀⠀");
    System.out.println("⠀⠀⠀⣿⡾⢿⣀⢀⣀⣦⣾⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⣫⣿⡿⠟⠻⠶⠀⠀⠀⠀⠀⢳⠀");
    System.out.println("⠀⠀⢀⣿⣧⡾⣿⣿⣿⣿⣿⡷⣶⣤⡀⠀⠀⠀⠀⠀⠀⠀⢀⡴⢿⣿⣧⠀⡀⠀⢀⣀⣀⢒⣤⣶⣿⣿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇");
    System.out.println("⠀⠀⡾⠁⠙⣿⡈⠉⠙⣿⣿⣷⣬⡛⢿⣶⣶⣴⣶⣶⣶⣤⣤⠤⠾⣿⣿⣿⡿⠿⣿⠿⢿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇");
    System.out.println("⠀⣸⠃⠀⠀⢸⠃⠀⠀⢸⣿⣿⣿⣿⣿⣿⣷⣾⣿⣿⠟⡉⠀⠀⠀⠈⠙⠛⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇");
    System.out.println("⠀⣿⠀⠀⢀⡏⠀⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⠿⠿⠛⠛⠉⠁⠀⠀⠀⠀⠀⠉⠠⠿⠟⠻⠟⠋⠉⢿⣿⣦⡀⢰⡀⠀⠀⠀⠀⠀⠀⠁");
    System.out.println("⢀⣿⡆⢀⡾⠀⠀⠀⠀⣾⠏⢿⣿⣿⣿⣯⣙⢷⡄⠀⠀⠀⠀⠀⢸⡄⠀⠀⠀⠀⠀⠀⠀⠀⢀⣤⣿⣻⢿⣷⣀⣷⣄⠀⠀⠀⠀⢸⠀");
    System.out.println("⢸⠃⠠⣼⠃⠀⠀⣠⣾⡟⠀⠈⢿⣿⡿⠿⣿⣿⡿⠿⠿⠿⠷⣄⠈⠿⠛⠻⠶⢶⣄⣀⣀⡠⠈⢛⡿⠃⠈⢿⣿⣿⡿⠀⠀⠀⠀⠀⡀");
    System.out.println("⠟⠀⠀⢻⣶⣶⣾⣿⡟⠁⠀⠀⢸⣿⢅⠀⠈⣿⡇⠀⠀⠀⠀⠀⣷⠂⠀⠀⠀⠀⠐⠋⠉⠉⠀⢸⠁⠀⠀⠀⢻⣿⠛⠀⠀⠀⠀⢀⠇");
    System.out.println("⠀⠀⠀⠀⠹⣿⣿⠋⠀⠀⠀⠀⢸⣧⠀⠰⡀⢸⣷⣤⣤⡄⠀⠀⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡆⠀⠀⠀⠀⡾⠀⠀⠀⠀⠀⠀⢼⡇");
    System.out.println("⠀⠀⠀⠀⠀⠙⢻⠄⠀⠀⠀⠀⣿⠉⠀⠀⠈⠓⢯⡉⠉⠉⢱⣶⠏⠙⠛⠚⠁⠀⠀⠀⠀⠀⣼⠇⠀⠀⠀⢀⡇⠀⠀⠀⠀⠀⠀⠀⡇");
    System.out.println("⠀⠀⠀⠀⠀⠀⠻⠄⠀⠀⠀⢀⣿⠀⢠⡄⠀⠀⠀⣁⠁⡀⠀⢠⠀⠀⠀⠀⠀⠀⠀⠀⢀⣐⡟⠀⠀⠀⠀⢸⡇⠀⠀⠀⠀⠀⠀⢠⡇");

    System.out.println("YOU ARE THE WINNER!");

  }

  private static void beep() {
    System.out.print("\007");
  }

  protected static Position parsePosition(String input) {
    Letter letter = Letter.valueOf(input.toUpperCase().substring(0, 1));
    int number = Integer.parseInt(input.substring(1));
    return new Position(letter, number);
  }

  protected static boolean checkPositionIsValid(String input) {
    try {
      return Integer.parseInt(input.substring(1)) >= 1 && Integer.parseInt(input.substring(1)) <= 8 && input.toUpperCase().substring(0, 1).matches("^[A-H]$");
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }

  private static List<Position> fillWithAllPositions() {
    List<Position> positionSet = new ArrayList<>();

    int rows = 8;
    int lines = 8;

    for (int indexLetter = 0; indexLetter < rows; indexLetter++) {
      for (int number = 1; number <= lines; number++) {
        Letter letter = Letter.values()[indexLetter];
        Position position = new Position(letter, number);
        positionSet.add(position);
      }
    }

    return positionSet;

  }

  public static Position getRandomPosition() {

    if (computerShots.isEmpty()) {
      return null;
    }

    Random random = new Random();
    int indexPosition = random.nextInt(computerShots.size());
    return computerShots.remove(indexPosition);

  }

  private static void InitializeGame() {
    InitializeMyFleet();
    computerShots = fillWithAllPositions();
    InitializeEnemyFleet();
  }

  private static void InitializeMyFleet() {
    Scanner scanner = new Scanner(System.in);
    myFleet = GameController.initializeShips();

    System.out.println("Please position your fleet (Game board has size from A to H and 1 to 8) :");

    for (Ship ship : myFleet) {
      System.out.println("");
      System.out.println(String.format("Please enter the positions for the %s (size: %s)", ship.getName(), ship.getSize()));
      for (int i = 1; i <= ship.getSize(); i++) {
        String positionInput;
        do {
          System.out.println(String.format("Enter position %s of %s (i.e A3):", i, ship.getSize()));
          positionInput = scanner.next();
          if (isValidPosition(myFleet, ship, i, positionInput)) {
            break;
          } else {
            System.out.println("Invalid position, please try again");
          }
        } while (true);
        ship.addPosition(positionInput);
        telemetry.trackEvent("Player_PlaceShipPosition", "Position", positionInput, "Ship", ship.getName(), "PositionInShip", Integer.valueOf(i).toString());
      }
    }
  }

  public static void InitializeEnemyFleet() {
    enemyFleet = getDefaultEnemyFleet();
  }

  private static boolean isValidPosition(List<Ship> myFleet, Ship ship, int i, String positionInput) {
    if (!checkPositionIsValid(positionInput)) {
      return false;
    }
    Position position = parsePosition(positionInput);
    return canAddThisPosition(ship, position)
        && consecutivePositions(ship, position)
        && notRepeatedPositions(myFleet, position);
  }

  private static boolean notRepeatedPositions(List<Ship> myFleet, Position position) {
    return myFleet.stream().allMatch(ship -> ship.getPositions().stream().noneMatch(p -> p.equals(position)));
  }

  private static boolean canAddThisPosition(Ship ship, Position position) {
    return ship.getPositions().size() == 0
        || ship.getPositions().stream().allMatch(pos -> pos.getRow() == position.getRow())
        || ship.getPositions().stream().allMatch(pos -> pos.getColumn() == position.getColumn());
  }

  private static boolean consecutivePositions(Ship ship, Position position) {
    if (ship.getPositions().size() == 0) {
      return true;
    }
    Position lastPosition = ship.getPositions().get(ship.getPositions().size() - 1);
    return Math.abs(lastPosition.getRow() - position.getRow()) <= 1
        && Math.abs(lastPosition.getColumn().ordinal() - position.getColumn().ordinal()) <= 1;

  }

  public static List<Ship> getDefaultEnemyFleet() {
    List<Ship> defaultEnemyFleet = GameController.initializeShips();

    defaultEnemyFleet.get(0).getPositions().add(new Position(Letter.B, 4));
    defaultEnemyFleet.get(0).getPositions().add(new Position(Letter.B, 5));
    defaultEnemyFleet.get(0).getPositions().add(new Position(Letter.B, 6));
    defaultEnemyFleet.get(0).getPositions().add(new Position(Letter.B, 7));
    defaultEnemyFleet.get(0).getPositions().add(new Position(Letter.B, 8));

    defaultEnemyFleet.get(1).getPositions().add(new Position(Letter.E, 4));
    defaultEnemyFleet.get(1).getPositions().add(new Position(Letter.E, 5));
    defaultEnemyFleet.get(1).getPositions().add(new Position(Letter.E, 6));
    defaultEnemyFleet.get(1).getPositions().add(new Position(Letter.E, 7));

    defaultEnemyFleet.get(2).getPositions().add(new Position(Letter.A, 3));
    defaultEnemyFleet.get(2).getPositions().add(new Position(Letter.B, 3));
    defaultEnemyFleet.get(2).getPositions().add(new Position(Letter.C, 3));

    defaultEnemyFleet.get(3).getPositions().add(new Position(Letter.F, 8));
    defaultEnemyFleet.get(3).getPositions().add(new Position(Letter.G, 8));
    defaultEnemyFleet.get(3).getPositions().add(new Position(Letter.H, 8));

    defaultEnemyFleet.get(4).getPositions().add(new Position(Letter.C, 5));
    defaultEnemyFleet.get(4).getPositions().add(new Position(Letter.C, 6));
    return defaultEnemyFleet;
  }

}

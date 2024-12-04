import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.animation.AnimationTimer
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.Includes._

// Main object of the application, extending JFXApp to set up the JavaFX application
object MainApp extends JFXApp {

  // Constants defining the size of the grid and the size of each grid cell
  val GridSize = 20
  val GridWidth = 40
  val GridHeight = 40
  val MovesPerGridCell = 10 // Controls the speed of snake movement

  // Creating an instance of the game world
  val game = new World()

  // Start a new game, resetting all game state
  def startGame(): Unit = {
    game.snake = List(new SnakePlayer(GridWidth / 2, GridHeight / 2))
    game.food = game.generateFood()
    game.direction = (1, 0)
    game.moveCounter = 0
    game.score = 0
    game.gameOver = false
    stage.scene().content = game.render
    timer.start()
  }

  // Set up the Main Stage (window) of the application
  stage = new PrimaryStage {
    title = "Snake Game"
    scene = new Scene(GridWidth * GridSize, GridHeight * GridSize) {
      fill = Color.Black
      content = game.welcomeScreen // Show the welcome screen initially

      // Handles the keyboard presses to change the snake's direction
      onKeyPressed = (event: KeyEvent) => {
        event.code match {
          case KeyCode.Up    => game.changeDirection((0, -1))
          case KeyCode.Down  => game.changeDirection((0, 1))
          case KeyCode.Left  => game.changeDirection((-1, 0))
          case KeyCode.Right => game.changeDirection((1, 0))
          case _             =>
        }
      }
    }
  }

  // Timer to create the game loop, continuously updating the game state
  val timer = AnimationTimer { _ =>
    if (!game.gameOver) {
      game.update() // Updates the game state if not over
      stage.scene().content = game.render // Re-renders the scene
    } else {
      val gameOverText = game.gameOverText
      gameOverText.layoutX = (GridWidth * GridSize - gameOverText.boundsInLocal.value.getWidth) / 2
      gameOverText.layoutY = (GridHeight * GridSize - gameOverText.boundsInLocal.value.getHeight) / 2
      stage.scene().content = Seq(gameOverText) // Display only the Game Over text on the screen when the game ends
    }
  }
}
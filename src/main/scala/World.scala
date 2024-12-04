import MainApp.{GridHeight, GridWidth, GridSize, MovesPerGridCell, startGame}
import scalafx.geometry.Pos
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, Text}

import scala.util.Random

// Class representing the entire game world, including the snake, food, and game state
class World {
  var snake: List[SnakePlayer] = List(new SnakePlayer(GridWidth / 2, GridHeight / 2)) // Initial snake position
  var food: Food = generateFood() // Initial food position
  var direction: (Int, Int) = (0, 0) // Initial snake movement (stationary)
  var moveCounter: Int = 0 // Counter to manage snake movement speed
  var score: Int = 0 // Player's initial score
  var gameOver: Boolean = false // Game over state

  // Update the game state, including moving the snake and checking for collisions
  def update(): Unit = {
    if (!gameOver) {
      moveCounter += 1
      if (moveCounter >= MovesPerGridCell) {
        moveSnake() // Move the snake in the current direction
        checkCollisions() // Check for collisions with walls or itself
        moveCounter = 0
      }
    }
  }

  // Move the snake by adding a new head
  def moveSnake(): Unit = {
    val head = snake.head
    val newHead = new SnakePlayer(
      head.x + direction._1, // Calculate new x position based on direction
      head.y + direction._2 // Calculate new y position based on direction
    )
    // Add new head to the snake
    snake = newHead :: snake

    // Check if the snake has eaten the food
    if (newHead.x == food.x && newHead.y == food.y) {
      food = generateFood() // Generate new food if eaten
      score += 10 // Increases the score after a food item has been eaten
    } else {
      snake = snake.init // Remove the last segment (tail) if food was not eaten
    }
  }

  // Render all elements of the game: snake, food, and score
  def render: Seq[scalafx.scene.Node] = {
    val snakeNodes = snake.map(_.render)
    val foodNode = food.render
    val scoreText = new Text {
      x = 10
      y = 20
      text = s"Score: $score" // Displays the current score
      fill = Color.White
      font = new Font(20)
    }

    // Combines snake, food, and score into a single sequence
    snakeNodes :+ foodNode :+ scoreText
  }

  // Check for collisions (walls, self)
  def checkCollisions(): Unit = {
    val head = snake.head
    if (head.x < 0 || head.x >= GridWidth || head.y < 0 || head.y >= GridHeight ||
      snake.tail.exists(segment => segment.x == head.x && segment.y == head.y)) {
      gameOver = true // Ends the game if a collision is detected
    }
  }

  // Change the direction of the snake, ensuring it cannot reverse direction directly
  def changeDirection(newDirection: (Int, Int)): Unit = {
    if (direction._1 + newDirection._1 != 0 || direction._2 + newDirection._2 != 0) {
      direction = newDirection
    }
  }

  // Generate a new food item at a random position on the grid
  // Ensures the food does not appear on the snake's body
  def generateFood(): Food = {
    val random = new Random()
    var foodX, foodY: Int = 0
    do {
      foodX = random.nextInt(GridWidth)
      foodY = random.nextInt(GridHeight)
    } while (snake.exists(s => s.x == foodX && s.y == foodY))
    new Food(foodX, foodY)
  }

  // Create the welcome screen with instructions and a play button
  def welcomeScreen: VBox = new VBox(20) {
    alignment = Pos.Center
    prefWidth = GridWidth * GridSize
    prefHeight = GridHeight * GridSize
    children = Seq(
      new Text {
        text = "Welcome to a classic game of snake!\n" +
          "Here's how the game works:\n" +
          "* Move the snake with the arrow keys.\n" +
          "* You cannot go past the border or collide with yourself.\n" +
          "* Collect the food items to rack up points!"
        fill = Color.White
        font = new Font(20)
        textAlignment = scalafx.scene.text.TextAlignment.Center
      },
      new Button("Play") {
        onAction = _ => startGame()
        prefWidth = 100
        prefHeight = 40
        style = "-fx-font-size: 18px;"
      }
    )
  }

  // Creates the Game Over text to display when the game ends
  def gameOverText: Text = new Text {
    text = s"Game Over!\nScore: $score"
    fill = Color.White
    font = new Font(30)
    textAlignment = scalafx.scene.text.TextAlignment.Center
  }
}
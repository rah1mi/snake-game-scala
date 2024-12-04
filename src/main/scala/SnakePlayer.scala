import scalafx.scene.paint.Color

// Class representing a segment of the snake, extends GameEntity
// Each segment is a green rectangle on the grid
class SnakePlayer(x: Int, y: Int) extends GameEntity(x, y, Color.Green)
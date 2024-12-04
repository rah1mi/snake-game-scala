import scalafx.scene.paint.Color

// Class representing the food in the game, extends GameEntity
// The food is a red rectangle that the snake tries to eat
class Food(x: Int, y: Int) extends GameEntity(x, y, Color.Red)
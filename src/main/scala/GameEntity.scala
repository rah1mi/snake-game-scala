import MainApp.{GridSize}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

// Base class for all entities in the game (e.g., SnakePlayer, Food)
// Provides common properties (x, y) and a method to render the entity as a rectangle
abstract class GameEntity(var x: Int, var y: Int, val color: Color) {
  def render: Rectangle = new Rectangle {
    width = GridSize
    height = GridSize
    fill = color
    x = GameEntity.this.x * GridSize
    y = GameEntity.this.y * GridSize
  }
}
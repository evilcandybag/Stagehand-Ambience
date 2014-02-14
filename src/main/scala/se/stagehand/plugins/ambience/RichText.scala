package se.stagehand.plugins.ambience

import se.stagehand.lib.scripting.network.NetworkedEffect
import se.stagehand.lib.scripting.ID

class RichText(id:Int) extends NetworkedEffect(id) {
  def this() = this(ID.unique)

  def componentName = "Rich Text"
  
  def text = ""
  
  def requirements = null
  override def runArgs = null
}
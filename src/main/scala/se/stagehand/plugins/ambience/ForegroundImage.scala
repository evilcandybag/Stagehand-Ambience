package se.stagehand.plugins.ambience

import se.stagehand.lib.scripting.network.NetworkedEffect
import se.stagehand.lib.scripting.ID
import se.stagehand.lib.scripting.network.Capabilities
import se.stagehand.swing.assets.AudioAssets
import se.stagehand.swing.assets.ImageAssets

class ForegroundImage(id:Int) extends URLEffect(id) {
  def this() = this(ID.unique)

  def componentName = "Foreground Image"  
  def default = ImageAssets.FOREGROUND_PLACEHOLDER.toURL()
    
  def requirements = Set(Capabilities.IMG_FOREGROUND)
  
  override def runArgs = Map(
      (Capabilities.IMG_FOREGROUND -> url.toString())
      ) ++ super.runArgs 
  
}
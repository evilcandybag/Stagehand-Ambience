package se.stagehand.plugins.ambience

import se.stagehand.lib.scripting.network.NetworkedEffect
import se.stagehand.lib.scripting.ID
import se.stagehand.lib.scripting.network.Capabilities
import se.stagehand.swing.assets.AudioAssets

class BackgroundMusic(id:Int) extends URLEffect(id) {
  def this() = this(ID.unique)

  def componentName = "Background Music"  
  def default = AudioAssets.BGM_WEB
    
  def requirements = Set(Capabilities.MUSIC_BACKGROUND)
  
  override def runArgs = Map(
      (Capabilities.MUSIC_BACKGROUND -> url.toString())
      ) ++ super.runArgs 
  
}
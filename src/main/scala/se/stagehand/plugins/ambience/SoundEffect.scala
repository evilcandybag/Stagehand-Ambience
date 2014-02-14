package se.stagehand.plugins.ambience

import se.stagehand.lib.scripting.network.NetworkedEffect
import se.stagehand.lib.scripting.ID
import se.stagehand.lib.scripting.network.Capabilities
import se.stagehand.swing.assets.AudioAssets

class SoundEffect(id:Int) extends URLEffect(id) {
  def this() = this(ID.unique)

  def componentName = "Sound Effect"  
  def default = AudioAssets.SOUND_EFFECT_PLACEHOLDER.toURL()
    
  def requirements = Set(Capabilities.SOUND_EFFECT)
  
  override def runArgs = Map(
      (Capabilities.SOUND_EFFECT -> url.toString())
      ) ++ super.runArgs 
  
}
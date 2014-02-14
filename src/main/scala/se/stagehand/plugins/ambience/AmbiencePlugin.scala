package se.stagehand.plugins.ambience

import se.stagehand.plugins.EffectPlugin
import se.stagehand.lib.scripting.Effect
import se.stagehand.plugins.ComponentGUI

class AmbiencePlugin extends EffectPlugin {

  val name = "Ambience Plugin"
  
  val guis:List[ComponentGUI] = List(BackgroundImageGUI, BackgroundMusicGUI, ForegroundImageGUI, SoundEffectGUI)
    
  val effects:Array[Effect] = Array(new BackgroundImage, new BackgroundMusic, new ForegroundImage, new SoundEffect)
  
}
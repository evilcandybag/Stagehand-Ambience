package se.stagehand.plugins.ambience

import se.stagehand.plugins.EffectPlugin
import se.stagehand.lib.scripting.Effect
import se.stagehand.plugins.ComponentGUI

class AmbiencePlugin extends EffectPlugin {

  val name = "Stagehand-Ambience"
  
  val guis:List[ComponentGUI] = List(BackgroundImageGUI)
    
  val effects:Array[Effect] = Array(new BackgroundImage)
  
}
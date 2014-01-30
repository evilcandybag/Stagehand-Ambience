package se.stagehand.plugins.ambience.server

import se.stagehand.lib.network.EffectServer
import se.stagehand.lib.scripting.network.Capabilities
import se.stagehand.lib.scripting.network.NetworkedEffect
import se.stagehand.lib.network.AbstractWorker
import se.stagehand.lib.scripting.Target
import scalafx.application.Platform

object AmbienceServer extends EffectServer {
  def name = "AmbienceServer"
  def properties = {
    val caps = List(Capabilities.IMG_BACKGROUND)
    val desc = "Displays rich content as a HTML5 page using the Ambience libraries."
    
    Map((NetworkedEffect.DESCRIPTION -> desc), (NetworkedEffect.CAPABILITIES -> caps.mkString(",")))
  }
  
  val worker = new AmbienceWorker
  
  override def main(args:Array[String]) {
    super.main(args)
    AmbienceView.main(args)
  }
  
}
class AmbienceWorker extends AbstractWorker(AmbienceServer) {
  import Target.Protocol
  
  def received(args:Protocol.Arguments) {
    import AmbienceProtocol._
    val json = jsonString(parse(args))
    
    log.debug(json)
    
    Platform.runLater {
      AmbienceView.play(json)
    }
  }
  
}
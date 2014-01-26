package se.stagehand.plugins.ambience

import se.stagehand.lib.scripting._
import scala.xml.Elem
import scala.swing._
import se.stagehand.lib.scripting.network.NetworkedEffect
import se.stagehand.lib.scripting.network.Capabilities
import java.net.URL
import se.stagehand.swing.assets.ImageAssets
import scala.xml.Node
import java.net.MalformedURLException

class BackgroundImage(id:Int) extends NetworkedEffect(id) {
  def this() = this(ID.unique)

  def componentName = "BackgroundImage"
    
  def requirements = Set(Capabilities.IMG_BACKGROUND)
  
  def runArgs = Map(
    (Capabilities.IMG_BACKGROUND -> imgUrl.toString())  
  )
  
  var imgUrl: URL = ImageAssets.BACKGROUND_PLACEHOLDER.toURL()
  
  override def readInstructions(in:Node) {
    super.readInstructions(in)
    try {
      imgUrl = new URL((in \\ "url").text)
    } catch {
      case e: MalformedURLException => {
        log.error("Effect " + componentName + "#" + id + " contains invalid URL: " + imgUrl.toString )
      }
    }
    
  }
  override def generateInstructions = {
    implicit var xml = super.generateInstructions
    xml = addChild(<url>{imgUrl.toString}</url>)
    
    xml
  }
  
}

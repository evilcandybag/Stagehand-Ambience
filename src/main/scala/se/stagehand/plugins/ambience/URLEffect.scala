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

abstract class URLEffect(id:Int) extends NetworkedEffect(id) {
  def this() = this(ID.unique)
  
  protected def default: URL
  private var _url:URL = default
  def url = _url
  def url_=(u:URL) {_url = u}
  
  override def readInstructions(in:Node) {
    super.readInstructions(in)
    try {
      url = new URL((in \\ "url").text)
    } catch {
      case e: MalformedURLException => {
        log.error("Effect " + componentName + "#" + id + " contains invalid URL: " + url.toString )
      }
    }
    
  }
  override def generateInstructions = {
    implicit var xml = super.generateInstructions
    xml = addChild(<url>{url.toString}</url>)
    
    xml
  }
  
}

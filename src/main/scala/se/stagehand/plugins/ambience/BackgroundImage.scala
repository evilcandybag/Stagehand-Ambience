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

class BackgroundImage(id:Int) extends URLEffect(id) {
  def this() = this(ID.unique)

  def componentName = "Background Image"
  def default = ImageAssets.BG_WEB
    
  def requirements = Set(Capabilities.IMG_BACKGROUND)
  
  override def runArgs = Map(
    (Capabilities.IMG_BACKGROUND -> url.toString()) 
  ) ++ super.runArgs 
}

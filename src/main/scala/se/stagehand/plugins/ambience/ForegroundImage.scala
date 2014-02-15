package se.stagehand.plugins.ambience

import se.stagehand.lib.scripting.network.NetworkedEffect
import se.stagehand.lib.scripting.ID
import se.stagehand.lib.scripting.network.Capabilities
import se.stagehand.swing.assets.AudioAssets
import se.stagehand.swing.assets.ImageAssets
import scala.xml.Node
import se.stagehand.lib.scripting.Target

class ForegroundImage(id:Int) extends URLEffect(id) {
  def this() = this(ID.unique)

  def componentName = "Foreground Image"  
  def default = ImageAssets.FG_WEB
    
  def requirements = Set(Capabilities.IMG_FOREGROUND)
  
  var scale: (Option[Int], Option[Int]) = (None,None)
  
  
  override def runArgs = Map(
      (Capabilities.IMG_FOREGROUND -> (url.toString() + 
        Target.Protocol.KEY_KEY + showScale(scale._1) + 
        Target.Protocol.KEY_KEY + showScale(scale._2))
      )
      ) ++ super.runArgs 
  def showScale(o: Option[Int]) = o match {
      case None => "auto"
      case Some(i) => i.toString + "%"
    }
  override def generateInstructions = {
    implicit var xml = super.generateInstructions
    
    addChild(
        <scale><x>{showScale(scale._1)}</x><y>{showScale(scale._2)}</y></scale>
        )
  }
  override def readInstructions(in:Node) {
    super.readInstructions(in)
    
    def readScale(s:String) = s match {
      case "auto" => None
      case i => Some(i.substring(0,i.length-1).toInt)
    }
    
    val x = (in \\ "scale" \\ "x").text
    val y = (in \\ "scale" \\ "y").text
    
    scale = (readScale(x),readScale(y))
  }
   
}
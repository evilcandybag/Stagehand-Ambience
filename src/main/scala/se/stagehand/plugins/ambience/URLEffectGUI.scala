package se.stagehand.plugins.ambience

import se.stagehand.plugins.ComponentGUI
import se.stagehand.swing.lib.EffectGUI
import se.stagehand.swing.lib.EditorEffectItem
import scala.swing.TextField
import se.stagehand.swing.lib.TargetedPlayerEffectItem
import se.stagehand.lib.scripting.Effect
import scala.swing.GridPanel
import javax.swing.ImageIcon
import scala.swing.Label
import scala.swing.Alignment
import scala.swing.Swing
import se.stagehand.swing.gui.GUIUtils
import javax.imageio.ImageIO
import org.imgscalr.Scalr
import scala.swing.event.EditDone
import java.net.URL
import java.net.MalformedURLException
import scala.swing.Dialog
import java.awt.Color
import scala.swing.event.MouseClicked
import scala.swing.BoxPanel
import scala.swing.Orientation

abstract class URLEffectGUI[T <: URLEffect] extends EffectGUI {
  type peertype = T
  
  def effectIcon: ImageIcon
  
  abstract class URLEffectEditorItem(e: peertype) extends BoxPanel(Orientation.Horizontal) with EditorEffectItem[peertype] {
    def effect = e 
    listenTo(mouse.clicks)
    background = Color.WHITE
    
    
    
    /**
     * Message for input dialog
     */
    def msg:String
    /**
     * Title for input dialog.
     */
    def ttl:String
    val label = new Label()
    contents += new Label("") {
      icon = effectIcon
      opaque = false
    }
    contents += label
    
    reactions += {
      case e:MouseClicked => {
        val s = Dialog.showInput[String](message = msg, title = ttl, initial = effect.url.toString)
        if (s.isDefined) try {
          val url = new URL(s.get)
          updateURL(url)
        } catch {
          case e: MalformedURLException => Dialog.showMessage(message = s + " is not a valid URL.")
        }
      }
    }
    
    updateURL(effect.url)
    tooltip = effect.componentName + ": " + effect.url.toString
    
    private def updateURL(url:URL) = {
      effect.url = url
      label.text = url.getPath().substring(url.getPath.lastIndexOf('/'))
    }
    
  }
}

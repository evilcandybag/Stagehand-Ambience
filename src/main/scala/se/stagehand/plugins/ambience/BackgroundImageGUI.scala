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

object BackgroundImageGUI extends EffectGUI {
  val peer = classOf[BackgroundImage]
  type peertype = BackgroundImage
  def editorItem(effect: Effect) = new BackgroundImageEditorItem(checkEffect[peertype](effect))
  def playerItem(effect: Effect) = new BackgroundImagePlayerItem(checkEffect[peertype](effect))

  
  class BackgroundImageEditorItem(e: peertype) extends Label with EditorEffectItem[peertype] {
    def effect = e 
    listenTo(mouse.clicks)
    background = Color.WHITE
    
    updateURL(effect.imgUrl)
    tooltip = effect.imgUrl.toString
    
    reactions += {
      case e:MouseClicked => {
        val s = Dialog.showInput[String](message = "Enter an URL to an image", title = "Enter new URL", initial = effect.imgUrl.toString)
        if (s.isDefined) try {
          val url = new URL(s.get)
          updateURL(url)
        } catch {
          case e: MalformedURLException => Dialog.showMessage(message = s + " is not a valid URL.")
        }
      }
    }
    
    private def updateURL(url:URL) = {
      effect.imgUrl = url
      text = url.getPath().substring(url.getPath.lastIndexOf('/'))
    }
    
  }
  class BackgroundImagePlayerItem(e: peertype) extends TargetedPlayerEffectItem[peertype](e){
    def effectItem = new GridPanel(1,1) {
      border = Swing.EmptyBorder(1)
      val img = ImageIO.read(effect.imgUrl)
      val scaledImg = Scalr.resize(img,80)
      img.flush()
      
      val icon = new ImageIcon(scaledImg)
      
      contents += new Label("",icon, Alignment.Center)
    }
  }
}

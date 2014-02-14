package se.stagehand.plugins.ambience

import se.stagehand.swing.lib.EditorEffectItem
import se.stagehand.lib.scripting.Effect
import se.stagehand.swing.lib.TargetedPlayerEffectItem
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.Label
import se.stagehand.swing.assets.ImageAssets
import java.awt.Color
import scala.swing.event.MouseClicked
import scala.swing.Dialog
import java.net.URL
import java.net.MalformedURLException

object ForegroundImageGUI extends URLEffectGUI[ForegroundImage] {
  val peer = classOf[ForegroundImage]
  
  def effectIcon = ImageAssets.NOTE_ICON
  
  def editorItem(e:Effect) = new ForegroundImageEditorItem(checkEffect[peertype](e))
  def playerItem(e:Effect) = new ForegroundImagePlayerItem(checkEffect[peertype](e))
  
  class ForegroundImageEditorItem(e: peertype) extends BoxPanel(Orientation.Horizontal) with EditorEffectItem[peertype] {
    def effect = e
    
    val icon = new Label(){
      icon = effectIcon
      opaque = false
    }
    listenTo(mouse.clicks)
    background = Color.WHITE
    
    
    
    /**
     * Message for input dialog
     */
    def msg:String = "Enter an URL for this image."
    /**
     * Title for input dialog.
     */
    def ttl:String = "Pick an URL"
    val label = new Label()
    contents += new Label("") {
      icon = effectIcon
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
  
  class ForegroundImagePlayerItem(e: peertype) extends TargetedPlayerEffectItem(e) {
    def effectItem = new BoxPanel(Orientation.Horizontal) {
      val icon = new Label("") {
        icon = effectIcon
      }
      val label = new Label(effect.url.getPath().substring(effect.url.getPath.lastIndexOf('/')))
      contents += icon
      contents += label
    }
  }
}
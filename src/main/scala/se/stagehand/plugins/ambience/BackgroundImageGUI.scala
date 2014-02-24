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
import se.stagehand.swing.assets.ImageAssets

object BackgroundImageGUI extends URLEffectGUI[BackgroundImage] {
  val peer = classOf[BackgroundImage]
  
  def effectIcon = ImageAssets.TREE_ICON
  
  def editorItem(effect: Effect) = new BackgroundImageEditorItem(checkEffect[peertype](effect))
  def playerItem(effect: Effect) = new BackgroundImagePlayerItem(checkEffect[peertype](effect))
  
  
  
  class BackgroundImageEditorItem(e: peertype) extends URLEffectEditorItem(e) {
     def msg = "Enter an URL to an image"
     def ttl = "Enter new URL"
  }
  class BackgroundImagePlayerItem(e: peertype) extends TargetedPlayerEffectItem[peertype](e){
    def effectItem = new GridPanel(1,1) {
      border = Swing.EmptyBorder(1)
//      val img = ImageIO.read(effect.url)
//      val scaledImg = Scalr.resize(img,80)
//      img.flush()
//      
//      val icon = new ImageIcon(scaledImg)
//      
//      contents += new Label("",icon, Alignment.Center)
      contents += new Label(effect.url.toString().substring(effect.url.toString().lastIndexOf("/")))
    }
  }
}

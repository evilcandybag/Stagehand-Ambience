package se.stagehand.plugins.ambience

import se.stagehand.swing.lib.EditorEffectItem
import se.stagehand.lib.scripting.Effect
import se.stagehand.swing.lib.TargetedPlayerEffectItem
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.Label
import se.stagehand.swing.assets.ImageAssets

object BackgroundMusicGUI extends URLEffectGUI[BackgroundMusic] {
  val peer = classOf[BackgroundMusic]
  
  def effectIcon = ImageAssets.NOTE_ICON
  
  def editorItem(e:Effect) = new BackgroundMusicEditorItem(checkEffect[peertype](e))
  def playerItem(e:Effect) = new BackgroundMusicPlayerItem(checkEffect[peertype](e))
  
  class BackgroundMusicEditorItem(e: peertype) extends URLEffectEditorItem(e) {
    def msg = "Select an URL for the desired music file."
    def ttl = "Pick an URL"
  }
  
  class BackgroundMusicPlayerItem(e: peertype) extends TargetedPlayerEffectItem(e) {
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
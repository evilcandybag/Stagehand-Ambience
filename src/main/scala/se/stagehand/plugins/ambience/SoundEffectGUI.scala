package se.stagehand.plugins.ambience

import se.stagehand.swing.lib.EditorEffectItem
import se.stagehand.lib.scripting.Effect
import se.stagehand.swing.lib.TargetedPlayerEffectItem
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.Label
import se.stagehand.swing.assets.ImageAssets

object SoundEffectGUI extends URLEffectGUI[SoundEffect] {
  val peer = classOf[SoundEffect]
  
  def effectIcon = ImageAssets.AUDIO_ICON
  
  def editorItem(e:Effect) = new SoundEffectEditorItem(checkEffect[peertype](e))
  def playerItem(e:Effect) = new SoundEffectPlayerItem(checkEffect[peertype](e))
  
  class SoundEffectEditorItem(e: peertype) extends URLEffectEditorItem(e) {
    def msg = "Select an URL for the desired music file."
    def ttl = "Pick an URL"
  }
  
  class SoundEffectPlayerItem(e: peertype) extends TargetedPlayerEffectItem(e) {
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
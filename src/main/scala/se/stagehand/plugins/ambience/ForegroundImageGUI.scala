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
import se.stagehand.swing.gui.BetterDialog
import scala.swing.BorderPanel
import scala.swing.TextField
import scala.swing.CheckBox
import scala.swing.event.ButtonClicked
import scala.swing.Button
import scala.swing.Action
import scala.swing.FlowPanel
import scala.swing.Separator

object ForegroundImageGUI extends URLEffectGUI[ForegroundImage] {
  val peer = classOf[ForegroundImage]
  
  def effectIcon = ImageAssets.NOTE_ICON
  
  def editorItem(e:Effect) = new ForegroundImageEditorItem(checkEffect[peertype](e))
  def playerItem(e:Effect) = new ForegroundImagePlayerItem(checkEffect[peertype](e))
  
  
  class ForegroundImageEditorItem(e: peertype) extends BoxPanel(Orientation.Horizontal) with EditorEffectItem[peertype] {
    def effect = e
    lazy val me = this
    
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
    val urllabel = new Label()
    contents += new Label("") {
      icon = effectIcon
    }
    val scalelabel = new Label()
    contents += urllabel
    contents += new Label(" ")
    contents += scalelabel
    
    reactions += {
      case e:MouseClicked => {
        val s = BetterDialog.inputDialog(new SelectorDialog)
        if (s._1 != "") try {
          val url = new URL(s._1)
          updateURL(url)
          updateScale(s._2)
        } catch {
          case e: MalformedURLException => Dialog.showMessage(message = s + " is not a valid URL.")
        }
      }
    }
    
    updateURL(effect.url)
    updateScale(effect.scale)
    tooltip = effect.componentName + ": " + effect.url.toString
    
    private def updateURL(url:URL) = {
      effect.url = url
      urllabel.text = url.getPath().substring(url.getPath.lastIndexOf('/'))
    }
    
    private def updateScale(o:(Option[Int],Option[Int])) = {
      effect.scale = o
      scalelabel.text = effect.showScale(o._1) + "/" + effect.showScale(o._2) 
    }
    
    class SelectorDialog extends BetterDialog.InputDialog[(String,(Option[Int],Option[Int]))] {
      private var _selection:(String,(Option[Int], Option[Int])) = ("",(None,None))
      def selected = _selection
      
      private def eStr(o: Option[Int])= o match {
        case None => ""
        case Some(i) => i.toString
      }
      
      val selector = new BoxPanel(Orientation.Vertical) {
        val url = new TextField(me.effect.url.toString)
        val xCheck = new CheckBox("x")
        val yCheck = new CheckBox("y")
        val xField = new TextField(eStr(me.effect.scale._1),4) {
          tooltip = "Scale image in x axis"
        }
        val yField = new TextField(eStr(me.effect.scale._2),4) {
          tooltip = "Scale image in y axis"
        }
        //Couple checkboxes with textfields
        listenTo(xCheck,yCheck)
        reactions += {
          case ButtonClicked(`xCheck`) => xField.enabled = xCheck.selected
          case ButtonClicked(`yCheck`) => yField.enabled = yCheck.selected
        }
        //Set enabled for components
        def options(o: Option[_], c: CheckBox, f: TextField) = o match {
          case None => {c.selected = false; f.enabled = false}
          case _ => {c.selected = true; f.enabled = true}
        }
        options(me.effect.scale._1,xCheck,xField)
        options(me.effect.scale._1,yCheck,yField)
        
        contents += url
        contents += new FlowPanel {
          contents += xCheck; contents += xField
        }
        contents += new FlowPanel {
          contents += yCheck; contents += yField
        }
      }
      val submit = new Button("Submit"){
        action = new Action("Submit") {
          def apply = {
            def scale(c:CheckBox, f:TextField) = {
              if (c.selected) Some(f.text.toInt) 
              else None
            }
            val url = selector.url.text
            val x = scale(selector.xCheck,selector.xField)
            val y = scale(selector.yCheck,selector.yField)
            _selection = (url,(x,y))
            close()
            dispose()
          }
        }
      }
      
      contents = new BorderPanel() {
        layout(selector) = BorderPanel.Position.Center
        layout(submit) = BorderPanel.Position.South
      }
      
    }
    revalidate
    repaint
  }
  
  class ForegroundImagePlayerItem(e: peertype) extends TargetedPlayerEffectItem(e) {
    def effectItem = new BoxPanel(Orientation.Vertical) {
      
      
      val icon = new Label("") {
        icon = effectIcon
      }
      val url = new Label(effect.url.getPath().substring(effect.url.getPath.lastIndexOf('/')))
      val scale = new Label(effect.showScale(effect.scale._1) + "/" + effect.showScale(effect.scale._2))
      
      contents += new FlowPanel {contents += icon; contents += url; vGap = 1}
      contents += new FlowPanel {contents += scale; vGap = 1}
    }
  }
}
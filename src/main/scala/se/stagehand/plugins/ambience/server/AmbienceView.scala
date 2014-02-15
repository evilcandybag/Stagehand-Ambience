package se.stagehand.plugins.ambience.server

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.stage.Stage
import scalafx.scene.web.WebView
import java.net.URL
import se.stagehand.lib.Log
import java.io.File
import se.stagehand.plugins.ambience.AmbiencePlugin
import se.stagehand.plugins.Plugin
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import se.stagehand.plugins.ambience.server.AmbienceProtocol.AmbienceAST
import javafx.event.EventHandler
import scala.swing.Dialog
import javafx.scene.web.WebEvent
import scalafx.scene.layout.Priority

object AmbienceView extends JFXApp {
  private val log = Log.getLog(this.getClass())
  
  val browser = new WebView {
    hgrow = Priority.ALWAYS
    vgrow = Priority.ALWAYS
    
    onKeyPressed = {e:KeyEvent => {
      
      e.code match {
        case KeyCode.J => {
          log.debug("KeyEvent " + e)
          log.debug("Starting Firebug console.")
          engine.delegate.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}")
        }
        case KeyCode.A => {
          engine.delegate.executeScript("alert(background + ' ' + blayer + ':' + foreground + ' ' + flayer)")
        }
        case KeyCode.F11 => {
          stage.fullScreen = !stage.fullScreen.value
          
        }
        case KeyCode.W => {
          log.debug(stage.width + "x" + stage.height)
        }
        case _ => log.debug("Unrecognized shit: " + e.code)
      }
      prefHeight <== stage.height
      prefWidth <== stage.width
    }}
    
    onAlert = new EventHandler[WebEvent[String]]() {
      def handle(e: WebEvent[String]) {
        Dialog.showMessage(message = e.data)
      }
    }
    
    onResized = (e: WebEvent[_]) => log.debug("onResized: " + e)
    log.debug("w " + width + prefWidth + " h " + height + prefHeight)
  }
  
  stage = new JFXApp.PrimaryStage {
    title = "Ambience Server"
      
    scene = new Scene {
      fill = Color.BLACK
      content = browser
      val url = Plugin.localResource(this, "page.html")
        
      load(url)
      
      val doc = browser.engine.delegate.getDocument()
      log.debug("" + resizable)
    }
    
  }
  
  def load(url:URL) {
    load(url.toExternalForm())
  }
  def load(url:String) {
    log.debug("Attempting to load URL: " + url)
    browser.engine.load(url)
  }
  def play(ast: AmbienceAST) {
//    log.debug("" + json )
    val fground = AmbienceProtocol.jsonString(ast.foreground)
    val bground = AmbienceProtocol.jsonString(ast.background)
    
    val pm = if (ast.mixin) "mixin" else "play"
      
    val mkstr = (p:String,div:String,fg:Boolean) => {
      (if (fg) "foreground" else "background") +
      "." + p + "(JSON.parse('" + div + "'))"
    }  
      
    browser.engine.delegate.executeScript(mkstr(pm,bground,false))
    browser.engine.delegate.executeScript(mkstr(pm,fground,true))
  }
  
  override def stopApp {
    super.stopApp
    System.exit(0)
  }
  
}
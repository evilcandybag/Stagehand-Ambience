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

object AmbienceView extends JFXApp {
  private val log = Log.getLog(this.getClass())
  
  val browser = new WebView {
    onKeyPressed = {e:KeyEvent => {
      
      e.code match {
        case KeyCode.J => {
          log.debug("KeyEvent " + e)
          log.debug("Starting Firebug console.")
          engine.delegate.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}")
        }
        case _ => log.debug("Unrecognized shit: " + e.code)
      }
      
    }}
  }
  
  stage = new JFXApp.PrimaryStage {
    title = "Ambience Server"
    scene = new Scene {
      fill = Color.BLACK
      content = browser
      val url = Plugin.localResource(this, "page.html")
        
      load(url)
    }
  }
  
  def load(url:URL) {
    load(url.toExternalForm())
  }
  def load(url:String) {
    log.debug("Attempting to load URL: " + url)
    browser.engine.load(url)
  }
  def play(json:String) {
//    log.debug("" + json )
    browser.engine.delegate.executeScript("background.play(JSON.parse('" + json + "'))")
  }
  
  override def stopApp {
    super.stopApp
    System.exit(0)
  }
  
}
package se.stagehand.plugins.ambience.server

import se.stagehand.lib.scripting.Target
import se.stagehand.lib.scripting.network.Capabilities
import se.stagehand.lib.scripting.network.Directives
import se.stagehand.lib.Log

object AmbienceProtocol {
  import Target._
  private val log = Log.getLog(this.getClass())
  
  def parse(args:Protocol.Arguments): AmbienceAST = {
    import Capabilities._
    import Directives._
    
    log.debug("ARGUMENTS: " + args)
    
    var mixin = false
    val media = for (kv <- args) yield kv match {
      case (IMG_BACKGROUND, v) => {
        Some(Right(Image(v,Size.Fill())))
      }
      case (IMG_FOREGROUND, v) => {
        val args = v.split(Target.Protocol.KEY_KEY)
        Some(Left(Image(args(0),Size.Scale(args(1),args(2)))))
      }
      case (MUSIC_BACKGROUND, v) => {
        Some(Right(Sound(v,true,true)))
      }
      case (SOUND_EFFECT, v) => {
        Some(Left(Sound(v,true,false)))
      }
      case (Directives.Persistence.ARG, v) => v match {
        case Persistence.CLEAR => {mixin = false; None}
        case Persistence.KEEP => {mixin = true; None}
      }
      case _ => None
    }
    var fg,bg = Set[AmbienceMedia]()
    for (m <- media.flatten) m match {
      case Right(e) => bg += e
      case Left(e) => fg += e
    }
    new AmbienceAST(fg,bg, mixin)
  }
  
  def jsonString(media:Set[AmbienceMedia]):String = {
    log.debug("MEDIA: " + media)
    var visual = false; 
    var its = (media.map( _ match {
	    case Image(p,s) => {
	      visual = true;
	      "\"image\": {" + List(jsonStr("url",p),jsonStyle(s)).mkString(",") + "}"
	    }
	    case Text() => ""
	    case Sound(p,s,r) => "\"sound\": {" + List(
	      jsonVal("overlap","1"),
	      jsonVal("volume", "1"),
	      jsonVal("tracks","[\"" + p + "\"]"),
	      jsonVal("loop","" + r),
	      jsonVal("shuffle", "" + s)
	    ).mkString(",") + "}"
    })) + jsonFade
    if (visual) its += jsonVisual
    "{" + its.mkString(",") + "}"
  }
  
  val jsonFade = jsonVal("fade","{ \"in\" : 0,\"out\" : 0}")
  val jsonVisual = jsonVal("isVisual","true")
  val jsonBg = jsonStr("background", "#000000")
    
  def jsonStyle(s:Size) = {
    "\"style\": {" + jsonStr("backgroundSize", s.toString) + "}"
  }
    
  object Media extends Enumeration {
    val image, sound, text = Value
  }
  private def jsonStr(key: String, value:String):String = jsonVal(key, "\"" + value + "\"")
  private def jsonVal(k: String, v: String) = "\"" + k + "\": " + v + " "
  
  class AmbienceAST(val foreground:Set[AmbienceMedia], val background: Set[AmbienceMedia], val mixin: Boolean) 
  
  sealed trait AmbienceMedia {
    override def equals(other:Any) = other match {
      case that: AmbienceMedia => {
        this.getClass == that.getClass
      }
      case _ => false 
    }
  }
  sealed trait Size 
  object Size {
    case class Fill extends Size {
      override def toString= "cover"
    }
    case class Fit extends Size {
      override def toString = "contain"
    }
    case class Scale(x:String,y:String) extends Size {
      override def toString = x + " " + y
    }
  }
  case class Image (
    path: String,
    size: Size
    ) extends AmbienceMedia
  
  case class Text extends AmbienceMedia
  
  case class Sound(
      path:String,
      shuffle:Boolean, 
      repeat:Boolean
      ) extends AmbienceMedia
}
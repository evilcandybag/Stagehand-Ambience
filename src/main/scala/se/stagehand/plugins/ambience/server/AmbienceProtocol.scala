package se.stagehand.plugins.ambience.server

import se.stagehand.lib.scripting.Target
import se.stagehand.lib.scripting.network.Capabilities

object AmbienceProtocol {
  import Target._
  def parse(args:Protocol.Arguments): AmbienceAST = {
    
    val media = for (kv <- args) yield kv match {
      case (Capabilities.IMG_BACKGROUND, v) => {
        Image(v,Size.Fill)
      }
      case _ => Text()
    }
    
    new AmbienceAST(media.toSeq:_*)
  }
  
  def jsonString(ast:AmbienceAST):String = "{" + (ast.media.map( _ match {
    case Image(p,s) => {
      "\"image\": {" + List(jsonItem("url",p),jsonStyle).mkString(",") + "}"
    }
    case Text() => ""
    case Sound() => ""
  }) + jsonVisual + jsonBg + jsonFade).mkString(",") + "}"
  
  def jsonFade = "\"fade\" : { \"in\" : 0,\"out\" : 0}"
  def jsonVisual = "\"isVisual\" : true"
  def jsonBg = "\"background\" : \"#000000\""
  def jsonStyle = "\"style\": {" + jsonItem("backgroundSize", "cover") + "}"
    
  object Media extends Enumeration {
    val image, sound, text = Value
  }
  private def jsonItem(key:String, value:String):String = "\"" + key + "\": \"" + value + "\""
  
  
  class AmbienceAST(private val things:AmbienceMedia*) {
    import Media._
    val media = things.toSet
  } 
  
  sealed trait AmbienceMedia {
    override def equals(other:Any) = other match {
      case that: AmbienceMedia => {
        this.getClass == that.getClass
      }
      case _ => false 
    }
  }
  object Size extends Enumeration {
      val Fill = Value("cover")
      val Fit = Value("contain")
  }
  case class Image (
    path: String,
    size: Size.Value
  ) extends AmbienceMedia
  
  case class Text extends AmbienceMedia
  
  case class Sound extends AmbienceMedia
}
/**
 * Created by tmnd91 on 31/03/15.
 */

import java.nio.channels.Channels

import scala.reflect.internal.util.StringOps
import scala.xml._
import java.io._
object Main extends App{
  import implicits._
  val path = new File(args(0))
  if (path.isDirectory){
    val x = <plist version="1.0"><array>{path.categories.map(_.toXml())}</array></plist>
    save(x,"output.plist")
  }
  else{
    println("arg0 must be a directory")
  }

  def save(node: Node, fileName: String) = {
    val Encoding = "UTF-8"
    val pp = new PrettyPrinter(80, 2)
    val fos = new FileOutputStream(fileName)
    val writer = Channels.newWriter(fos.getChannel(), Encoding)

    try {
      writer.write(s"""<?xml version='1.0' encoding='$Encoding'?>
          |<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
          |""".stripMargin('|'))
      writer.write(pp.format(node))
    } finally {
      writer.close()
    }

    fileName
  }

  object implicits{
    implicit class RichFile(f : File){
      def listDirectories : Seq[File] = f.listFiles().filter(_.isDirectory)
      def listNonEmptyDirectories : Seq[File] = f.listFiles().filter(x => x.isDirectory && x.listFiles().filter(_.isFile).nonEmpty)
      def categories : Seq[Category] = {
        require(f.isDirectory)
        val cats = f.listNonEmptyDirectories
        require(cats.nonEmpty)
        for (c <- cats) yield {
          Category(c.nameNoExtension,registrazioni(c))
        }
      }
      private def registrazioni(ff : File) : Seq[Registration] = {
        val filtered = ff.listFiles().filter(x => x.extension == "mp3" || x.extension == "m4a")
        for (r <- filtered)yield {
          Registration(r.nameNoExtension, r.extension, r.nameNoExtension.replace('_',' ').trim)
        }
      }

      def nameNoExtension : String = {
        val splitted = f.getName().split('.')
        splitted.length match{
          case 0 => throw new Exception("Does this happen?")
          case 1 => splitted.head
          case _ => splitted.dropRight(1).mkString(".")
        }
      }
      def extension : String = {
        val splitted = f.getName().split('.')
        splitted.length match{
          case 0 => throw new Exception("Does this happen?")
          case 1 => ""
          case _ => splitted.last
        }
      }
    }
  }
}

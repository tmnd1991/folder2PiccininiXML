/**
 * Created by tmnd91 on 31/03/15.
 */
case class Registration(filename: String, fileType : String, name : String){
  def toXml() = {
   <dict>
     <key>filename</key>
     <string>{xml.Utility.escape(filename)}</string>
     <key>filetype</key>
     <string>{xml.Utility.escape(fileType)}</string>
     <key>name</key>
     <string>{xml.Utility.escape(name)}</string>
   </dict>
  }
}
/**
 * Created by tmnd91 on 31/03/15.
 */
case class Category(name : String, registrazioni : Seq[Registration]){
  def toXml() = {
   <dict>
     <key>name</key>
     <string>{xml.Utility.escape(name)}</string>
     <key>registrazioni</key>
     <array>
       {registrazioni.map(_.toXml)}
     </array>
   </dict>
  }
}
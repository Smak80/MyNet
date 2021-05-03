import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket
import kotlin.concurrent.thread

class SocketIO(val socket: Socket) {

    private var stop = false
    private val socketClosedListener = mutableListOf<()->Unit>()

    fun addSocketClosedListener(l: ()->Unit){
        socketClosedListener.add(l)
    }

    fun removeSocketClosedListener(l: ()->Unit){
        socketClosedListener.remove(l)
    }

    fun stop(){
        stop = true
        socket.close()
    }

    fun startDataReceiving() {
        stop = false
        thread{
            try {
                val br = BufferedReader(InputStreamReader(socket.getInputStream()))
                while (!stop) {
                    val data = br.readLine()
                    if (data!=null)
                        println(data)
                    else {
                        throw IOException("Связь прервалась")
                    }
                }
            } catch (ex: Exception){
                println(ex.message)
            }
            finally {
                socket.close()
                socketClosedListener.forEach{it()}
            }
        }
    }

    fun sendData(data: String): Boolean{
        try {
            val pw = PrintWriter(socket.getOutputStream())
            pw.println(data)
            pw.flush()
            return true
        } catch (ex: Exception){
            return false
        }
    }
}
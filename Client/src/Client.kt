import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class Client(
    val host: String,
    val port: Int
) {
    private val socket: Socket
    private val communicator: SocketIO
    init{
        socket = Socket(host, port)
        communicator = SocketIO(socket)
    }

    fun stop(){
        communicator.stop()
    }

    fun start(){
        communicator.startDataReceiving()
    }

    fun send(data: String) {
        communicator.sendData(data)
    }

    fun addSessionFinishedListener(l: ()->Unit){
        communicator.addSocketClosedListener(l)
    }

    fun removeSessionFinishedListener(l: ()->Unit){
        communicator.removeSocketClosedListener(l)
    }
}
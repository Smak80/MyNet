import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server(port: Int = 5804) {
    private val sSocket: ServerSocket
    private val clients = mutableListOf<Client>()
    private var stop = false

    inner class Client(val socket: Socket){
        private var sio: SocketIO? = null
        fun startDialog(){
            sio = SocketIO(socket).apply{
                addSocketClosedListener {
                    clients.remove(this@Client)
                }
                startDataReceiving()
            }
        }

        fun stop(){
            sio?.stop()
        }
    }

    init{
        sSocket = ServerSocket(port)
    }

    fun stop(){
        sSocket.close()
        stop = true
    }

    fun start() {
        stop = false
        thread {
            try {
                while (!stop) {
                    clients.add(
                        Client(
                            sSocket.accept()
                        ).also { client -> client.startDialog() })
                }
            } catch (e: Exception){
                println("${e.message}")
            } finally {
                stopAllClients()
                sSocket.close()
                println("Сервер остановлен.")
            }
        }
    }

    private fun stopAllClients(){
        clients.forEach { client -> client.stop() }
    }
}
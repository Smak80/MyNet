import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val client = Client("localhost", 5804)
    client.start()
    val br = BufferedReader(
        InputStreamReader(System.`in`))
    var data: String
    client.addSessionFinishedListener {
        println("Работа с сервером завершена. Нажмите Enter для выхода...")
        br.close()
    }

    try{
        do{
            data = br.readLine()
            client.send(data)
        } while (data != "STOP")
    } catch (e: Exception){
        println("${e.message}")
    } finally {
        client.stop()
    }
}
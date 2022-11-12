package Gneiss.PacketCompiler

import Gneiss.PacketCompiler.DatabaseAccess.PacketDao
import Gneiss.PacketCompiler.Helpers.JWTHelper
import Gneiss.PacketCompiler.Helpers.JsonSerializer
import Gneiss.PacketCompiler.Helpers.PDFHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class PacketCompilerApplication

@Bean
fun jwtHelper(): JWTHelper {
    return JWTHelper()
}

@Bean
fun pdfHelper(): PDFHelper {
    return PDFHelper()
}

@Bean
fun packetDao(): PacketDao {
    return PacketDao(jsonSerializer())
}

@Bean
fun jsonSerializer(): JsonSerializer {
    return JsonSerializer()
}

fun main(args: Array<String>) {
    runApplication<PacketCompilerApplication>(*args)
}

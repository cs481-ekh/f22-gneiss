package Gneiss.PacketCompiler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class PacketCompilerApplication

fun main(args: Array<String>) {
    runApplication<PacketCompilerApplication>(*args)
}

package Gneiss.PacketCompiler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.*

@SpringBootApplication
class PacketCompilerApplication

fun main(args: Array<String>) {
	runApplication<PacketCompilerApplication>(*args)
}

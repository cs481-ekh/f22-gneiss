package Gneiss.PacketCompiler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PacketCompilerApplication

fun main(args: Array<String>) {
	runApplication<PacketCompilerApplication>(*args)
}

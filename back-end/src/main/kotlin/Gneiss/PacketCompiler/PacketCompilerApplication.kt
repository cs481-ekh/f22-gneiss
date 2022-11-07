package Gneiss.PacketCompiler

import Gneiss.PacketCompiler.Helpers.JWTHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class PacketCompilerApplication 

fun main(args: Array<String>) {
    runApplication<PacketCompilerApplication>(*args)
    //val jwtHelper: JWTHelper = context.getBean(JWTHelper::class.java)
}

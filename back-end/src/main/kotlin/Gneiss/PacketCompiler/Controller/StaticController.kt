package Gneiss.PacketCompiler.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.boot.web.servlet.error.ErrorController

@Controller
class StaticController: ErrorController {

    @RequestMapping("/error")
    fun index():String {
        return "index.html";
    }

    fun getErrorPath():String {
        return "index.html";
    }
}

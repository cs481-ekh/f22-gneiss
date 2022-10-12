package Gneiss.PacketCompiler.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
public class StaticController {
    @RequestMapping("/{path:[^\\.]+}")
    fun index(): String {
        return "index.html"
    }
}

package Gneiss.PacketCompiler.Service

class CreateTestRequest(val name: String)
class CreateTestResponse(val name: String)

class Test {
    companion object {
        fun CreateTest(req: CreateTestRequest): CreateTestResponse {
            var res = "mutated" + req.name
            return CreateTestResponse(res)
        }
    }
}
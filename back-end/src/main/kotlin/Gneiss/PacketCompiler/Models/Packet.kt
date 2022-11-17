package Gneiss.PacketCompiler.Models

data class Packet(
    var name: String,
    var invoicePDFPath: String,
    var approvalPDFPath: String,
    var csvPDFPath: String,
    var compiledPDFPath: String
)

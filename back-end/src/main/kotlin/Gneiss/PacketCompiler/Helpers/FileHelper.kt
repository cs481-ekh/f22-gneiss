package Gneiss.PacketCompiler.Helpers

import Gneiss.PacketCompiler.Helpers.InputFileHelper
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.List

interface FileHelper {
    fun uploadFiles(files: List<MultipartFile>): List<InputFileHelper>
}
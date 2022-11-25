package com.app.controller

import Gneiss.PacketCompiler.Helpers.InputFileHelper
import Gneiss.PacketCompiler.Helpers.FileHelper
import lombok.RequiredArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.List

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private val logger = LoggerFactory.getLogger(javaClass)

    lateinit private var fileService: FileHelper

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun addFile(@RequestParam("files") files: List<MultipartFile>): List<InputFileHelper> {
        logger.debug("Call addFile API")
        return fileService.uploadFiles(files)
    }
}
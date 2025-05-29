package com.ggomg.imagebff.image.api

import com.ggomg.imagebff.image.application.ImageService
import com.ggomg.imagebff.image.model.PresignedUploadUrl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "Image", description = "이미지 presigned URL 처리")
@RestController
@RequestMapping("/image")
class ImageController(private val imageService: ImageService) {

    @Operation(summary = "단일 이미지 uploadPresignedURL 요청", description = "단일 이미지 presigned upload URL 요청")
    @PostMapping("/upload-url")
    fun generatePresignedUrl(
        @RequestParam filename: String,
        @RequestParam contentType: String
    ): PresignedUploadUrl {
        return imageService.save(filename, contentType)
    }

    @Operation(summary = "다중 이미지 uploadPresignedURL 요청", description = "다중 이미지 presigned upload URL 요청")
    @PostMapping("/upload-urls")
    fun generatePresignedUrls(
        @RequestParam filenames: List<String>,
        @RequestParam contentTypes: List<String>
    ): List<PresignedUploadUrl> {
        return imageService.saveAll(filenames, contentTypes)
    }

    @Operation(summary = "업로드 완료 알림 (단일)", description = "단일 이미지 업로드 완료를 알림")
    @PostMapping("/confirm-upload/{imageId}")
    fun confirmUpload(@PathVariable imageId: UUID) {

        imageService.confirmUpload(imageId)
    }

    @Operation(summary = "업로드 완료 알림 (다중)", description = "다중 이미지 업로드 완료를 알림")
    @PostMapping("/confirm-uploads")
    fun confirmUploads(@RequestBody imageIds: List<UUID>) {
        imageService.confirmAllUploads(imageIds)
    }

    @Operation(summary = "단일 이미지 downloadPresignedURL 요청", description = "단일 이미지 다운로드용 presigned URL 요청")
    @GetMapping("/{imageId}")
    fun getPresignedDownloadUrl(@PathVariable imageId: UUID): PresignedUploadUrl {
        return imageService.read(imageId)
    }

    @Operation(summary = "다중 이미지 downloadPresignedURL 요청", description = "다중 이미지 다운로드용 presigned URL 요청")
    @PostMapping("/downloads")
    fun getPresignedDownloadUrls(@RequestBody imageIds: List<UUID>): List<PresignedUploadUrl> {
        return imageService.readAll(imageIds)
    }
}

package com.rx.services;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.dao.Document;
import com.rx.dao.DocumentType;
import com.rx.dao.repositories.DocumentRepository;
import com.rx.dto.FileDownloadResultDto;
import com.rx.dto.FileUploadResultDto;
import com.rx.helpers.FileStorageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.rx.dto.FileDownloadResultDto.FileDownloadResultDtoBuilder;
import static com.rx.dto.FileUploadResultDto.FileUploadResultDtoBuilder;

@Service
public class FileStorageService {

    private static final Logger LOGGER = LogManager.getLogger(FileStorageService.class);

    private DocumentRepository documentRepository;
    private FileStorageHelper fileStorageHelper;

    @Autowired
    public FileStorageService(DocumentRepository documentRepository, FileStorageHelper fileStorageHelper) {
        this.documentRepository = documentRepository;
        this.fileStorageHelper = fileStorageHelper;
    }

    public FileUploadResultDto saveFileInStorage(MultipartFile file, DocumentType documentType) {
        String uploadedFilename = fileStorageHelper.saveNewFile(file);
        Document document = Document.builder()
                .withDocumentFilename(uploadedFilename)
                .withDocumentType(documentType)
                .build();

        Long fileId = documentRepository.save(document).getId(); //метод save возращает тот же объект что мы добавляем

        return new FileUploadResultDtoBuilder().withFileUUID(fileId).build();
    }

    public FileDownloadResultDto getFileFromStorageById(Long fileId) {
        if (fileId == null) {
            throw new FileDownloadNotFoundException("fileId is missing!");
        }

        Document document = this.documentRepository.findOne(fileId);

        if (document == null) {
            throw new FileDownloadNotFoundException("No file was found by fileId. fileId=" + fileId);
        }

        File file = fileStorageHelper.getFileByName(document.getDocumentFilename());
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        return new FileDownloadResultDtoBuilder().withFileResource(fileSystemResource).build();
    }
}

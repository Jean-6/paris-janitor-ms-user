package com.example.parisjanitormsuser.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentPublisher {

    void publishDocs(Long userId, List<MultipartFile> identity, List<MultipartFile> siret, List<MultipartFile> rib);
    void send(Long userId, List<MultipartFile> files, String type, String routingKey);

}

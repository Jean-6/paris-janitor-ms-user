package com.example.parisjanitormsuser.service.impl;

import com.example.parisjanitormsuser.config.Rabbit;
import com.example.parisjanitormsuser.dto.DocumentMsg;
import com.example.parisjanitormsuser.dto.FileType;
import com.example.parisjanitormsuser.service.DocumentPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
public class DocumentPublisherImp implements DocumentPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    private Rabbit rabbit;


    @Override
    public void publishDocs(Long userId, List<MultipartFile> identity, List<MultipartFile> siret, List<MultipartFile> rib) {

        send(userId, identity, FileType.IDENTITY.toString(), "docs.identity");
        send(userId, siret, FileType.SIRET.toString(), "docs.siret");
        send(userId, rib, FileType.RIB.toString(), "docs.rib");
    }

    @Override
    public void send(Long userId, List<MultipartFile> files, String type, String routingKey) {

        try{
            for(MultipartFile f :  files){
                DocumentMsg msg = new DocumentMsg(userId,type,"", f.getBytes(), f.getOriginalFilename());
                log.info("Sending message to docs.rib : {}", msg);
                rabbitTemplate.convertAndSend(Rabbit.EXCHANGE, routingKey, msg);
            }
        }catch(IOException ex){
            log.error("");
        }
    }
}

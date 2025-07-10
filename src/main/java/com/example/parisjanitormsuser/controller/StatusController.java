package com.example.parisjanitormsuser.controller;

import com.example.parisjanitormsuser.dto.ResponseWrapper;
import com.example.parisjanitormsuser.entity.Availability;
import com.example.parisjanitormsuser.entity.Company;
import com.example.parisjanitormsuser.entity.ProviderInfo;
import com.example.parisjanitormsuser.service.DocumentPublisher;
import com.example.parisjanitormsuser.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/status")
@RequiredArgsConstructor
@Tag(name = "Status Controller", description = "Gestion des demandes de status")
public class StatusController {

    @Autowired
    private DocumentPublisher documentPublisher;
    @Autowired
    private ProviderService providerService;


    @Operation(
            summary = "Request to change status",
            description = "Allows a user to submit a status change request by providing information about the company, services, departments, availabilities, and associated documents."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request successfully processed"),
            @ApiResponse(responseCode = "400", description = "Invalid data or bad request format"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
    })
    @PostMapping(value = "/change", produces = MediaType.APPLICATION_JSON_VALUE) //.MULTIPART_FORM_DATA_VALUE
    public ResponseEntity<ResponseWrapper<ProviderInfo>> requestForChangeStatus(
            @RequestPart("userId") Long userId,
            @RequestPart("company") Company company,
            @RequestPart("services") List<String> services,
            @RequestPart("departments") List<String> departments,
            @RequestPart("availabilities") List<Availability> availabilities,
            @RequestPart("identityDocs") List<MultipartFile> identityDocs,
            @RequestPart("siretDocs") List<MultipartFile> siretDocs,
            @RequestPart("ribDocs") List<MultipartFile> ribDocs,
            HttpServletRequest request) {

        log.debug("userId" + userId.toString());

        documentPublisher.publishDocs(userId, identityDocs, siretDocs, ribDocs);
        ProviderInfo providerInfo = this.providerService.saveProviderInfo(userId, company, services, departments, availabilities);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseWrapper.ok("Demande éffectuée avec succès", request.getRequestURI(),providerInfo));
    }
}
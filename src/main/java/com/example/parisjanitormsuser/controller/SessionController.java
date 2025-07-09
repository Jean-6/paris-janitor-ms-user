package com.example.parisjanitormsuser.controller;


import com.example.parisjanitormsuser.dto.ResponseWrapper;
import com.example.parisjanitormsuser.entity.Session;

import com.example.parisjanitormsuser.service.SessionService;
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
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Session Controller", description = "Responsible for managing user sessions, including authentication, session duration and security mechanisms")
public class SessionController {

    @Autowired
    private SessionService sessionService;


    @Operation(
            summary = "Register user",
            description = "Registers a new user session and returns session details in a JSON wrapper"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered user",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Session service not found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "204", description = "No content to return")
    })
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<Session>> save(@RequestBody Session session, HttpServletRequest request) {
        log.debug("Save user session ");
        Session session1 = sessionService.save(session);
        return ResponseEntity.ok().body(ResponseWrapper.ok("Utilisateur sauvegardé",request.getRequestURI(),session1));

    }

    @Operation(
            summary = "Get user session by ID",
            description = "Fetches a user session by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Session found successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            )
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<Session>> getById(@PathVariable Long id, HttpServletRequest request) {

        Optional<Session> optionalSession = this.sessionService.findById(id);
        return optionalSession.map(session -> ResponseEntity.ok().body(ResponseWrapper.ok("",request.getRequestURI(),session)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseWrapper.error(null, "Session not found", HttpStatus.NOT_FOUND.value(), request.getRequestURI())));
    }

    @Operation(
            summary = "Get all sessions",
            description = "Retrieves a list of all user sessions"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of sessions retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            )
    })
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<List<Session>>> getAll(HttpServletRequest request) {
        log.debug("Get all sessions");
        List<Session> sessions = sessionService.findAll();
        return ResponseEntity.ok().body(ResponseWrapper.ok("Liste des sessions", request.getRequestURI(),sessions));
    }

    @Operation(
            summary = "Delete session by ID",
            description = "Deletes a user session identified by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Session deleted successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            )
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<Void>> deleteById(@RequestParam Long id, HttpServletRequest request) {
        sessionService.deleteSessionById(id);
        return ResponseEntity.ok().body(ResponseWrapper.ok("Session deleted successfully", request.getRequestURI()));
    }


}

package com.luizalabs.api.handler;

import com.luizalabs.exception.ResourceDuplicatedException;
import com.luizalabs.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());

    List<Map<String, String>> errors =
        exception.getBindingResult().getFieldErrors().stream()
            .map(x -> Map.of(x.getField(), x.getDefaultMessage()))
            .collect(Collectors.toList());

    body.put("errors", errors);

    return ResponseEntity.badRequest().body(body);
  }

  @ResponseBody
  @ExceptionHandler(ResourceNotFoundException.class)
  protected ResponseEntity handleCustomerNotFoundException(ResourceNotFoundException ex) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.NOT_FOUND.value());

    List<String> errors = List.of(ex.getMessage());
    body.put("errors", errors);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ResponseBody
  @ExceptionHandler(ResourceDuplicatedException.class)
  protected ResponseEntity handleCustomerNotFoundException(ResourceDuplicatedException ex) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());

    List<String> errors = List.of(ex.getMessage());
    body.put("errors", errors);

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
  }
}


package com.example.fstree.web;
import jakarta.persistence.EntityNotFoundException; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String,Object>> notFound(EntityNotFoundException ex){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","NOT_FOUND","message",ex.getMessage()));
  }
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,Object>> bad(IllegalArgumentException ex){
    return ResponseEntity.badRequest().body(Map.of("error","BAD_REQUEST","message",ex.getMessage()));
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException ex){
    var errors = ex.getBindingResult().getFieldErrors().stream().map(f->Map.of("field",f.getField(),"msg",f.getDefaultMessage())).toList();
    return ResponseEntity.badRequest().body(Map.of("error","VALIDATION","details",errors));
  }
}

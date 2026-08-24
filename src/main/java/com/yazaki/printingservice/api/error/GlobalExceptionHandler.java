package com.yazaki.printingservice.api.error;
import java.time.Instant; import java.util.*;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import jakarta.servlet.http.HttpServletRequest;
@RestControllerAdvice
public class GlobalExceptionHandler {
 public record ApiError(Instant timestamp,int status,String code,String message,String path,Map<String,String> fieldErrors) {}
 @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<ApiError> notFound(RuntimeException e,HttpServletRequest r){return error(HttpStatus.NOT_FOUND,"NOT_FOUND",e,r,Map.of());}
 @ExceptionHandler(ConflictException.class) ResponseEntity<ApiError> conflict(RuntimeException e,HttpServletRequest r){return error(HttpStatus.CONFLICT,"CONFLICT",e,r,Map.of());}
 @ExceptionHandler(BusinessRuleException.class) ResponseEntity<ApiError> rule(RuntimeException e,HttpServletRequest r){return error(HttpStatus.UNPROCESSABLE_ENTITY,"BUSINESS_RULE",e,r,Map.of());}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiError> validation(MethodArgumentNotValidException e,HttpServletRequest r){
  Map<String,String> fields=new LinkedHashMap<>(); e.getBindingResult().getFieldErrors().forEach(x->fields.putIfAbsent(x.getField(),x.getDefaultMessage()));
  return error(HttpStatus.BAD_REQUEST,"VALIDATION_ERROR",new RuntimeException("Request validation failed"),r,fields); }
 @ExceptionHandler(Exception.class) ResponseEntity<ApiError> unexpected(Exception e,HttpServletRequest r){return error(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_ERROR",new RuntimeException("Unexpected server error"),r,Map.of());}
 private ResponseEntity<ApiError> error(HttpStatus s,String c,RuntimeException e,HttpServletRequest r,Map<String,String> f){return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),c,e.getMessage(),r.getRequestURI(),f));}
}

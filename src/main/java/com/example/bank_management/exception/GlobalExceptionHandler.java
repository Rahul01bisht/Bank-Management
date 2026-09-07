package com.example.bank_management.exeption;

import com.example.bank_management.dto.ExceptionResponseDto;
import com.example.bank_management.dto.ValidExceptionResponse;

import com.example.bank_management.model.TransactionType;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.HashMap;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler{



  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ExceptionResponseDto> handleRuntimeExeption(RuntimeException ex){

    ExceptionResponseDto data =
      new ExceptionResponseDto(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR
          .getReasonPhrase(),
        ex.getMessage(),
        TransactionType.FAILED,
        LocalDateTime.now()
    );
    
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(data);
  }

  

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponseDto> handleRuntimeExeption(Exception ex){

    ExceptionResponseDto data =
      new ExceptionResponseDto(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR
          .getReasonPhrase(),
        "Something went wrong, Pls try again later",
        TransactionType.FAILED,
        LocalDateTime.now()
    );
    
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(data);
  }

  
    @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ExceptionResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex){

    ExceptionResponseDto data =
      new ExceptionResponseDto(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        TransactionType.FAILED,
        LocalDateTime.now()
    );
    
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(data);
  }

  

  @ExceptionHandler(LowBalanceException.class)
  public ResponseEntity<ExceptionResponseDto> handleLowBalanceException(LowBalanceException ex){

    ExceptionResponseDto data =
      new ExceptionResponseDto(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ex.getMessage(),
        TransactionType.FAILED,
        LocalDateTime.now()
    );
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(data);
  }


  @ExceptionHandler(SameAccountTransferException.class)
  public ResponseEntity<ExceptionResponseDto> handleSameAccountTransferException(SameAccountTransferException ex){

    ExceptionResponseDto data =
      new ExceptionResponseDto(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ex.getMessage(),
        TransactionType.FAILED,
        LocalDateTime.now()
    );
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(data);
  }



  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

    Map<String, String> fieldError = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
      .forEach(error -> fieldError.put(
        error.getField() , error.getDefaultMessage()
      ));

    ValidExceptionResponse data =
      new ValidExceptionResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "Validatin Failed",
        TransactionType.FAILED,
        LocalDateTime.now(),
        fieldError
    );
    
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(data);
  }
  
}
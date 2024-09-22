package com.example.snackordering.util;

import com.example.snackordering.model.ResponseModel.MetaDataDTO;
import com.example.snackordering.model.ResponseModel.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static ResponseEntity<ResponseDTO> getObject(Object result, HttpStatus status, String response) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .details(ExceptionUtils.getResponseString(response))
                        .content(result)
                        .statusCode(status.value())
                        .build()
                , status
        );
    }

    public static ResponseEntity<?> getCollection(Object result, HttpStatus status, String response
            , MetaDataDTO metaData) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .statusCode(status.value())
                        .details(ExceptionUtils.getResponseString(response))
                        .content(result)
                        .metaDataDTO(metaData)
                        .build()
                , status
        );
    }

    public static ResponseEntity<?> error(String error, String message, HttpStatus status) {
        return new ResponseEntity<>(
                ResponseDTO.builder()
                        .details(ExceptionUtils.getError(error))
                        .statusCode(status.value())
                        .build()
                , status
        );
    }
}

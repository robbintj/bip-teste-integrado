package com.example.backend.exception;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Estrutura padrao de resposta de erro da API.
 * Segue RFC 7807 (Problem Details for HTTP APIs).
 * 
 * @author Robert R Serra Java Fullstack Developer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private List<String> details;
}

package capprezy.ua.controller.exception;

import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.controller.exception.model.ApiError;
import capprezy.ua.controller.exception.model.NotValidDataException;
import capprezy.ua.controller.exception.model.PermissionException;
import javassist.NotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AlreadyExistsException.class
    })
    private ResponseEntity<ApiError> dataAccessException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new ApiError(ex.getMessage()), headers, HttpStatus.CONFLICT);

    }

    @ExceptionHandler({
            NotFoundException.class,
            UsernameNotFoundException.class
    })
    private ResponseEntity<ApiError> handleNotFoundException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new ApiError(ex.getMessage()), headers, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({
            PermissionException.class
    })
    private ResponseEntity<ApiError> handleForbiddenException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new ApiError(ex.getMessage()), headers, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ NotValidDataException.class})
    private ResponseEntity<ApiError> handleNotValidData(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new ApiError(ex.getMessage()), headers, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler()
    private ResponseEntity<Object> handleNotValidArgumentException(MethodArgumentNotValidException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.NOT_ACCEPTABLE);

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);
        body.put("message", errors.get(0));

        return new ResponseEntity<>(body, headers, HttpStatus.NOT_ACCEPTABLE);
    }


}
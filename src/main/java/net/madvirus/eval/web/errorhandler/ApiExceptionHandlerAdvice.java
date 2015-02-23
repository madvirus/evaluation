package net.madvirus.eval.web.errorhandler;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotYetOpenedException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.SelfEvalNotYetFinishedException;
import net.madvirus.eval.api.personaleval.YouAreNotRateeException;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.api.personaleval.second.YouAreNotSecondRaterException;
import net.madvirus.eval.api.user.MappingUserIdNotFoundException;
import org.axonframework.repository.AggregateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "net.madvirus.eval.web.restapi")
public class ApiExceptionHandlerAdvice {
    private Logger logger = LoggerFactory.getLogger(ApiExceptionHandlerAdvice.class);

    @ExceptionHandler
    public ResponseEntity rateeNotFound(RateeNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity youAreNotRatee(YouAreNotRateeException ex) {
        logger.warn("youAreNotRatee", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler
    public ResponseEntity evalSeasonNotFound(EvalSeasonNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity aggregateNotFound(AggregateNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity mappingUserIdNotFound(MappingUserIdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
    }

    @ExceptionHandler
    public ResponseEntity duplicateId(DuplicateIdException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler
    public ResponseEntity evalSeasonNotYetOpened(EvalSeasonNotYetOpenedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler
    public ResponseEntity personalEvalNotFount(PersonalEvalNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity selfEvalNotYetFinished(SelfEvalNotYetFinishedException ex) {
        // TODO Not Found가 맞을까??
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity youAreNotFirstRater(YouAreNotFirstRaterException ex) {
        logger.warn("youAreNotFirstRater", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler
    public ResponseEntity youAreNotSecondRater(YouAreNotSecondRaterException ex) {
        logger.warn("youAreNotSecondRater", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler
    public ResponseEntity commonError(Exception ex) {
        logger.warn(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ex));
    }

    private class ErrorResponse {

        private Exception ex;

        public ErrorResponse(Exception ex) {
            this.ex = ex;
        }

        public String getExceptionType() {
            return ex.getClass().getSimpleName();
        }

        public String getMessage() {
            return ex.getMessage();
        }
    }
}

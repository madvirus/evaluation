package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotYetOpenedException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.SelfEvalNotYetFinishedException;
import net.madvirus.eval.api.personaleval.YouAreNotRateeException;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.api.user.MappingUserIdNotFoundException;
import org.axonframework.repository.AggregateNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    public ResponseEntity rateeNotFound(RateeNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity youAreNotRatee(YouAreNotRateeException ex) {
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
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler
    public ResponseEntity commonError(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
    }
}

package net.madvirus.eval.web.errorhandler;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.SelfEvalNotYetFinishedException;
import net.madvirus.eval.api.personaleval.YouAreNotRateeException;
import net.madvirus.eval.api.personaleval.colleague.YouAreNotColleagueRaterException;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.api.personaleval.second.YouAreNotSecondRaterException;
import net.madvirus.eval.api.user.MappingUserIdNotFoundException;
import org.axonframework.repository.AggregateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice(basePackages = "net.madvirus.eval.web.user")
public class UserExceptionHandlerAdvice {
    private Logger logger = LoggerFactory.getLogger(UserExceptionHandlerAdvice.class);

    @ExceptionHandler
    public void rateeNotFound(RateeNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler
    public void youAreNotRatee(YouAreNotRateeException ex, HttpServletResponse response) throws IOException {
        logger.warn("youAreNotRatee", ex);
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @ExceptionHandler
    public void evalSeasonNotFound(EvalSeasonNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler
    public void aggregateNotFound(AggregateNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler
    public void mappingUserIdNotFound(MappingUserIdNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }


    @ExceptionHandler
    public void personalEvalNotFount(PersonalEvalNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler
    public void selfEvalNotYetFinished(SelfEvalNotYetFinishedException ex, HttpServletResponse response) throws IOException {
        // TODO Not Found가 맞을까??
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler
    public void youAreNotColleagueRater(YouAreNotColleagueRaterException ex, HttpServletResponse response) throws IOException {
        logger.warn("youAreNotColleagueRater", ex);
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @ExceptionHandler
    public void youAreNotFirstRater(YouAreNotFirstRaterException ex, HttpServletResponse response) throws IOException {
        logger.warn("youAreNotFirstRater", ex);
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @ExceptionHandler
    public void youAreNotSecondRater(YouAreNotSecondRaterException ex, HttpServletResponse response) throws IOException {
        logger.warn("youAreNotSecondRater", ex);
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @ExceptionHandler
    public void commonError(Exception ex, HttpServletResponse response) throws IOException {
        logger.warn(ex.getMessage(), ex);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}

package net.madvirus.eval.axon;

import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.TransactionManager;
import org.axonframework.unitofwork.UnitOfWork;

import java.util.concurrent.Callable;

public abstract class AxonUtil {
    public static void runInUOW(Runnable runnable) {
        UnitOfWork uow = DefaultUnitOfWork.startAndGet();
        try {
            runnable.run();
            uow.commit();
        } catch (RuntimeException ex) {
            uow.rollback(ex);
            throw ex;
        } catch (Exception ex) {
            uow.rollback(ex);
            throw ex;
        }
    }

    public static void runInUOW(TransactionManager tx, Runnable runnable) {
        UnitOfWork uow = DefaultUnitOfWork.startAndGet(tx);
        try {
            runnable.run();
            uow.commit();
        } catch (RuntimeException ex) {
            uow.rollback(ex);
            throw ex;
        } catch (Exception ex) {
            uow.rollback(ex);
            throw ex;
        }
    }

    public static <T> T runInUOW(Callable<T> callable) {
        UnitOfWork uow = DefaultUnitOfWork.startAndGet();
        try {
            T result = callable.call();
            uow.commit();
            return result;
        } catch (RuntimeException ex) {
            uow.rollback(ex);
            throw ex;
        } catch (Exception ex) {
            uow.rollback(ex);
            throw new RuntimeException(ex);
        }
    }
}

package net.madvirus.eval.axon;

import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;

public abstract class AxonUtil {
    public static void runInUOW(Runnable runnable) {
        UnitOfWork uow = DefaultUnitOfWork.startAndGet();
        try {
            runnable.run();
            uow.commit();
        } catch (Exception ex) {
            uow.rollback(ex);
            throw ex;
        }
    }
}

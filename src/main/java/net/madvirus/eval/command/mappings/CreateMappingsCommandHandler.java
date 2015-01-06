package net.madvirus.eval.command.mappings;

import net.madvirus.eval.api.CreateMappingsCommand;
import net.madvirus.eval.api.DuplicateIdException;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;

public class CreateMappingsCommandHandler {
    private Repository<Mappings> mappingInfoRepository;

    public CreateMappingsCommandHandler(Repository<Mappings> repository) {
        this.mappingInfoRepository = repository;
    }

    @CommandHandler
    public void handle(CreateMappingsCommand command) {
        try {
            Mappings mapp = mappingInfoRepository.load(command.getId());
            if (mapp != null) {
                throw new DuplicateIdException(command.getId());
            }
        } catch (AggregateNotFoundException e) {
        }
        mappingInfoRepository.add(new Mappings(command.getId()));
    }
}

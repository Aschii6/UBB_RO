package Repo;

import Domain.Entity;
import Domain.Validators.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * @param <ID>
 * @param <E>
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    // de pus validator in service

    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id){
        if (id == null)
            throw new IllegalArgumentException("id must not be null.\n");
        E entity = entities.get(id);
        if (entity == null)
            throw new RepositoryException("Nonexistent id.\n");
        return entity;
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must not be null.\n");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            throw new RepositoryException("Duplicate id/entity.\n");
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("Id must not be null.\n");
        E entity = entities.get(id);
        if (entity == null)
            throw new RepositoryException("Nonexistent id.\n");

        entities.remove(id);
        return entity;
    }

    @Override
    public E update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must not be null.\n");
        validator.validate(entity);

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }

        throw new RepositoryException("Nonexistent id.\n");
    }
}

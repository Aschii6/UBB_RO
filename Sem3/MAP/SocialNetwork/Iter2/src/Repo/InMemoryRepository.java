package Repo;

import Domain.Entity;
import Domain.Validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @param <ID>
 * @param <E>
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    Map<ID,E> entities;

    public InMemoryRepository() {
        entities = new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id){
        if (id == null)
            throw new IllegalArgumentException("id must not be null.\n");

        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must not be null.\n");

        if(entities.get(entity.getId()) != null) {
            return Optional.of(entity);
        }
        else
            entities.put(entity.getId(), entity);

        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("Id must not be null.\n");

        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must not be null.\n");

        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }

        return Optional.of(entity);
    }
}

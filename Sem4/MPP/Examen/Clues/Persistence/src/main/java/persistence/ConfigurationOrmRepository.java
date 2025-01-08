package persistence;

import model.Configuration;

import java.util.Optional;

public class ConfigurationOrmRepository implements ConfigurationRepository{
    @Override
    public Optional<Configuration> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Configuration> findAll() {
        return null;
    }

    @Override
    public Optional<Configuration> save(Configuration configuration) {
        try {
            HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(configuration));

            System.out.println(configuration.getId());

            System.out.println("A MERS ?????????????????????????");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return Optional.empty();
    }

    @Override
    public Optional<Configuration> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Configuration> update(Configuration entity) {
        return Optional.empty();
    }
}

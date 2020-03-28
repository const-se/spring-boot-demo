package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public abstract class AbstractMigration extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        NamedParameterJdbcTemplate template = getTemplate(context);
        migrate(template);
    }

    abstract protected void migrate(NamedParameterJdbcTemplate template) throws Exception;

    private NamedParameterJdbcTemplate getTemplate(Context context) {
        return new NamedParameterJdbcTemplate(
                new SingleConnectionDataSource(context.getConnection(), true)
        );
    }
}

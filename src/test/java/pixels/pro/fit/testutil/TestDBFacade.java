package pixels.pro.fit.testutil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.support.TransactionTemplate;

@TestComponent
public class TestDBFacade {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private TestEntityManager entityManager;

    public void cleanDatabase(){
        transactionTemplate.executeWithoutResult(
                transactionStatus -> JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_principal")
        );
    }

    public <T> T findById(Class<T> entityClass, Long id){
        return entityManager.getEntityManager()
                .createQuery("SELECT * FROM " + entityClass.getSimpleName() + " WHERE id=" + id)
                .unwrap(entityClass);
    }
}

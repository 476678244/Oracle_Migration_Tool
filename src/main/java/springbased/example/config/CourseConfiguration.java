package springbased.example.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbased.config.MysqlEmbededConfig;
import springbased.example.dao.CourseDao;
import springbased.example.dao.CourseIncFeeDao;
import springbased.example.dao.impl.HibernateContextualSessionCourseDAO;
import springbased.example.dao.impl.HibernateCourseDao;
import springbased.example.dao.impl.HibernateCourseDaoSupport;
import springbased.example.dao.impl.HibernateDAOSupportCourseDAO;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class CourseConfiguration {

	@Autowired
	private DataSource dataSource;

	@Bean
	public CourseIncFeeDao courseDao() {
		HibernateCourseDao courseIncFeeDao = new HibernateCourseDao(sessionFactory(), hibernateCourseDaoSupport());
		return courseIncFeeDao;
	}

	@Bean
	public HibernateCourseDaoSupport hibernateCourseDaoSupport() {
		HibernateCourseDaoSupport hibernateCourseDaoSupport = new HibernateCourseDaoSupport(sessionFactory());
		return hibernateCourseDaoSupport;
	}

	@Bean
	@DependsOn("embeddedMysqlStarter")
	public LocalSessionFactoryBean sessionFactoryBean() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));
		sessionFactoryBean.setHibernateProperties(hibernateProperties());
		sessionFactoryBean.setPackagesToScan("springbased.example.bean");
		sessionFactoryBean.setDataSource(dataSource);
		return sessionFactoryBean;
	}

	@Bean
	public SessionFactory sessionFactory() {
		return sessionFactoryBean().getObject();
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", org.hibernate.dialect.MySQL57Dialect.class.getName());
		properties.put("hibernate.show_sql", true);
		properties.put("hibernate.hbm2dll.auto", "update");
		return properties;
	}

	@Bean
	public CourseDao courseDao2() {
		HibernateDAOSupportCourseDAO courseDao = new HibernateDAOSupportCourseDAO();
		courseDao.setSessionFactory(sessionFactory());
		return courseDao;
	}

	@Bean
	public HibernateTemplate hibernateTemplate() {
		return new HibernateTemplate(sessionFactory());
	}

	/**
	 * To enable declarative transaction management for the methods annotated with @Transactional,
	 * you have to add the @EnableTransactionManagement annotation to your configuration class.
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactory());
	}
}

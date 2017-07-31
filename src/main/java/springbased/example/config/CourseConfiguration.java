package springbased.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import springbased.example.dao.CourseDao;
import springbased.example.dao.impl.HibernateCourseDao;
import springbased.example.dao.impl.HibernateDAOSupportCourseDAO;

import java.util.Properties;

@Configuration
public class CourseConfiguration {
	@Bean
	public CourseDao courseDao() {
		return new HibernateCourseDao(sessionfactory().getObject());
	}

	@Bean
	public LocalSessionFactoryBean sessionfactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));
		sessionFactoryBean.setHibernateProperties(hibernateProperties());
		sessionFactoryBean.setPackagesToScan("springbased.example.bean");
		return sessionFactoryBean;
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
		courseDao.setSessionFactory(sessionfactory().getObject());
		return courseDao;
	}

	@Bean
	public HibernateTemplate hibernateTemplate() {
		return new HibernateTemplate(sessionfactory().getObject());
	}

	/**
	 * To enable declarative transaction management for the methods annotated with @Transactional,
	 * you have to add the @EnableTransactionManagement annotation to your configuration class.
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionfactory().getObject());
	}
}

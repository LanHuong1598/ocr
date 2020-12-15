package com.example.ocr;

import com.example.ocr.config.HibernateStatisticsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class OcrApplication {

	@Autowired
	protected DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(OcrApplication.class, args);
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setEntityInterceptor(statisticsInterceptor());
		sessionFactory.setPackagesToScan("com.example.ocr");
		return sessionFactory;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

	@Bean
	public HibernateStatisticsInterceptor statisticsInterceptor() {
		return new HibernateStatisticsInterceptor();
	}
}

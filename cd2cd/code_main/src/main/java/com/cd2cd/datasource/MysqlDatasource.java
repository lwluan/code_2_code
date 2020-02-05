package com.cd2cd.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = "com.yishang.loan_api.mapper", 
sqlSessionTemplateRef  = "sqlSessionTemplate")
public class MysqlDatasource {
	
	
	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.multisource")

    @Primary
    public DataSource dataSource() {
        return new MultiDataSource();
    }
	
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setTypeAliasesPackage("com.yishang.loan_api.domain");
        bean.setDataSource(dataSource);
        
        // 设置多数据库支持
        Properties mProperties = new Properties();
        mProperties.put("Oracle", "oracle");
        mProperties.put("MySQL", "mysql");
        mProperties.put("SQL Server", "sqlserver");
        mProperties.put("DB2", "db2");
        
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        databaseIdProvider.setProperties(mProperties);
        
        bean.setDatabaseIdProvider(databaseIdProvider);
        
        return bean.getObject();
    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}	

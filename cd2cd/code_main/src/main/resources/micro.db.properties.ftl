datasource.${dbPrev}.dataSourceType=org.apache.commons.dbcp2.BasicDataSource
datasource.${dbPrev}.driverClassName=com.mysql.cj.jdbc.Driver
datasource.${dbPrev}.url=jdbc:mysql://${hostname}:${port}/${dbName}?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
datasource.${dbPrev}.password=${password}
datasource.${dbPrev}.username=${username}
datasource.${dbPrev}.maxActive=50
datasource.${dbPrev}.maxIdle=10
datasource.${dbPrev}.minIdle=5
package net.todoApplication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "net.todoApplication.data.repositories")
public class MongoConfiguration extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "todoApplication";
    }
}
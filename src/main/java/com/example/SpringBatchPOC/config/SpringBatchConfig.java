package com.example.SpringBatchPOC.config;

import com.example.SpringBatchPOC.dto.UserDTO;
import com.example.SpringBatchPOC.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory ;


    @Bean
    public Job job(Step step) {
        return jobBuilderFactory.get("Carga-ETL")
                .incrementer(new RunIdIncrementer()) // Cada vez que ejecute un batch este valor se va a incrementar en 1
                .start(step)
                .build();
    }

    @Bean
    public Step step(FlatFileItemReader<UserDTO> reader,
                     ItemProcessor<UserDTO, User> processor,
                     ItemWriter<User> writer) {
       return stepBuilderFactory.get("Carga-ETL-archivo")
                .<UserDTO, User>chunk(2) // Cantidad de registros a procesar por batch
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<UserDTO> reader(@Value("${input}") Resource resource) {
        FlatFileItemReader<UserDTO> flatFileItemReader = new FlatFileItemReader();
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setName("Lector-CSV");
        flatFileItemReader.setLinesToSkip(1); // Skip el header del csv
        flatFileItemReader.setLineMapper(lineMapper()); // Le indicamos la logica de mapeo al dto
        return  flatFileItemReader;
    }

    @Bean
    public LineMapper<UserDTO> lineMapper() {
        DefaultLineMapper<UserDTO> defaultLineMapper = new DefaultLineMapper();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id", "name", "dept", "salary"); // Define las columnas del csv

        BeanWrapperFieldSetMapper<UserDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(UserDTO.class); // Mapea cada linea a este pojo

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

}

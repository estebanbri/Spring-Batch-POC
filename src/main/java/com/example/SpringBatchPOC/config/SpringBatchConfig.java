package com.example.SpringBatchPOC.config;

import com.example.SpringBatchPOC.batch.MyJobListener;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory ;

    @Autowired
    private MyJobListener myJobListener ;

    @Bean
    public Job job(Step step) {
        return jobBuilderFactory.get("Carga-ETL")
                .incrementer(new RunIdIncrementer()) // Cada vez que ejecute un batch este valor se va a incrementar en 1
                .listener(myJobListener)
                .start(step)
                .build();
    }

    @Bean
    public Step step(FlatFileItemReader<UserDTO> reader,
                     ItemProcessor<UserDTO, User> processor,
                     ItemWriter<User> writer) {
       return stepBuilderFactory.get("Carga-ETL-archivo")
                .<UserDTO, User>chunk(2) // Tamaño del batch, es decir va a leer y escribir de a 2 registros en este caso
                .reader(reader)
                .processor(processor)
                .writer(writer)
                //.taskExecutor(new SimpleAsyncTaskExecutor()) // El reader, processor y writer se van a ejecutar async, es decir sin esto tarda 9seg y con esto tarda 3seg
                .build();
    }

    @Bean
    public FlatFileItemReader<UserDTO> reader(@Value("${file.input}") Resource resource) {
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
        // Define los nombres de las columnas del csv, porque como es un csv cada valor crudo es un string sin contexto, con setNames le indicas que el primer valor es el id, el segundo name..
        // Que luego se tienen que corresponder con los nombres de las variables de tu clase que definas en setTargetType
        delimitedLineTokenizer.setNames("id", "name", "dept", "salary");

        BeanWrapperFieldSetMapper<UserDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(UserDTO.class); // Mapea cada linea a este pojo

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

}

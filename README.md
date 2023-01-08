# Spring Batch POC

# Descripcion
Todo va a comenzar con un ***Scheduler*** (puede ser un Spring scheduler o Quartz scheduler)
que va a hacer el trigger de nuestro batch. Nosotros para mantenerlo simple al ejemplo el trigger en vez 
que se haga automatico vamos a hacerlo manual con un endpoint comun y corriente (Ver endpoint debajo).
Dicho Scheduler va a triggear el ***JobLauncher***,  el JobLauncher es el punto de partida 
de cualquier Job. El JobLauncher behind the scenes el solito va a triggear:
1. El ***JobRepository***, el JobRepository va a contener informacion estadistica de cuantos batches se ejecutaron, 
el estado de cada batch, cuantos mensajes se procesaron, cuando mensajes fueron skipped or failed, etc.
2.  El ***Job*** (***JobExecution***), el Job, que previamente le registraste a dicho JobLauncher, puede tener uno o varios Steps.

Un ***Step*** (***StepExecution***) consiste de 3 partes :
1. Un ItemReader: read de un source (Ej: csv file)
2. Un ItemProcessor (Opcional): procesar la data (Mapping dto to entity and transform dept_id to dept_name)
3. Un ItemWriter: escribirla a otro source (Ej: Database)

![alt text](https://github.com/estebanbri/Spring-Batch-POC/blob/master/workflow.png)

### Tablas de JobRepository:
![alt text](https://github.com/estebanbri/Spring-Batch-POC/blob/master/result-job-repository.png)

### Tabla de users escritas por writer:
![alt text](https://github.com/estebanbri/Spring-Batch-POC/blob/master/result-job-output.png)

### Para hacer el trigger de la carga de los datos csv a la db, hacer un request a:
> http://localhost:8081/load - Trigger point for Spring Batch
> http://localhost:8081/h2-console - H2 Console for querying the in-memory tables.

### Nota: 
Al agregar "spring-boot-devtools" spring nos autoconfigurará y proveerá un endpoint "/h2-console"
para acceder al a base de datos en memoria h2

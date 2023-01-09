# Spring Batch POC

# Descripcion
Spring Batch te sirve cuando tenes que procesar MUCHA data, porque recorda que si no lo usas y haces
todo en Java/Spring convencional vas a llenar la memoria RAM y cuando recien se termine de procesar el ultimo registro
recien ahi va a hacer el commit a la db de salida, esto no es performante porque te vas a quedar sin RAM y cuanto más ram
este en uso mas lento va a ir procesando la data. Por eso nace Spring Batch para ir procesando en chunks o bloques e ir commiteando
esos chunks es decir divide el universo de datos en fragmentos y va trabajando con cada uno de esos fragmentos y cuando finaliza
de procesar un chunk, commitea y escribe en db.

# Tecnicamente
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

### Tabla GURU_USER escritas por writer:
![alt text](https://github.com/estebanbri/Spring-Batch-POC/blob/master/result-job-output.png)

### Para hacer el trigger de la carga de los datos csv a la db, hacer un request a:
> - http://localhost:8081/load - Trigger point for Spring Batch
> - http://localhost:8081/h2-console - H2 Console for querying the in-memory tables.

### Nota: 
- Al agregar "spring-boot-devtools" spring nos autoconfigurará y proveerá un endpoint "/h2-console"
para acceder al a base de datos en memoria h2
- Si queremos que nuestro Job se ejecute de manera asincrona, caso real que aplica cuando desde tu job llamas a un 
endpoint con RestTemplate cada llamada es bloqueante por ende no queres bloquear la ejecucion del Job con estas llamadas.
Esto se hace configurando el JobLauncher, setTaskExecutor(new SimpleTaskExecutor()) o admite un executor customizado tmb.
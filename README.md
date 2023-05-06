# Big Data Project, BATCH Section
In this school project, We needed to make a project that follows a lambda architecture, meaning using both Stream and Batch types of operations to generate an output that is a visualization saved in a Database.
For this project we used Hadoop for the batch processing, and Spark coupled with Kafka for the stream processing, finally saving visualizations to a MongoDB database.
This visualization is then read and offered to clients using a web interface built using NextJS.

## What is this repo?
This repo contains a Map Reduce job that has a csv file as input. This csv file contains the number of covid cases per country per day, and so this map reduce serves as a way to calculate the monthly number of covid cases per day, so to then save this information in a MongoDB database. 



## To run this repository:
  1. Pull the image containing dockerfiles :
  >`docker pull liliasfaxi/spark-hadoop:hv-2.7.2`
  2. Create a network bridge :
  > `docker network create --driver=bridge hadoop`
  3. Run containers:
  ```
  docker run -itd --net=hadoop -p 50070:50070 -p 8088:8088 -p 7077:7077 -p 16010:16010 \
            --name hadoop-master --hostname hadoop-master \
            liliasfaxi/spark-hadoop:hv-2.7.2

  docker run -itd -p 8040:8042 --net=hadoop \
        --name hadoop-slave1 --hostname hadoop-slave1 \
              liliasfaxi/spark-hadoop:hv-2.7.2

  docker run -itd -p 8041:8042 --net=hadoop \
        --name hadoop-slave2 --hostname hadoop-slave2 \
              liliasfaxi/spark-hadoop:hv-2.7.2 
 ```
     
 4. Enter the master container and run this script:
  > `docker exec -it hadoop-master bash`
  
  > `./start-hadoop.sh`
  
  Now that hadoop is started, we need to run the Hadoop Map Reduce Job, **Make sure that mongodb is running and is in the hadoop bridge network**
  
 5. Compile this repository using Maven:
  > `mvn clean compile assembly:single`
  
 You should have obtained a .jar file in the target directory of your local project.
 
 6. Move that jar to the master container:
 > ` docker cp path/to/jar/file.jar hadoop-master:/root/file.jar`
 
 Now don't forget to make an input directory in HDFS using `hadoop fs -mkdir input` and then `hadoop fs -put input_file.csv`. In this case the input file being world_cases that's in the ressources directory.
 
 7. Now all that's left is to run the Job:
 >`hadoop jar file.jar projet.batch.CasesMonthlyPerCountryJob input output`
 
 And like that if everything was done properly, if you check your mongodb database that's in the container called mongodb, you should find a collection called countries with alot of documents.
 
 Thanks for reading.
 ## Made by:
  - KHEMIRI Mohamed Hedi 
  - DALI HASSEN Naim
  - LAMINE Hatem

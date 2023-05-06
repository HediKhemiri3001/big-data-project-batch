package projet.batch;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.Document;

import java.io.IOException;
import java.util.Collections;


public class CasesMonthlyPerCountryReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable result = new IntWritable();
    private MongoClient mongoClient;
    private MongoDatabase outputDB;
    private MongoCollection outputColl;
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        // Initialize the MongoDB client and collection
        ServerAddress serverAddress = new ServerAddress("mongodb", 27017);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(serverAddress)))
                .build();
        this.mongoClient = MongoClients.create(settings);
        this.outputDB = mongoClient.getDatabase("projet-bigdata");
        this.outputColl = outputDB.getCollection("countries");
    }
    //inputs {"country:month_index:year" : {cases, cases, cases, ...}}
    //outputs {"country:month_index:year": total_cases}
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int monthly = 0;
        String[] keys = key.toString().split(":");
        for (IntWritable val : values) {

            monthly += val.get();
        }
        System.out.println("--> Sum of:"+key+" = "+monthly);
        Document document = new Document("country", keys[0]).append("month",keys[1]).append("year",keys[2]).append("cases", monthly);
        this.outputColl.insertOne(document);
        result.set(monthly);
        context.write(key, result);
    }
}
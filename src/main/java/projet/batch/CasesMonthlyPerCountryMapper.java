package projet.batch;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.io.IOException;

public class CasesMonthlyPerCountryMapper extends Mapper<Object, Text, Text, IntWritable> {
    private static final Logger logger = LoggerFactory.getLogger(CasesMonthlyPerCountryMapper.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Text word = new Text();
    private IntWritable OutCases = new IntWritable();
    //Outputs {"country:month_index" : number of cases in that month}
    public void map(Object key, Text value, Mapper.Context context) throws IOException, InterruptedException {
        logger.debug("Received key: {} and value: {}", key.toString(), value.toString());
        String line = value.toString();
        String[] fields = line.split(",");

        // Extract the required fields from the CSV row
        String dateString = fields[0].trim();
        String country = fields[6].trim();
        String casesString = fields[4].trim();

        try {
            // Convert the date string to a Date object
            Date date = dateFormat.parse(dateString);

            // Extract the month index and year from the date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int monthIndex = calendar.get(Calendar.MONTH) +1;
            int year = calendar.get(Calendar.YEAR);

            // Set the output key as country:month_index:year
            word.set(country + ":" + monthIndex + ":" + year);

            // Set the output value as the number of cases
            int cases = Integer.parseInt(casesString);
            OutCases.set(cases);

            // Emit the key-value pair
            context.write(word, OutCases);

        } catch (ParseException e) {
            // Ignore rows with invalid date format
            e.printStackTrace();
        }
    }
}
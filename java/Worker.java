import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.*;

import static org.apache.spark.api.java.JavaSparkContext.toSparkContext;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.window;

public final class Worker {

    private static MapFunction<String, LogAccessData> String2LogDataMapperRoman = data -> {
        return LogAccessData.parseString(data);
    };

    public static void start(String inputFile, String outputFolder){
        final SparkConf conf = new SparkConf().setAppName("SparkTest").setMaster("local");
        final JavaSparkContext context = new JavaSparkContext(conf);

        SparkSession spark = SparkSession
                .builder()
                .sparkContext(toSparkContext(context))
                .getOrCreate();

        Dataset<LogAccessData> ds = spark
        .read().textFile(inputFile)
        .map(String2LogDataMapperRoman, Encoders.bean(LogAccessData.class));

        ds
            .filter(x -> x.getResponseCodeInt() >= 500)
            .filter(x -> x.getResponseCodeInt() < 600)
            .groupBy("Method", "Request")
            .agg(count("Method").as("countNumber"))
            .coalesce(1)
            .write()
            .csv(outputFolder + "/task1");

        ds
            .groupBy("DateTimeString", "Method", "ResponseCode").agg(count("Method").as("countNumber"))
            .filter("countNumber >= 10")
            .orderBy("DateTimeString")
            .coalesce(1)
            .write()
            .csv(outputFolder + "/task2");

        ds
            .filter(x -> x.getResponseCodeInt() >= 400)
            .filter(x -> x.getResponseCodeInt() < 600)
            .groupBy(window(ds.col("DateTimeString"), "1 week", "1 day" ))
            .agg(count("Request").as("Average4Week"))
            .select("window.start", "window.end", "Average4Week")
            .sort("start")
            .coalesce(1)
            .write()
            .csv(outputFolder + "/task3");
    }

}


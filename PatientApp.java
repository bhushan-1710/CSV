import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class PatientApp extends Application {

    static class Patient {
        String name;
        int age, heartRate, bp;

        Patient(String name, int age, int heartRate, int bp) {
            this.name = name;
            this.age = age;
            this.heartRate = heartRate;
            this.bp = bp;
        }
    }

    // Read CSV
    public static List<Patient> readCSV(String file) {
        List<Patient> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                list.add(new Patient(
                        d[0],
                        Integer.parseInt(d[1]),
                        Integer.parseInt(d[2]),
                        Integer.parseInt(d[3])
                ));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e);
        }
        return list;
    }

    // Calculate stats
    public static void calculateStats(List<Patient> list) {
        DescriptiveStatistics age = new DescriptiveStatistics();
        DescriptiveStatistics hr = new DescriptiveStatistics();
        DescriptiveStatistics bp = new DescriptiveStatistics();

        for (Patient p : list) {
            age.addValue(p.age);
            hr.addValue(p.heartRate);
            bp.addValue(p.bp);
        }

        System.out.println("Average Age: " + age.getMean());
        System.out.println("Median Heart Rate: " + hr.getPercentile(50));
        System.out.println("Std Dev Blood Pressure: " + bp.getStandardDeviation());
    }

    @Override
    public void start(Stage stage) {

        List<Patient> list = readCSV("patients.csv");

        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();

        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setTitle("Heart Rate Chart");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Heart Rate");

        for (Patient p : list) {
            series.getData().add(new XYChart.Data<>(p.name, p.heartRate));
        }

        chart.getData().add(series);

        stage.setScene(new Scene(chart, 600, 400));
        stage.setTitle("Patient Data");
        stage.show();
    }

    public static void main(String[] args) {
        List<Patient> list = readCSV("patients.csv");
        calculateStats(list);
        launch(args);
    }
}
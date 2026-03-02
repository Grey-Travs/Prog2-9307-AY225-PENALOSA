/**
 * Data model representing a single video game sales record.
 * * @author Travis Peñalosa
 * Project: Programming 2 - Machine Problem
 */
public class VideoGameRecord {
    private String title;
    private String console;
    private String publisher;
    private double totalSales;

    public VideoGameRecord(String title, String console, String publisher, double totalSales) {
        this.title = title;
        this.console = console;
        this.publisher = publisher;
        this.totalSales = totalSales;
    }

    // Getters for analytics processing
    public double getTotalSales() { return totalSales; }
    public String getTitle() { return title; }
    public String getConsole() { return console; }
    public String getPublisher() { return publisher; }
}
package graduation.plantcare.ui.home;

public class HistoryItem {
    private String documentId; // سيتم تعبئته من Firestore
    private String imageUrl;
    private String diseaseName;
    private String description;
    private String steps;
    private String supplementName;
    private String supplementImage;
    private long timestamp;

    public HistoryItem() {}

    public HistoryItem(String imageUrl, String diseaseName, String description, String steps,
                      String supplementName, String supplementImage, long timestamp) {
        this.imageUrl = imageUrl;
        this.diseaseName = diseaseName;
        this.description = description;
        this.steps = steps;
        this.supplementName = supplementName;
        this.supplementImage = supplementImage;
        this.timestamp = timestamp;
    }

    public String getImageUrl() { return imageUrl; }
    public String getDiseaseName() { return diseaseName; }
    public String getDescription() { return description; }
    public String getSteps() { return steps; }
    public String getSupplementName() { return supplementName; }
    public String getSupplementImage() { return supplementImage; }
    public long getTimestamp() { return timestamp; }
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
}

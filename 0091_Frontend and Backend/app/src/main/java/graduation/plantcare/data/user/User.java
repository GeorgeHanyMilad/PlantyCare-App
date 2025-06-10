package graduation.plantcare.data.user;
import java.util.UUID;

public class User {
    private String UID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean verified;
    private long timestamp;
    private int modelOneScore;
    private int modelTwoScore;
    private int modelThreeScore;

    public int getModelOneScore() {
        return modelOneScore;
    }

    public void setModelOneScore(int modelOneScore) {
        this.modelOneScore = modelOneScore;
    }

    public int getModelTwoScore() {
        return modelTwoScore;
    }

    public void setModelTwoScore(int modelTwoScore) {
        this.modelTwoScore = modelTwoScore;
    }

    public int getModelThreeScore() {
        return modelThreeScore;
    }

    public void setModelThreeScore(int modelThreeScore) {
        this.modelThreeScore = modelThreeScore;
    }

    public void setProfileImageUrl(String profileImageUrl) {
    }

    public String getUID() {
        return UID;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public User(){

    }

    public User(String firstName, String lastName, String email, String password, boolean isVerified, long timestamp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.verified = isVerified;
        this.timestamp = timestamp;
        this.UID = UUID.randomUUID().toString();
    }

    public User(String UID, String firstName, String lastName, String email, String password, boolean isVerified, long timestamp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.verified = isVerified;
        this.timestamp = timestamp;
        this.UID = UID;
    }
    public User(String UID, String firstName, String lastName, String email, String password) {
        this.UID = UID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "User{" +
                "UID='" + UID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isVerified=" + verified +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
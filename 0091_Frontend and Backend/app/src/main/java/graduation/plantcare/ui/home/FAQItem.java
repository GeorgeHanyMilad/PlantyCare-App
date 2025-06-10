package graduation.plantcare.ui.home;

public class FAQItem {
    private final String question;
    private final String answer;
    private boolean isExpanded;

    public FAQItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.isExpanded = false;
    }

    public FAQItem(String question, String answer, boolean isExpanded) {
        this.question = question;
        this.answer = answer;
        this.isExpanded = isExpanded;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}

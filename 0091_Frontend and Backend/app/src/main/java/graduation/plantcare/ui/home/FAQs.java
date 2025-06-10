package graduation.plantcare.ui.home;

import android.os.Bundle;
import android.view.View;

import graduation.plantcare.adapters.FAQAdapter;
import graduation.plantcare.base.BaseActivity;
import graduation.plantcare.R;
import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FAQs extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.faqs);

        RecyclerView faqRecyclerView = findViewById(R.id.faqRecyclerView);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<FAQItem> faqList = new ArrayList<>();
        faqList.add(new FAQItem("How can I detect a plant disease?", "Upload an image of the plant leaf, and our system will provide a description of the disease, its treatment, and the required steps."));
        faqList.add(new FAQItem("How accurate is the plant disease detection model?", "Our model provides accurate results based on the analysis of the uploaded leaf image, helping you identify common plant diseases."));
        faqList.add(new FAQItem("What should I do after detecting a plant disease?", "Follow the suggested treatment steps provided by the model and apply the recommended cure to your plant."));
        faqList.add(new FAQItem("How do I know which plant I have?", "Enter 7 specific inputs (such as leaf type, color, etc.), and our system will predict the plant name for you."));
        faqList.add(new FAQItem("How can I find the right fertilizer for my plant?", "Provide 8 inputs related to your plant's condition and soil type, and our system will suggest the best fertilizer for optimal growth."));
        faqList.add(new FAQItem("Can I upload multiple images for plant disease detection?", "Currently, our system supports uploading one image at a time for plant disease analysis."));
        faqList.add(new FAQItem("What kind of inputs do I need for plant disease detection?", "You need to upload a clear image of the plant leaf showing visible symptoms of the disease."));
        faqList.add(new FAQItem("How do I upload an image of my plant?", "Use the upload button in the app to take a photo or select an image from your device to upload it for analysis."));
        faqList.add(new FAQItem("Can I get a detailed report of the disease and treatment?", "Yes, after analysis, the system provides a detailed report including the disease description, treatment steps, and a recommended image for the treatment."));
        faqList.add(new FAQItem("What if the plant disease is not detected correctly?", "If the result seems incorrect, please ensure the image is clear and properly captures the affected part of the plant leaf."));
        faqList.add(new FAQItem("How can I improve the prediction accuracy for plant disease detection?", "Make sure the leaf image is in focus, well-lit, and clearly shows the symptoms of the disease."));
        faqList.add(new FAQItem("What kind of plants can the plant disease detection model recognize?", "The model can recognize a wide variety of plants, but its accuracy depends on the quality and clarity of the image provided."));
        faqList.add(new FAQItem("What should I do if the plant prediction is wrong?", "If the prediction is incorrect, try adjusting the inputs (such as leaf shape or size) and try again. The model improves with more accurate data."));
        faqList.add(new FAQItem("How can I make sure I am using the correct fertilizer?", "Provide accurate details about the plant's condition, soil type, and environment. Our system will recommend the best fertilizer based on these inputs."));
        faqList.add(new FAQItem("What types of fertilizer does your system recommend?", "The system can suggest different types of fertilizers, such as organic, chemical, and liquid fertilizers, depending on your plant’s needs and soil condition."));
        faqList.add(new FAQItem("Is the fertilizer suggestion based on any scientific data?", "Yes, the fertilizer recommendations are based on research and common agricultural practices, ensuring the plant gets the optimal nutrients for growth."));
        faqList.add(new FAQItem("How do I know if the fertilizer suggested is suitable for my plant?", "The fertilizer is selected based on your plant’s requirements and soil health, ensuring it matches the plant’s needs."));
        faqList.add(new FAQItem("Can I use multiple fertilizers for my plant?", "It’s recommended to follow the fertilizer recommendation provided. Using multiple fertilizers might cause over-fertilization, which can harm the plant."));
        faqList.add(new FAQItem("Can I change the fertilizer type later?", "Yes, you can modify your inputs to adjust the fertilizer suggestion as per your plant’s changing needs."));
        faqList.add(new FAQItem("Does the system support all types of plants?", "Our system supports a wide range of plants, but it’s most accurate for common types that are frequently studied."));
        faqList.add(new FAQItem("What if the fertilizer doesn’t seem to work on my plant?", "Ensure that the inputs you provided are accurate, and consider adjusting the amount or frequency of fertilizer based on the plant's response."));
        faqList.add(new FAQItem("How can I track the results after using the suggested fertilizer?", "Keep a record of your plant’s growth and condition after applying the recommended fertilizer, and you can re-check the plant’s status periodically."));
        faqList.add(new FAQItem("Can I upload multiple plant images for different plants?", "Currently, the app supports uploading one image at a time for disease detection, but we plan to extend support for multiple uploads in future updates."));
        faqList.add(new FAQItem("How often should I upload images for disease monitoring?", "We recommend uploading images whenever you notice any abnormal symptoms in your plants, or on a weekly basis to monitor plant health."));
        faqList.add(new FAQItem("Is there a way to get continuous updates about my plants' health?", "We are working on adding features for regular health tracking and notifications. Stay tuned for upcoming updates."));

        FAQAdapter adapter = new FAQAdapter(this,faqList);
        faqRecyclerView.setAdapter(adapter);
    }

    public void backButton(View view) {
        finish();
    }
}
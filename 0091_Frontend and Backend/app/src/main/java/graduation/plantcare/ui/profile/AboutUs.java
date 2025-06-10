package graduation.plantcare.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;

import graduation.plantcare.R;
import graduation.plantcare.base.BaseActivity;

public class AboutUs extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.about_us);
        animateElements();
    }

    private void animateElements() {
        ImageView aboutUsPhoto = findViewById(R.id.aboutUsPhoto);

        LinearLayout whoAreWeTitle = findViewById(R.id.aboutUsWhoAreWeTitle);
        LinearLayout visionAndMissionTitle = findViewById(R.id.aboutUsVisionAndMissionTitle);
        TextView visionAndMissionDetails = findViewById(R.id.aboutUsVisionAndMissionDetails);
        TextView whoAreWeDetails = findViewById(R.id.aboutUsWhoAreWeDetails);
        LinearLayout whoAreWeDetails2 = findViewById(R.id.aboutUsWhoAreWeTitle2);
        CardView aboutUsCardOne = findViewById(R.id.aboutUsCardOne);
        CardView aboutUsCardTwo = findViewById(R.id.aboutUsCardTwo);
        CardView aboutUsCardThree = findViewById(R.id.aboutUsCardThree);
        CardView aboutUsCardFour = findViewById(R.id.aboutUsCardFour);
        CardView aboutUsCardFive = findViewById(R.id.aboutUsCardFive);
        CardView aboutUsCardSix = findViewById(R.id.aboutUsCardSix);
        CardView aboutUsCardSeven = findViewById(R.id.aboutUsCardSeven);


        aboutUsPhoto.postDelayed(() -> {
            aboutUsPhoto.setVisibility(View.VISIBLE);
            aboutUsPhoto.startAnimation(createFadeInAnimation(1500));
        }, 500);

        whoAreWeTitle.postDelayed(() -> {
            whoAreWeTitle.setVisibility(View.VISIBLE);
            whoAreWeTitle.startAnimation(createSlideInLeftAnimation(1500));
        }, 1500);

        whoAreWeDetails.postDelayed(() -> {
            whoAreWeDetails.setVisibility(View.VISIBLE);
            whoAreWeDetails.startAnimation(createFadeInAnimation(1500));
        }, 2000);

        visionAndMissionTitle.postDelayed(() -> {
            visionAndMissionTitle.setVisibility(View.VISIBLE);
            visionAndMissionTitle.startAnimation(createSlideInLeftAnimation(1500));
        }, 2500);


        visionAndMissionDetails.postDelayed(() -> {
            visionAndMissionDetails.setVisibility(View.VISIBLE);
            visionAndMissionDetails.startAnimation(createFadeInAnimation(1500));
        }, 3500);


        whoAreWeDetails2.postDelayed(() -> {
            whoAreWeDetails2.setVisibility(View.VISIBLE);
            whoAreWeDetails2.startAnimation(createSlideInLeftAnimation(1500));
        }, 4500);

        aboutUsCardOne.postDelayed(() -> {
            aboutUsCardOne.setVisibility(View.VISIBLE);
            aboutUsCardOne.startAnimation(createFadeInAnimation(1500));
        }, 5500);

        aboutUsCardTwo.postDelayed(() -> {
            aboutUsCardTwo.setVisibility(View.VISIBLE);
            aboutUsCardTwo.startAnimation(createFadeInAnimation(1500));
        }, 6500);

        aboutUsCardThree.postDelayed(() -> {
            aboutUsCardThree.setVisibility(View.VISIBLE);
            aboutUsCardThree.startAnimation(createFadeInAnimation(1500));
        }, 8500);

        aboutUsCardFour.postDelayed(() -> {
            aboutUsCardFour.setVisibility(View.VISIBLE);
            aboutUsCardFour.startAnimation(createFadeInAnimation(1500));
        }, 9500);

        aboutUsCardFive.postDelayed(() -> {
            aboutUsCardFive.setVisibility(View.VISIBLE);
            aboutUsCardFive.startAnimation(createFadeInAnimation(1500));
        }, 10500);

        aboutUsCardSix.postDelayed(() -> {
            aboutUsCardSix.setVisibility(View.VISIBLE);
            aboutUsCardSix.startAnimation(createFadeInAnimation(1500));
        }, 11500);

        aboutUsCardSeven.postDelayed(() -> {
            aboutUsCardSeven.setVisibility(View.VISIBLE);
            aboutUsCardSeven.startAnimation(createFadeInAnimation(1500));
        }, 12500);

    }

    private Animation createFadeInAnimation(int duration) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        anim.setDuration(duration);
        return anim;
    }

    private Animation createSlideInLeftAnimation(int duration) {
        Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        anim.setDuration(duration);
        return anim;
    }


    public void backButton(View view) {
        finish();
    }

    public void bishoFacebook(View view) {
        openLink("https://www.facebook.com/bisho3zzat/");
    }

    public void bishoLinkedIn(View view) {
        openLink("https://www.linkedin.com/in/bisho3zzat/");
    }

    public void bishoInstagram(View view) {
        openLink("https://www.instagram.com/bisho3zzat/");
    }

    public void georgeFacebook(View view) {
        openLink("https://www.facebook.com/georgehanymilad/");}

    public void georgeLinkedIn(View view) {
        openLink("https://www.linkedin.com/in/georgehanymilad/");
    }

    public void georgeInstagram(View view) {
        openLink("https://www.instagram.com/gorge_hany.m/");
    }

    public void mariamLikedin(View view) {
        openLink("https://www.linkedin.com/in/mariem-elsaqa-8a8859246/");
    }

    public void eslamFacebook(View view) {
        openLink("https://www.facebook.com/share/16P1Janub9/");

    }

    public void eslamLinkedIn(View view) {
        openLink("https://www.linkedin.com/in/eslam-ayman-6b6b7726a");
    }

    public void eslamInstagram(View view) {
        openLink("https://www.instagram.com/eslam_ayman002?igsh=aWhtZmZwMXQybjRm");
    }

    public void openLink(String uri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }

}
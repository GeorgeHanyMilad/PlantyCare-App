package graduation.plantcare.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import graduation.plantcare.R;
import graduation.plantcare.ui.home.FAQItem;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {
    private final List<FAQItem> faqList;
    private final Context context;
    private int lastPositionAnimated = -1;

    public FAQAdapter(Context context, List<FAQItem> faqList) {
        this.context = context;
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_item, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FAQItem item = faqList.get(position);
        holder.questionTextView.setText(item.getQuestion());
        holder.answerTextView.setText(item.getAnswer());

        if (item.isExpanded()) {
            holder.answerTextView.setVisibility(View.VISIBLE);
            holder.questionTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.arrow_circle_down, 0);
        } else {
            holder.answerTextView.setVisibility(View.GONE);
            holder.questionTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.arrow_circle_left, 0);
        }

        holder.questionTextView.setOnClickListener(v -> {
            item.setExpanded(!item.isExpanded());
            notifyItemChanged(position);
        });

        if (position > lastPositionAnimated) {
            Animation anim = (position % 2 == 0)
                    ? createSlideInLeftAnimation(1000)
                    : createSlideOutRightAnimation(1000);
            anim.setStartOffset(position * 300L);
            holder.itemView.startAnimation(anim);
            lastPositionAnimated = position;
        } else {
            holder.itemView.clearAnimation();
        }
    }

    private Animation createSlideInLeftAnimation(int duration) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        anim.setDuration(duration);
        return anim;
    }

    private Animation createSlideOutRightAnimation(int duration) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        anim.setDuration(duration);
        return anim;
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, answerTextView;
        FAQViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
        }
    }
}

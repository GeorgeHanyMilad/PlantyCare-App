package graduation.plantcare.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import graduation.plantcare.features.model_one.ModelOne;
import graduation.plantcare.R;
import graduation.plantcare.fragments.HomeFragment;
import graduation.plantcare.ui.home.HomePage;
import graduation.plantcare.ui.home.HistoryItem;

public class ScanResultAdapter extends RecyclerView.Adapter<ScanResultAdapter.ViewHolder> {
    private List<HistoryItem> scanResults;
    private final Context context;
    private final OnScanResultChangedListener listener;

    public ScanResultAdapter(Context context, List<HistoryItem> scanResults, OnScanResultChangedListener listener) {
        this.context = context;
        this.scanResults = scanResults;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageViewPlant;
        TextView textViewDiseaseName, textViewDate;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewPlant = itemView.findViewById(R.id.plant_image);
            textViewDiseaseName = itemView.findViewById(R.id.plant_name);
            textViewDate = itemView.findViewById(R.id.plant_date);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryItem result = scanResults.get(position);

        holder.textViewDiseaseName.setText(result.getDiseaseName());

        String formattedDate = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(new Date(result.getTimestamp()));
        holder.textViewDate.setText(formattedDate);

        Glide.with(context).load(result.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading).into(holder.imageViewPlant);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModelOne.class);
            intent.putExtra("imageUrl", result.getImageUrl());
            intent.putExtra("diseaseName", result.getDiseaseName());
            intent.putExtra("description", result.getDescription());
            intent.putExtra("steps", result.getSteps());
            intent.putExtra("supplementName", result.getSupplementName());
            intent.putExtra("supplementImage", result.getSupplementImage());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            showDeleteDialog(position);
            return true;
        });
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setTitle("Delete Item")
                .setIcon(R.drawable.warning)
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("OK", (dialog, which) -> deleteItem(position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteItem(int position) {
        HistoryItem item = scanResults.get(position);
        deleteImageFromStorage(item.getImageUrl(), () -> {
            deleteDocumentFromFirestore(item.getDocumentId(), position);
        });
    }

    private void deleteImageFromStorage(String imageUrl, Runnable onSuccess) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference photoRef = storage.getReferenceFromUrl(imageUrl);

        photoRef.delete().addOnSuccessListener(aVoid -> {
            onSuccess.run();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to delete image", Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteDocumentFromFirestore(String documentId, int position) {
        FirebaseFirestore.getInstance()
                .collection("first_model_scans")
                .document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    scanResults.remove(position);
                    notifyDataSetChanged();
                    if (scanResults.isEmpty() && listener != null) {
                        listener.onScanResultsEmpty();
                    }
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete data", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return scanResults.size();
    }

    public void updateData(List<HistoryItem> newResults) {
        this.scanResults = newResults;
        notifyDataSetChanged();
    }
}

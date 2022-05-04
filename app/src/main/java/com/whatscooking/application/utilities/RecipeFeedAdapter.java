package com.whatscooking.application.utilities;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.whatscooking.application.R;

import java.util.ArrayList;

public class RecipeFeedAdapter implements ListAdapter {

    private final ArrayList<Recipe> recipeArrayList;
    private final Context context;

    public RecipeFeedAdapter(Context context, ArrayList<Recipe> recipeArrayList) {
        this.recipeArrayList = recipeArrayList;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
    }

    @Override
    public int getCount() {
        return recipeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return recipeArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return recipeArrayList.get(i).getUniqueId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Recipe recipe = recipeArrayList.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            convertView = layoutInflater.inflate(R.layout.recipe_feed_row, viewGroup, false);

            TextView recipeTitle = convertView.findViewById(R.id.recipeTitle);
            TextView recipeIngredients = convertView.findViewById(R.id.recipeIngredients);
            TextView recipeCategory = convertView.findViewById(R.id.recipeCategory);
            ImageView imageView = convertView.findViewById(R.id.list_image);

            byte[] imageBytes = Base64.decode(recipe.getImageData(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(decodedImage);

            recipeTitle.setText(recipe.getRecipeName());
            recipeIngredients.setText(recipe.getIngredients());
            recipeCategory.setText(recipe.getCategory());
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return recipeArrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}

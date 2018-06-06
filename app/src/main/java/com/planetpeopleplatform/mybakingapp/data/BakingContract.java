package com.planetpeopleplatform.mybakingapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class BakingContract {
    public static final String CONTENT_AUTHORITY = "com.planetpeopleplatform.mybakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String RECIPE_PATH = "recipes";
    public static final String INGREDIENT_PATH = "ingredients";
    public static final String STEP_PATH = "steps";


    public static final class RecipesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(RECIPE_PATH)
                .build();

        // for building URIs on insertion
        public static Uri buildRecipeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildBakingUriWithRecipeId(String recipe_id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(recipe_id)
                    .build();
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + RECIPE_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + RECIPE_PATH;

        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_RECIPE_ID = "id";
        public static final String COLUMN_RECIPE_NAME = "name";
        public static final String COLUMN_RECIPE_SERVING = "servings";
        public static final String COLUMN_RECIPE_IMAGE = "image";
        public static final String _ID = "_id";

    }

    public static final class IngredientsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(INGREDIENT_PATH)
                .build();

        public static Uri buildIngredientUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildBakingUriWithIngredientId(String Ingredient_id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Ingredient_id)
                    .build();
        }

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + INGREDIENT_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + INGREDIENT_PATH;

        public static final String TABLE_NAME = "ingredients";

        public static final String COLUMN_INGREDIENT_ID = "id";
        public static final String COLUMN_INGREDIENT_QUANTITY = "quantity";
        public static final String COLUMN_INGREDIENT_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT_INGREDIENT = "ingredient";
        public static final String _ID = "_id";
    }

    public static final class StepsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(STEP_PATH)
                .build();

        public static Uri buildStepUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildBakingUriWithStepId(String Step_id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Step_id)
                    .build();
        }

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + STEP_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + STEP_PATH;

        public static final String TABLE_NAME = "steps";

        public static final String COLUMN_STEP_ID = "id";
        public static final String COLUMN_STEP_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_STEP_DESCRIPTION = "description";
        public static final String COLUMN_STEP_VIDEO_URL = "videoURL";
        public static final String COLUMN_STEP_THUMBNAIL_URL = "thumbnailURL";
        public static final String _ID = "_id";
    }

}

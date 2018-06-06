package com.planetpeopleplatform.mybakingapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class BakingProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BakingDBHelper mOpenHelper;

    // Codes for the UriMatcher
    private static final int RECIPE = 100;
    private static final int RECIPE_ID = 101;

    private static final int INGREDIENT = 200;
    private static final int INGREDIENT_ID =201;

    private static final int STEP = 300;
    private static final int STEP_ID =301;


    private static final String FAILED_TO_INSERT_ROW_INTO = "Failed to insert row into ";
    private static final String CANNOT_HAVE_NULL = "Cannot have null content values";

    static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BakingContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, BakingContract.RECIPE_PATH, RECIPE);
        uriMatcher.addURI(authority, BakingContract.RECIPE_PATH + "/#", RECIPE_ID);

        uriMatcher.addURI(authority, BakingContract.INGREDIENT_PATH, INGREDIENT);
        uriMatcher.addURI(authority, BakingContract.INGREDIENT_PATH + "/#", INGREDIENT_ID);

        uriMatcher.addURI(authority, BakingContract.STEP_PATH, STEP);
        uriMatcher.addURI(authority, BakingContract.STEP_PATH + "/#", STEP_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new BakingDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mOpenHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case RECIPE:
                cursor = database.query(BakingContract.RecipesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case RECIPE_ID:
                String recipe_id = uri.getLastPathSegment();
                selection = BakingContract.RecipesEntry.COLUMN_RECIPE_ID + "=?";
                selectionArgs = new String[]{recipe_id};
                cursor = database.query(BakingContract.RecipesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case INGREDIENT:
                cursor = database.query(BakingContract.IngredientsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case INGREDIENT_ID:
                String ingredient_id = uri.getLastPathSegment();
                selection = BakingContract.IngredientsEntry.COLUMN_INGREDIENT_ID + "=?";
                selectionArgs = new String[]{ingredient_id};
                cursor = database.query(BakingContract.IngredientsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case STEP:
                cursor = database.query(BakingContract.StepsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case STEP_ID:
                String step_id = uri.getLastPathSegment();
                selection = BakingContract.StepsEntry.COLUMN_STEP_ID + "=?";
                selectionArgs = new String[]{step_id};
                cursor = database.query(BakingContract.StepsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }


    @Override
    public String getType( Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECIPE:
                return BakingContract.RecipesEntry.CONTENT_DIR_TYPE;
            case RECIPE_ID:
                return BakingContract.RecipesEntry.CONTENT_ITEM_TYPE;

            case INGREDIENT:
                return BakingContract.IngredientsEntry.CONTENT_DIR_TYPE;
            case INGREDIENT_ID:
                return BakingContract.IngredientsEntry.CONTENT_ITEM_TYPE;

            case STEP:
                return BakingContract.StepsEntry.CONTENT_DIR_TYPE;
            case STEP_ID:
                return BakingContract.StepsEntry.CONTENT_ITEM_TYPE;
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }



    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match) {
            case RECIPE:
                id = db.insertWithOnConflict(BakingContract.RecipesEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnUri = BakingContract.RecipesEntry.buildRecipeUri(id);
                } else {
                    throw new android.database.SQLException(FAILED_TO_INSERT_ROW_INTO + uri);
                }
                break;

            case INGREDIENT:
                id = db.insertWithOnConflict(BakingContract.IngredientsEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnUri = BakingContract.IngredientsEntry.buildIngredientUri(id);
                } else {
                    throw new android.database.SQLException(FAILED_TO_INSERT_ROW_INTO + uri);
                }
                break;

            case STEP:
                id = db.insertWithOnConflict(BakingContract.StepsEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnUri = BakingContract.StepsEntry.buildStepUri(id);
                } else {
                    throw new android.database.SQLException(FAILED_TO_INSERT_ROW_INTO + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case RECIPE:
                rowsDeleted = db.delete(BakingContract.RecipesEntry.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        BakingContract.RecipesEntry.TABLE_NAME + "'");
                break;
            case RECIPE_ID:
                String recipe_id = uri.getLastPathSegment();
                selection = BakingContract.RecipesEntry.COLUMN_RECIPE_ID + "=?";
                selectionArgs = new String[]{recipe_id};
                rowsDeleted = db.delete(BakingContract.RecipesEntry.TABLE_NAME,
                        selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        BakingContract.RecipesEntry.TABLE_NAME + "'");
                break;

            case INGREDIENT:
                rowsDeleted = db.delete(BakingContract.IngredientsEntry.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        BakingContract.IngredientsEntry.TABLE_NAME + "'");
                break;
            case INGREDIENT_ID:
                String ingredient_id = uri.getLastPathSegment();
                selection = BakingContract.IngredientsEntry.COLUMN_INGREDIENT_ID + "=?";
                selectionArgs = new String[]{ingredient_id};
                rowsDeleted = db.delete(BakingContract.IngredientsEntry.TABLE_NAME,
                        selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        BakingContract.IngredientsEntry.TABLE_NAME + "'");
                break;

            case STEP:
                rowsDeleted = db.delete(BakingContract.StepsEntry.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        BakingContract.StepsEntry.TABLE_NAME + "'");
                break;
            case STEP_ID:
                String step_id = uri.getLastPathSegment();
                selection = BakingContract.StepsEntry.COLUMN_STEP_ID + "=?";
                selectionArgs = new String[]{step_id};
                rowsDeleted = db.delete(BakingContract.StepsEntry.TABLE_NAME,
                        selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        BakingContract.StepsEntry.TABLE_NAME + "'");
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        final int match = sUriMatcher.match(uri);
        if (contentValues == null){
            throw new IllegalArgumentException(CANNOT_HAVE_NULL);
        }

        switch (match) {
            case RECIPE:
                rowsUpdated = db.update(BakingContract.RecipesEntry.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case RECIPE_ID:
                String recipe_id = uri.getLastPathSegment();
                selection = BakingContract.RecipesEntry.COLUMN_RECIPE_ID + "=?";
                selectionArgs = new String[]{recipe_id};
                rowsUpdated = db.update(BakingContract.RecipesEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);

            case INGREDIENT:
                rowsUpdated = db.update(BakingContract.IngredientsEntry.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case INGREDIENT_ID:
                String ingredient_id = uri.getLastPathSegment();
                selection = BakingContract.IngredientsEntry.COLUMN_INGREDIENT_ID + "=?";
                selectionArgs = new String[]{ingredient_id};
                rowsUpdated = db.update(BakingContract.IngredientsEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);

            case STEP:
                rowsUpdated = db.update(BakingContract.StepsEntry.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case STEP_ID:
                String step_id = uri.getLastPathSegment();
                selection = BakingContract.StepsEntry.COLUMN_STEP_ID + "=?";
                selectionArgs = new String[]{step_id};
                rowsUpdated = db.update(BakingContract.StepsEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] contentValues) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case RECIPE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : contentValues) {
                        if (value == null){
                            throw new IllegalArgumentException(CANNOT_HAVE_NULL);
                        }
                        long id = -1;
                        id = db.insertWithOnConflict(BakingContract.RecipesEntry.TABLE_NAME,
                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1) {
                            returnCount++;
                        }
                    }
                    if(returnCount > 0){
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if(returnCount > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return returnCount;

            case INGREDIENT:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : contentValues) {
                        if (value == null){
                            throw new IllegalArgumentException(CANNOT_HAVE_NULL);
                        }
                        long id = -1;
                        id = db.insertWithOnConflict(BakingContract.IngredientsEntry.TABLE_NAME,
                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1) {
                            returnCount++;
                        }
                    }
                    if(returnCount > 0){
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if(returnCount > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return returnCount;

            case STEP:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : contentValues) {
                        if (value == null){
                            throw new IllegalArgumentException(CANNOT_HAVE_NULL);
                        }
                        long id = -1;
                        id = db.insertWithOnConflict(BakingContract.StepsEntry.TABLE_NAME,
                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1) {
                            returnCount++;
                        }
                    }
                    if(returnCount > 0){
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if(returnCount > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return returnCount;
            default:
                return super.bulkInsert(uri, contentValues);
        }
    }

}

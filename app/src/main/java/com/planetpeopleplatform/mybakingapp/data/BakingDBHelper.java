package com.planetpeopleplatform.mybakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BakingDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "baking.db";


    public BakingDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RECIPE_TABLE =

                "CREATE TABLE " + BakingContract.RecipesEntry.TABLE_NAME + " (" +
                        BakingContract.RecipesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        BakingContract.RecipesEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        BakingContract.RecipesEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                        BakingContract.RecipesEntry.COLUMN_RECIPE_SERVING + " INTEGER NOT NULL, " +
                        BakingContract.RecipesEntry.COLUMN_RECIPE_IMAGE + " TEXT NOT NULL" +
                        "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);

        final String SQL_CREATE_INGREDIENT_TABLE =

                "CREATE TABLE " + BakingContract.IngredientsEntry.TABLE_NAME + " (" +
                        BakingContract.IngredientsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        BakingContract.IngredientsEntry.COLUMN_INGREDIENT_ID + " INTEGER NOT NULL, " +
                        BakingContract.IngredientsEntry.COLUMN_INGREDIENT_QUANTITY       + " REAL NOT NULL, " +
                        BakingContract.IngredientsEntry.COLUMN_INGREDIENT_MEASURE       + " TEXT NOT NULL, " +
                        BakingContract.IngredientsEntry.COLUMN_INGREDIENT_INGREDIENT       + " TEXT NOT NULL" +
                        "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);

        final String SQL_CREATE_STEP_TABLE =

                "CREATE TABLE " + BakingContract.StepsEntry.TABLE_NAME + " (" +

                        BakingContract.StepsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        BakingContract.StepsEntry.COLUMN_STEP_ID + " INTEGER NOT NULL, " +
                        BakingContract.StepsEntry.COLUMN_STEP_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                        BakingContract.StepsEntry.COLUMN_STEP_DESCRIPTION + " TEXT NOT NULL, " +
                        BakingContract.StepsEntry.COLUMN_STEP_VIDEO_URL + " TEXT NOT NULL, " +
                        BakingContract.StepsEntry.COLUMN_STEP_THUMBNAIL_URL + " TEXT NOT NULL" +
                        ");";
        sqLiteDatabase.execSQL(SQL_CREATE_STEP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.RecipesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                BakingContract.RecipesEntry.TABLE_NAME + "'");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.IngredientsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                BakingContract.IngredientsEntry.TABLE_NAME + "'");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.StepsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                BakingContract.StepsEntry.TABLE_NAME + "'");

        onCreate(sqLiteDatabase);
    }

}

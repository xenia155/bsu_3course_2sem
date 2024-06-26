package cnam.smb116.tp5.DBHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cnam.smb116.tp5.Model.Enseignant;

public class EnseignantsHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tp5.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ENSEIGNANTS = "Enseignants";
    public static final String NOM_COLONNE_ID = "Id";
    public static final String NOM_COLONNE_NOM = "Nom";
    public static final String NOM_COLONNE_PRENOM = "Prénom";
    public static final String NOM_COLONNE_COURRIEL = "Courriel";

    private static final String CREATION_TABLE = "CREATE TABLE " + TABLE_ENSEIGNANTS + " (" +
            NOM_COLONNE_ID + " integer primary key autoincrement, " +
            NOM_COLONNE_NOM + " text not null, " +
            NOM_COLONNE_PRENOM + " text not null, " +
            NOM_COLONNE_COURRIEL + " text not null);";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_ENSEIGNANTS + ";";

    private static final String GET_ALL = "SELECT * FROM " + TABLE_ENSEIGNANTS;

    public EnseignantsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == newVersion)
            return;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    // Insert une ligne dans la table Enseignants depuis un objet de type Enseignant
    public long insertEnseignant(Enseignant enseignant) {
        ContentValues values = new ContentValues();
        values.put(NOM_COLONNE_NOM, enseignant.Nom);
        values.put(NOM_COLONNE_PRENOM, enseignant.Prenom);
        values.put(NOM_COLONNE_COURRIEL, enseignant.Courriel);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_ENSEIGNANTS, null, values);
    }

    // Récupère toutes lignes de la table Enseignants et les convertis en un ArrayList d'objets de type Enseignant
    public ArrayList<Enseignant> getEnseignants() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(GET_ALL, null);
        ArrayList<Enseignant> enseignants = new ArrayList<Enseignant>();
        while (cursor.moveToNext()){
            @SuppressLint("Range") Enseignant enseignant =
                    new Enseignant(cursor.getInt(cursor.getColumnIndex(NOM_COLONNE_ID)),
                    cursor.getString(cursor.getColumnIndex(NOM_COLONNE_NOM)),
                    cursor.getString(cursor.getColumnIndex(NOM_COLONNE_PRENOM)),
                    cursor.getString(cursor.getColumnIndex(NOM_COLONNE_COURRIEL)));
            enseignants.add(enseignant);
        }

        return enseignants;
    }
}

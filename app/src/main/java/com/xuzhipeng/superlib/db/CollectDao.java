package com.xuzhipeng.superlib.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COLLECT".
*/
public class CollectDao extends AbstractDao<Collect, Long> {

    public static final String TABLENAME = "COLLECT";

    /**
     * Properties of entity Collect.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, Long.class, "userId", false, "USER_ID");
        public final static Property BookId = new Property(2, Long.class, "bookId", false, "BOOK_ID");
        public final static Property Like = new Property(3, boolean.class, "like", false, "LIKE");
    }


    public CollectDao(DaoConfig config) {
        super(config);
    }
    
    public CollectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COLLECT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_ID\" INTEGER," + // 1: userId
                "\"BOOK_ID\" INTEGER," + // 2: bookId
                "\"LIKE\" INTEGER NOT NULL );"); // 3: like
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COLLECT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Collect entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(2, userId);
        }
 
        Long bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindLong(3, bookId);
        }
        stmt.bindLong(4, entity.getLike() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Collect entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(2, userId);
        }
 
        Long bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindLong(3, bookId);
        }
        stmt.bindLong(4, entity.getLike() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Collect readEntity(Cursor cursor, int offset) {
        Collect entity = new Collect( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // bookId
            cursor.getShort(offset + 3) != 0 // like
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Collect entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setBookId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setLike(cursor.getShort(offset + 3) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Collect entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Collect entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Collect entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

package com.pluralsight;

import com.pluralsight.BucketItem;
import com.pluralsight.BucketItem.Category;
import com.pluralsight.BucketItem.Priority;
import com.pluralsight.BucketItemRepository;
import com.pluralsight.StorageException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Production‑ready repository backed by Azure SQL using Hikari connection‑pooling.
 */
public final class DatabaseBucketRepository implements BucketItemRepository {

    /* ------------------------------------------------------------------ */
    /* Datasource (connection pool)                                       */
    /* ------------------------------------------------------------------ */

    private static final DataSource DS;
    static {
        var cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:sqlserver://skills4it.database.windows.net:1433;database=yearup;");
        cfg.setUsername("bucket_user");
        cfg.setPassword("SuperSecurePassword123!");
        cfg.addDataSourceProperty("encrypt", "true");
        cfg.addDataSourceProperty("trustServerCertificate", "false");
        cfg.setMaximumPoolSize(5);
        DS = new HikariDataSource(cfg);
    }

    /* ------------------------------------------------------------------ */
    /* SQL templates                                                      */
    /* ------------------------------------------------------------------ */

    private static final String INSERT = """
        INSERT INTO BucketItems
          (title,description,category,priority,createdDate,targetDate,
           completedDate,isDone,notes,imageUrl)
        VALUES (?,?,?,?,?,?,?,?,?,?)""";

    private static final String UPDATE = """
        UPDATE BucketItems SET
          title=?,description=?,category=?,priority=?,targetDate=?,
          completedDate=?,isDone=?,notes=?,imageUrl=?
        WHERE id=?""";

    private static final String DELETE_BY_ID = "DELETE FROM BucketItems WHERE id=?";
    private static final String SELECT_ALL  = "SELECT * FROM BucketItems ORDER BY priority, id";
    private static final String SELECT_BY_ID = "SELECT * FROM BucketItems WHERE id=?";

    /* ------------------------------------------------------------------ */
    /* Repository implementation                                          */
    /* ------------------------------------------------------------------ */

    @Override
    public BucketItem save(BucketItem item) throws StorageException {
        try (Connection c = DS.getConnection()) {
            if (item.id() == 0) {
                try (PreparedStatement ps = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    bind(ps, item, false);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) return item.withId(keys.getInt(1));
                        throw new StorageException("Failed to obtain generated id");
                    }
                }
            } else {
                try (PreparedStatement ps = c.prepareStatement(UPDATE)) {
                    bind(ps, item, true);
                    ps.setInt(10, item.id());
                    ps.executeUpdate();
                    return item;
                }
            }
        } catch (SQLException ex) {
            throw new StorageException("DB save failed", ex);
        }
    }

    @Override
    public void delete(int id) throws StorageException {
        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE_BY_ID)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) { throw new StorageException("DB delete failed", ex); }
    }

    @Override
    public List<BucketItem> findAll() throws StorageException {
        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            return mapAll(rs);
        } catch (SQLException ex) { throw new StorageException("DB read failed", ex); }
    }

    @Override
    public Optional<BucketItem> findById(int id) throws StorageException {
        try (Connection c = DS.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException ex) { throw new StorageException("DB read failed", ex); }
    }

    /* ------------------------------------------------------------------ */
    /* Helpers                                                            */
    /* ------------------------------------------------------------------ */

    private static void bind(PreparedStatement ps, BucketItem i, boolean forUpdate) throws SQLException {
        ps.setString(1, i.title());
        ps.setString(2, i.description());
        ps.setString(3, i.category().name());
        ps.setInt   (4, i.priority().value);
        if (!forUpdate) ps.setDate(5, Date.valueOf(i.createdDate()));
        ps.setDate  (5, i.targetDate()     != null ? Date.valueOf(i.targetDate())     : null);
        ps.setDate  (6, i.completedDate()  != null ? Date.valueOf(i.completedDate())  : null);
        ps.setBoolean(7, i.done());
        ps.setString(8, i.notes());
        ps.setString(9, i.imageUrl());
    }

    private static List<BucketItem> mapAll(ResultSet rs) throws SQLException {
        List<BucketItem> list = new ArrayList<>();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    private static BucketItem map(ResultSet r) throws SQLException {
        return new BucketItem(
                r.getInt("id"),
                r.getString("title"),
                r.getString("description"),
                Category.valueOf(r.getString("category")),
                Priority.from(r.getInt("priority")),
                r.getDate("createdDate").toLocalDate(),
                r.getDate("targetDate")     != null ? r.getDate("targetDate").toLocalDate()     : null,
                r.getDate("completedDate")  != null ? r.getDate("completedDate").toLocalDate()  : null,
                r.getBoolean("isDone"),
                r.getString("notes"),
                r.getString("imageUrl"));
    }
}

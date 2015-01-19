package net.madvirus.eval.axon;

import org.axonframework.eventstore.jdbc.EventSqlSchema;
import org.axonframework.eventstore.jpa.SimpleSerializedDomainEventData;
import org.axonframework.serializer.SerializedDomainEventData;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomEventSqlSchema<T> implements EventSqlSchema<T> {
    private static final DateTimeFormatter UTC_FORMATTER = ISODateTimeFormat.dateTime().withZoneUTC();

    private static final String STD_FIELDS =
            "eventIdentifier, aggregateIdentifier, sequenceNumber, timeStamp, " +
                    "payloadType, payloadRevision, payload, metaData";

    private Class<T> dataType;
    private String snapshotEventEntryTable = "SnapshotEventEntry";
    private String domainEventEntryTable = "DomainEventEntry";
    private boolean forceUtc = false;

    public CustomEventSqlSchema() {
        this((Class<T>) byte[].class);
    }

    public CustomEventSqlSchema(Class<T> dataType) {
        this.dataType = dataType;
    }

    public void setForceUtc(boolean forceUtc) {
        this.forceUtc = forceUtc;
    }

    public void setSnapshotEventEntryTable(String snapshotEventEntryTable) {
        this.snapshotEventEntryTable = snapshotEventEntryTable;
    }

    public void setDomainEventEntryTable(String domainEventEntryTable) {
        this.domainEventEntryTable = domainEventEntryTable;
    }

    @Override
    public PreparedStatement sql_loadLastSnapshot(Connection connection, Object identifier, String aggregateType)
            throws SQLException {
        final String s = "SELECT " + STD_FIELDS
                + " FROM " + snapshotEventEntryTable
                + " WHERE aggregateIdentifier = ? AND type = ? ORDER BY sequenceNumber DESC";
        PreparedStatement statement = connection.prepareStatement(s);
        statement.setString(1, identifier.toString());
        statement.setString(2, aggregateType);
        return statement;
    }

    @Override
    public PreparedStatement sql_insertDomainEventEntry(Connection conn, String eventIdentifier,
                                                        String aggregateIdentifier, long sequenceNumber,
                                                        DateTime timestamp, String eventType, String eventRevision,
                                                        T eventPayload, T eventMetaData, String aggregateType)
            throws SQLException {
        return doInsertEventEntry(domainEventEntryTable,
                conn, eventIdentifier, aggregateIdentifier, sequenceNumber, timestamp,
                eventType, eventRevision, eventPayload,
                eventMetaData, aggregateType);
    }

    @Override
    public PreparedStatement sql_insertSnapshotEventEntry(Connection conn, String eventIdentifier,
                                                          String aggregateIdentifier, long sequenceNumber,
                                                          DateTime timestamp, String eventType, String eventRevision,
                                                          T eventPayload, T eventMetaData,
                                                          String aggregateType) throws SQLException {
        return doInsertEventEntry(snapshotEventEntryTable,
                conn, eventIdentifier, aggregateIdentifier, sequenceNumber, timestamp,
                eventType, eventRevision, eventPayload,
                eventMetaData, aggregateType);
    }

    protected PreparedStatement doInsertEventEntry(String tableName, Connection connection, String eventIdentifier,
                                                   String aggregateIdentifier,
                                                   long sequenceNumber, DateTime timestamp, String eventType,
                                                   String eventRevision,
                                                   T eventPayload, T eventMetaData, String aggregateType)
            throws SQLException {
        final String sql = "INSERT INTO " + tableName
                + " (eventIdentifier, type, aggregateIdentifier, sequenceNumber, timeStamp, payloadType, "
                + "payloadRevision, payload, metaData) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql); // NOSONAR
        preparedStatement.setString(1, eventIdentifier);
        preparedStatement.setString(2, aggregateType);
        preparedStatement.setString(3, aggregateIdentifier);
        preparedStatement.setLong(4, sequenceNumber);
        preparedStatement.setString(5, sql_dateTime(timestamp));
        preparedStatement.setString(6, eventType);
        preparedStatement.setString(7, eventRevision);
        preparedStatement.setObject(8, eventPayload);
        preparedStatement.setObject(9, eventMetaData);
        return preparedStatement;
    }

    @Override
    public PreparedStatement sql_pruneSnapshots(Connection connection, String type, Object aggregateIdentifier,
                                                long sequenceOfFirstSnapshotToPrune) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + snapshotEventEntryTable + " "
                + "WHERE type = ? "
                + "AND aggregateIdentifier = ? "
                + "AND sequenceNumber <= ?");
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, aggregateIdentifier.toString());
        preparedStatement.setLong(3, sequenceOfFirstSnapshotToPrune);
        return preparedStatement;
    }

    @Override
    public PreparedStatement sql_findSnapshotSequenceNumbers(Connection connection, String type,
                                                             Object aggregateIdentifier) throws SQLException {
        final String sql = "SELECT sequenceNumber FROM " + snapshotEventEntryTable + " "
                + "WHERE type = ? AND aggregateIdentifier = ? "
                + "ORDER BY sequenceNumber DESC";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, aggregateIdentifier.toString());
        return preparedStatement;
    }

    @Override
    public PreparedStatement sql_fetchFromSequenceNumber(Connection connection, String type, Object aggregateIdentifier,
                                                         long firstSequenceNumber) throws SQLException {
        final String sql = "SELECT " + STD_FIELDS
                + " FROM " + domainEventEntryTable + " "
                + "WHERE aggregateIdentifier = ? AND type = ? "
                + "AND sequenceNumber >= ? "
                + "ORDER BY sequenceNumber ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, aggregateIdentifier.toString());
        preparedStatement.setString(2, type);
        preparedStatement.setLong(3, firstSequenceNumber);
        return preparedStatement;
    }

    @Override
    public PreparedStatement sql_getFetchAll(Connection connection, String whereClause,
                                             Object[] params) throws SQLException {
        final String sql = "select " + STD_FIELDS +
                " from " + domainEventEntryTable + " e " + whereClause +
                " ORDER BY e.timeStamp ASC, e.sequenceNumber ASC, e.aggregateIdentifier ASC ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof DateTime) {
                param = sql_dateTime((DateTime) param);
            }

            if (param instanceof byte[]) {
                preparedStatement.setBytes(i + 1, (byte[]) param);
            } else {
                preparedStatement.setObject(i + 1, param);
            }
        }
        return preparedStatement;
    }

    protected Object readTimeStamp(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getString(columnIndex);
    }

    @SuppressWarnings("unchecked")
    protected T readPayload(ResultSet resultSet, int columnIndex) throws SQLException {
        if (byte[].class.equals(dataType)) {
            return (T) resultSet.getBytes(columnIndex);
        }
        return (T) resultSet.getObject(columnIndex);
    }

    @Override
    public PreparedStatement sql_createSnapshotEventEntryTable(Connection connection) throws SQLException {
        final String sql = "    create table " + snapshotEventEntryTable + " (\n" +
                "        aggregateIdentifier varchar(255) not null,\n" +
                "        sequenceNumber bigint not null,\n" +
                "        type varchar(255) not null,\n" +
                "        eventIdentifier varchar(255) not null,\n" +
                "        metaData blob,\n" +
                "        payload blob not null,\n" +
                "        payloadRevision varchar(255),\n" +
                "        payloadType varchar(255) not null,\n" +
                "        timeStamp varchar(255) not null,\n" +
                "        primary key (aggregateIdentifier, sequenceNumber, type)\n" +
                "    );";
        return connection.prepareStatement(sql);
    }

    @Override
    public PreparedStatement sql_createDomainEventEntryTable(Connection connection) throws SQLException {
        final String sql = "create table " + domainEventEntryTable + " (\n" +
                "        aggregateIdentifier varchar(255) not null,\n" +
                "        sequenceNumber bigint not null,\n" +
                "        type varchar(255) not null,\n" +
                "        eventIdentifier varchar(255) not null,\n" +
                "        metaData blob,\n" +
                "        payload blob not null,\n" +
                "        payloadRevision varchar(255),\n" +
                "        payloadType varchar(255) not null,\n" +
                "        timeStamp varchar(255) not null,\n" +
                "        primary key (aggregateIdentifier, sequenceNumber, type)\n" +
                "    );\n";
        return connection.prepareStatement(sql);
    }

    @Override
    public SerializedDomainEventData<T> createSerializedDomainEventData(ResultSet resultSet) throws SQLException {
        return new SimpleSerializedDomainEventData<T>(resultSet.getString(1), resultSet.getString(2),
                resultSet.getLong(3), readTimeStamp(resultSet, 4),
                resultSet.getString(5), resultSet.getString(6),
                readPayload(resultSet, 7),
                readPayload(resultSet, 8));
    }

    @Override
    public String sql_dateTime(DateTime input) {
        if (forceUtc) {
            return input.toString(UTC_FORMATTER);
        } else {
            return input.toString();
        }
    }

    @Override
    public Class<T> getDataType() {
        return dataType;
    }
}

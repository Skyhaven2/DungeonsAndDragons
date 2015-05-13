package dungeonsanddragons.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import dungeonsanddragons.model.FieldRelationData;

public class DandDDatabaseController
{

	/**
	 * This is the URL for the database.
	 */
	private String connectionString;
	/**
	 * This is the connection to the database.
	 */
	private Connection databaseConnection;
	/**
	 * This is the internal reference to the appController.
	 */
	private DandDAppController baseController;
	/**
	 * This is the primary key column name of the table that a column comes from
	 * example "Player" is column name that comes from the "Players" table the
	 * "Players" table's primary key is "Player_id" so the value of
	 * primaryKeyColumnName is "Player_id"
	 */
	private String primaryKeyColumnName;
	/**
	 * This is an array of the column header names of the current selected table
	 */
	private String[] columnNames;
	/**
	 * This is the information used to create the current selected table
	 */
	private String[][] tableData;
	/**
	 * Contains FieldRelationData objects that each contain information
	 * about how a column is related to other columns in other tables
	 */
	private FieldRelationData[] foreignKeyRelationData;
	
	/**
	 * Contains information of what column contains the most important info for
	 * each table col 0 is the table name col 1 is the most important column
	 * name
	 */
	private String[][] DandDTableMostImportantColumnNames;

	public DandDDatabaseController(DandDAppController baseController)
	{
		this.baseController = baseController;
		connectionString = "jdbc:mysql://localhost/dungeons_and_dragons?user=root";
		checkDriver();
		setupConnection();
		buildDandDMostImportantColumnNames();
	}

	// Start, Connection, and error methods
	// ----------------------------------------------------------------------------------------------------------------------

	/**
	 * Checks if the driver exists. If it doesn't, it will display the error
	 * code and exit.
	 */
	private void checkDriver()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception currentException)
		{
			displayErrors(currentException);
			System.exit(1);
		}
	}

	/**
	 * Attempts to connect to the database. If fails, displays the error code.
	 */
	public void setupConnection()
	{
		try
		{
			databaseConnection = DriverManager.getConnection(connectionString);
		}
		catch (SQLException currentException)
		{
			displayErrors(currentException);
		}
	}

	/**
	 * Displays the general error code, SQL state, and SQL error code.
	 * 
	 * @param currentException
	 */
	public void displayErrors(Exception currentException)
	{
		JOptionPane.showMessageDialog(baseController.getFrame(), currentException.getMessage());

		if (currentException instanceof SQLException)
		{
			JOptionPane.showMessageDialog(baseController.getFrame(), "SQL State: " + ((SQLException) currentException).getSQLState());
			JOptionPane.showMessageDialog(baseController.getFrame(), "SQL Error Code: " + ((SQLException) currentException).getErrorCode());
		}
	}

	/**
	 * Breaks the connection with the database server.
	 */
	public void closeConnection()
	{
		try
		{
			databaseConnection.close();
		}
		catch (Exception currentException)
		{
			displayErrors(currentException);
		}
	}

	/**
	 * Creates the DandDTableMostImportantColumnNames array
	 * 
	 * @return the built array
	 */
	private String[][] buildDandDMostImportantColumnNames()
	{
		DandDTableMostImportantColumnNames = new String[7][2];

		DandDTableMostImportantColumnNames[0][0] = "characters";
		DandDTableMostImportantColumnNames[0][1] = "character_name";
		DandDTableMostImportantColumnNames[1][0] = "classes";
		DandDTableMostImportantColumnNames[1][1] = "class_name";
		DandDTableMostImportantColumnNames[2][0] = "cleric_spells";
		DandDTableMostImportantColumnNames[2][1] = "spell_name";
		DandDTableMostImportantColumnNames[3][0] = "countries";
		DandDTableMostImportantColumnNames[3][1] = "country_name";
		DandDTableMostImportantColumnNames[4][0] = "parent_races";
		DandDTableMostImportantColumnNames[4][1] = "parent_race";
		DandDTableMostImportantColumnNames[5][0] = "players";
		DandDTableMostImportantColumnNames[5][1] = "player_name";
		DandDTableMostImportantColumnNames[6][0] = "races";
		DandDTableMostImportantColumnNames[6][1] = "race";

		return DandDTableMostImportantColumnNames;
	}

	// Helper Methods (Check or find)
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * This method finds the number of rows in a ResultSet
	 * 
	 * @param searchResultSet
	 *            The set to search in.
	 * @return The number of rows in the set.
	 */
	private int findNumberOfRows(ResultSet searchResultSet)
	{
		int numberOfRows = 0;
		try
		{
			searchResultSet.last();
			numberOfRows = searchResultSet.getRow();
			searchResultSet.beforeFirst();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}
		return numberOfRows;
	}

	/**
	 * Finds a cell string value
	 * 
	 * @param columnHeaderName
	 *            The name of the column header of the column to be selected
	 *            from
	 * @param row
	 *            The row to be selected from
	 * @return A string value of a selected cell
	 */
	private String findCellValue(String columnHeaderName, int row)
	{
		String cellValue = "";

		for (int col = 0; col < columnNames.length; col++)
		{
			if (columnNames[col].equals(columnHeaderName))
			{
				cellValue = tableData[row][col];
			}
		}

		return cellValue;
	}

	/**
	 * This method returns a String[] containing a list of available tables
	 * located in the dungeons_and_dragons database
	 * 
	 * @return a list of available tables in dungeons_and_dragons database
	 */
	public String[] findTablesDandD()
	{
		String query = "SHOW TABLES FROM ";
		String database = "dungeons_and_dragons";
		query += database;

		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int numberOfRows = findNumberOfRows(answer);
			String[] results = new String[numberOfRows];
			int currentRow = 0;
			while (answer.next())
			{
				results[currentRow] = answer.getString(1);
				currentRow++;
			}
			answer.close();
			firstStatement.close();
			return results;
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
			return new String[0];
		}
	}

	/**
	 * Checks if a given field/column's data has a foreign key attached to it if
	 * it does, then that column needs to be draw with a ComboBox
	 * 
	 * @param columnNumber
	 *            the column to be checked
	 * @return true if the column needs to be drawn with a ComboBox
	 */
	public boolean checkIfComboBoxForEdit(int columnNumber)
	{
		boolean needsAComboBox = false;

		for (int row = 0; row < foreignKeyRelationData.length; row++)
		{
			if (foreignKeyRelationData[row].getColName().equals(columnNames[columnNumber]))
			{
				needsAComboBox = true;
			}
		}

		return needsAComboBox;
	}

	/**
	 * Finds the relevant foreign key relation data for the given column in the
	 * selected table
	 * 
	 * @param columnNumber
	 *            The column that needs its key relation information
	 * @return the row in foriegnKeyRelationData where the info is stored
	 */
	private int findRelationRow(int columnNumber)
	{
		int relationRow = -1;
		for (int row = 0; row < foreignKeyRelationData.length; row++)
		{
			if (foreignKeyRelationData[row].getColName().equals(columnNames[columnNumber]))
			{
				relationRow = row;
			}
		}
		return relationRow;
	}

	/**
	 * Finds the associated most important column name (used with JComboBox) for
	 * the table located in the given row of foriegnKeyRelationData
	 * 
	 * @param relationRow
	 *            the row in foriegnKeyRelationData where the desired related
	 *            table name is stored
	 * @return the column name of the related table that should be shown
	 */
	private String findMostImportantColumnName(int relationRow)
	{
		String colName = "";
		for (int location = 0; location < DandDTableMostImportantColumnNames.length; location++)
		{
			if (foreignKeyRelationData[relationRow].getRelatedTableName().equals(DandDTableMostImportantColumnNames[location][0]))
			{
				colName = DandDTableMostImportantColumnNames[location][1];
			}
		}
		return colName;
	}

	/**
	 * Finds the primary id of a certain value in a table. Used with JComboBox.
	 * The player Camron has an id of 1. I choose Camron in the JComboBox and
	 * this method is used to find his id of 1 to pass to the database.
	 * 
	 * @param value
	 *            a non-primary value located in a related table
	 * @param columnNumber
	 *            the column the JComboBox is found in
	 * @return the associated id for the value
	 */
	public String findIdInRelatedTable(String value, int columnNumber)
	{
		int relationRow = findRelationRow(columnNumber);

		String query = "SELECT `" + foreignKeyRelationData[relationRow].getRelatedTableName() + "`.`" + foreignKeyRelationData[relationRow].getRelatedColumnName() + "`, `" + foreignKeyRelationData[relationRow].getRelatedTableName() + "`.`";

		query += findMostImportantColumnName(relationRow);

		query += "` FROM `" + foreignKeyRelationData[relationRow].getRelatedTableName() + "`";

		// Col 0 is id and Col 1 is the col used by the JCombobox
		String[][] idAndValues = runSELECTQueryGetTable(query);
		String newId = "";

		for (int row = 0; row < idAndValues.length; row++)
		{
			if (idAndValues[row][1].equals(value))
			{
				newId = idAndValues[row][0];
			}
		}

		return newId;
	}

	// collectTableInfo Methods
	// -----------------------------------------------------------------------------------------------------------------

	/**
	 * fills that variables related to table information
	 * 
	 * @param tableName
	 *            The table to collect info from.
	 */
	public void collectTableInfo(String tableName)
	{
		String query = "SELECT * FROM " + tableName;

		try
		{
			Statement SELECTStatement = databaseConnection.createStatement();
			ResultSet answer = SELECTStatement.executeQuery(query);
			ResultSetMetaData metaDataOfAnswer = answer.getMetaData();
			int numberOfColumns = metaDataOfAnswer.getColumnCount();
			int numberOfRows = findNumberOfRows(answer);
			tableData = new String[numberOfRows][numberOfColumns];
			columnNames = new String[numberOfColumns];
			int currentRow = 0;
			while (answer.next())
			{
				for (int col = 0; col < numberOfColumns; col++)
				{
					tableData[currentRow][col] = answer.getString(col + 1);
				}
				currentRow++;
			}
			for (int currentCol = 0; currentCol < numberOfColumns; currentCol++)
			{
				columnNames[currentCol] = metaDataOfAnswer.getColumnLabel(currentCol + 1);
			}
			setPrimaryKeyColumnName(tableName);
			foreignKeyRelationData = getForeignKeyRelationData(tableName);
			answer.close();
			SELECTStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}

	}

	/**
	 * Sets the primary key column name variable
	 * 
	 * @param tableName
	 *            The table to be searched
	 * 
	 */
	private void setPrimaryKeyColumnName(String tableName)
	{
		try
		{
			DatabaseMetaData metaDataOfDatabase = databaseConnection.getMetaData();
			ResultSet primaryKeyResultSet = metaDataOfDatabase.getPrimaryKeys(null, null, tableName);
			primaryKeyResultSet.next();
			primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
			primaryKeyResultSet.close();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}
	}

	/**
	 * Finds the foreign key relation data
	 * 
	 * @param tableName
	 *            The table to analyze
	 * @return the foreign key relation data for each column in the table
	 */
	private FieldRelationData[] getForeignKeyRelationData(String tableName)
	{
		try
		{
			DatabaseMetaData metaDataOfDatabase = databaseConnection.getMetaData();
			ResultSet foreignKeyResultSet = metaDataOfDatabase.getImportedKeys(null, null, tableName);
			int numberOfRows = findNumberOfRows(foreignKeyResultSet);
			FieldRelationData[] foreignKeyRelations = new FieldRelationData[numberOfRows];
			int row = 0;
			while (foreignKeyResultSet.next())
			{
				foreignKeyRelations[row] = new FieldRelationData(foreignKeyResultSet.getString("PKCOLUMN_NAME"), foreignKeyResultSet.getString("PKTABLE_NAME"), foreignKeyResultSet.getString("FKTABLE_NAME"), foreignKeyResultSet.getString("FKCOLUMN_NAME"));

				row++;
			}
			foreignKeyResultSet.close();
			return foreignKeyRelations;
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
			FieldRelationData[] emptyArray = new FieldRelationData[0];
			emptyArray[0] = new FieldRelationData("", "", "", "");
			return emptyArray;
		}
	}

	// Build query methods
	// ---------------------------------------------------------------------------------------------------------------

	public String buildSELECTQuery(String tableName)
	{
		collectTableInfo(tableName);
		String SELECTQuery = buildSELECTPartOfQuery(tableName);
		SELECTQuery += buildFROMPartOfQuery(tableName);
		SELECTQuery += buildWHEREPartOfQuery(tableName);

		return SELECTQuery;
	}

	/**
	 * This is sub-method for buildSELECTQuery This builds the SELECT part of
	 * the query Only selects primary data (no IDs)
	 * 
	 * @param tableName
	 *            Table to select from
	 * @return String of the first part of the query
	 */
	private String buildSELECTPartOfQuery(String tableName)
	{
		String SELECTQuery = "SELECT";
		boolean columnRecorded = false;

		for (int col = 0; col < columnNames.length; col++)
		{
			for (int row = 0; row < foreignKeyRelationData.length; row++)
			{
				if (columnNames[col].equals(foreignKeyRelationData[row].getColName()))
				{
					SELECTQuery += " `" + foreignKeyRelationData[row].getRelatedTableName() + "`.`";
					for (int location = 0; location < DandDTableMostImportantColumnNames.length; location++)
					{
						if (foreignKeyRelationData[row].getRelatedTableName().equals(DandDTableMostImportantColumnNames[location][0]))
						{
							SELECTQuery += DandDTableMostImportantColumnNames[location][1];
						}
					}
					SELECTQuery += "`";
					columnRecorded = true;
					if (col < columnNames.length - 1)
					{
						SELECTQuery += ",";
					}
				}
			}
			if (!columnRecorded)
			{
				SELECTQuery += " `" + tableName + "`.`" + columnNames[col] + "`";
				if (col < columnNames.length - 1)
				{
					SELECTQuery += ",";
				}
			}
			columnRecorded = false;
		}

		return SELECTQuery;
	}

	/**
	 * This is sub-method for buildSELECTQuery This builds the FROM part of the
	 * query SELECTS all tables required for the SELECT query
	 * 
	 * @param tableName
	 *            Table to select from
	 * @return String of the second part of the query
	 */
	private String buildFROMPartOfQuery(String tableName)
	{
		String FROMQuery = " FROM `" + tableName + "`";

		String tablesAlreadySelected = "";
		for (int row = 0; row < foreignKeyRelationData.length; row++)
		{
			if (!(tablesAlreadySelected.contains(foreignKeyRelationData[row].getRelatedTableName())))
			{
				tablesAlreadySelected += foreignKeyRelationData[row].getRelatedTableName() + " ";
				FROMQuery += ", `" + foreignKeyRelationData[row].getRelatedTableName() + "`";
			}
		}

		return FROMQuery;
	}

	/**
	 * This is sub-method for buildSELECTQuery This builds the WHERE part of the
	 * query Restricts table to only where the foriegn key column and related
	 * column are the same
	 * 
	 * @param tableName
	 *            Table to select from
	 * @return String of the third part of the query
	 */
	private String buildWHEREPartOfQuery(String tableName)
	{
		String WHEREQuery = " WHERE ";
		boolean firstWhereRecorded = false;

		for (int col = 0; col < columnNames.length; col++)
		{
			for (int row = 0; row < foreignKeyRelationData.length; row++)
			{
				if (columnNames[col].equals(foreignKeyRelationData[row].getColName()))
				{
					if (firstWhereRecorded)
					{
						WHEREQuery += "AND ";
					}
					WHEREQuery += "`" + foreignKeyRelationData[row].getRelatedTableName() + "`.`" + foreignKeyRelationData[row].getRelatedColumnName() + "`";
					WHEREQuery += " = ";
					WHEREQuery += "`" + tableName + "`.`" + columnNames[col] + "` ";
					firstWhereRecorded = true;
				}
			}
		}

		if (WHEREQuery.equals(" WHERE "))
		{
			WHEREQuery = "";
		}

		return WHEREQuery;
	}

	// Run methods
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * This method returns a 2d Array of the values retrieved from the query
	 * call.
	 * 
	 * @param query
	 *            The query to be sent to the database.
	 * @return 2d array of the values retrieved from the query
	 */
	public String[][] runSELECTQueryGetTable(String query)
	{
		try
		{
			Statement SELECTStatement = databaseConnection.createStatement();
			ResultSet answer = SELECTStatement.executeQuery(query);
			ResultSetMetaData metaDataOfAnswer = answer.getMetaData();
			int numberOfColumns = metaDataOfAnswer.getColumnCount();
			int numberOfRows = findNumberOfRows(answer);
			String[][] results = new String[numberOfRows][numberOfColumns];
			int currentRow = 0;
			while (answer.next())
			{
				for (int col = 0; col < numberOfColumns; col++)
				{
					results[currentRow][col] = answer.getString(col + 1);
				}
				currentRow++;
			}
			answer.close();
			SELECTStatement.close();
			return results;
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
			return (new String[][] { { "An error occured" } });
		}

	}

	/**
	 * This method returns a string array of the column headers retrieved form
	 * the query call
	 * 
	 * @param query
	 *            The query to be sent to the database.
	 * @return a string array of the column headers
	 */
	public String[] runSELECTQueryGetColumnNames(String query)
	{

		try
		{
			Statement SELECTStatement = databaseConnection.createStatement();
			ResultSet answer = SELECTStatement.executeQuery(query);
			ResultSetMetaData metaDataOfAnswer = answer.getMetaData();
			int numberOfColumns = metaDataOfAnswer.getColumnCount();
			String[] localColumnNames = new String[numberOfColumns];
			for (int currentCol = 0; currentCol < numberOfColumns; currentCol++)
			{
				localColumnNames[currentCol] = metaDataOfAnswer.getColumnLabel(currentCol + 1);
			}
			answer.close();
			SELECTStatement.close();
			return localColumnNames;
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
			return null;
		}
	}

	/**
	 * This method sends a update query to the database that changes a single
	 * cell.
	 * 
	 * @param newData
	 *            The new value to set the cell to
	 * @param column
	 *            The column that the new data is located in the 2d string array
	 *            of database values.
	 * @param row
	 *            The row that the new data is located in the 2d string array of
	 *            database values.
	 * @return The number of rows affected by the update call. Will return 0 if
	 *         update failed.
	 */
	public int runUPDATEQuery(String newData, int column, int row, String tableName)
	{
		collectTableInfo(tableName);
		String UPDATEquery = ("UPDATE `dungeons_and_dragons`.`" + tableName + "` SET `" + columnNames[column] + "` = '" + newData + "' WHERE `" + tableName + "`.`" + primaryKeyColumnName + "` = " + findCellValue(primaryKeyColumnName, row));
		try
		{
			Statement updateStatement = databaseConnection.createStatement();
			int rowsAffected = updateStatement.executeUpdate(UPDATEquery);
			updateStatement.close();
			return rowsAffected;
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
			return 0;
		}
	}

	/**
	 * Gets the values to be used in an individual column's JComboBoxes
	 * @param columnNumber the column that the values will be used in
	 * @return the values to put into the JComboBoxes
	 */
	public String[] getComboBoxForEdit(int columnNumber)
	{
		int relationRow = findRelationRow(columnNumber);

		String query = "SELECT `" + foreignKeyRelationData[relationRow].getRelatedTableName() + "`.`";

		query += findMostImportantColumnName(relationRow);

		query += "` FROM " + foreignKeyRelationData[relationRow].getRelatedTableName();
		String[] columnValues = new String[0];

		try
		{
			Statement SELECTStatement = databaseConnection.createStatement();
			ResultSet answer = SELECTStatement.executeQuery(query);
			int numberOfRows = findNumberOfRows(answer);
			columnValues = new String[numberOfRows];
			int currentRow = 0;
			while (answer.next())
			{
				columnValues[currentRow] = answer.getString(1);
				currentRow++;
			}
			answer.close();
			SELECTStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
			return null;
		}

		String[] columnComboBoxData = new String[columnValues.length];

		for (int place = 0; place < columnValues.length; place++)
		{
			columnComboBoxData[place] = columnValues[place];
		}

		return columnComboBoxData;
	}
}
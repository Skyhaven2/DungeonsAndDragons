package dungeonsanddragons.model;

public class FieldRelationData
{
	private String relatedColumnName;
	private String relatedTableName;
	private String tableName;
	private String colName;
	
	public FieldRelationData(String relatedColumnName, String relatedTableName, String tableName, String colName)
	{
		this.relatedColumnName = relatedColumnName;
		this.relatedTableName = relatedTableName;
		this.tableName = tableName;
		this.colName = colName;
	}

	public String getRelatedColumnName()
	{
		return relatedColumnName;
	}

	public String getRelatedTableName()
	{
		return relatedTableName;
	}

	public String getTableName()
	{
		return tableName;
	}

	public String getColName()
	{
		return colName;
	}
}

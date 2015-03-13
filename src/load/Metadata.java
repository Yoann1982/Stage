package load;

import java.sql.Date;

/**
 * Cette classe correspond au contenu de la table Metadata d'I2B2
 * 
 * @author Yoann Keravec Date: 13/03/2015<br>
 *         Institut Bergonié<br>
 */

public class Metadata {

	private int cHLevel; // Correspond à C_HLEVEL
	private String cFullName; // Correspond à C_FULLNAME
	private String cName; // Correspond à C_NAME
	private char cSynonym; // Correspond à C_SYNONYM_CD
	private String cVisualAttributes; // Correspond à C_VISUALATTRIBUTES
	private int cTotalNum; //C_TOTALNUM
	private String cBaseCode; // Correspond à C_BASECODE
	private String cMetaDataXML; // Correspond à C_METADATAXML
	private String cFactTableColumn; // Correspond à C_FACTTABLECOLUMN
	private String cTableName; // Correspond à C_TABLENAME
	private String cColumnName; // Correspond à C_COLUMNNAME
	private String cColumnDataType; // Correspond à C_COLUMNDATATYPE
	private String cOperator; // Correspond à C_OPERATOR
	private String cDimCode; // Correspond à C_DIMCODE
	private String cComment; // Correspond à C_COMMENT
	private String cTooltip; // Correspond à C_TOOLTIP 
	private Date updateDate; // Correspond à UPDATE_DATE
	private Date downloadDate; // Correspond à DOWNLOAD_DATE
	private Date importDate; // Correspond à IMPORT_DATE
	private String sourceSystem; // Correspond à SOURCESYSTEM_CD
	private String valueType; // Correspond à VALUETYPE_CD
	private String mAppliedPath; // Correspond à M_APPLIED_PATH
	private String mExclusion; // Correspond à M_EXCLUSION_CD
	private String cPath; // Correspond à C_PATH
	private String cSymbol; // Correspond à C_SYMBOL
	/**
	 * @return the cHLevel
	 */
	public int getcHLevel() {
		return cHLevel;
	}
	/**
	 * @param cHLevel the cHLevel to set
	 */
	public void setcHLevel(int cHLevel) {
		this.cHLevel = cHLevel;
	}
	/**
	 * @return the cFullName
	 */
	public String getcFullName() {
		return cFullName;
	}
	/**
	 * @param cFullName the cFullName to set
	 */
	public void setcFullName(String cFullName) {
		this.cFullName = cFullName;
	}
	/**
	 * @return the cName
	 */
	public String getcName() {
		return cName;
	}
	/**
	 * @param cName the cName to set
	 */
	public void setcName(String cName) {
		this.cName = cName;
	}
	/**
	 * @return the cSynonym
	 */
	public char getcSynonym() {
		return cSynonym;
	}
	/**
	 * @param cSynonym the cSynonym to set
	 */
	public void setcSynonym(char cSynonym) {
		this.cSynonym = cSynonym;
	}
	/**
	 * @return the cVisualAttributes
	 */
	public String getcVisualAttributes() {
		return cVisualAttributes;
	}
	/**
	 * @param cVisualAttributes the cVisualAttributes to set
	 */
	public void setcVisualAttributes(String cVisualAttributes) {
		this.cVisualAttributes = cVisualAttributes;
	}
	/**
	 * @return the cTotalNum
	 */
	public int getcTotalNum() {
		return cTotalNum;
	}
	/**
	 * @param cTotalNum the cTotalNum to set
	 */
	public void setcTotalNum(int cTotalNum) {
		this.cTotalNum = cTotalNum;
	}
	/**
	 * @return the cBaseCode
	 */
	public String getcBaseCode() {
		return cBaseCode;
	}
	/**
	 * @param cBaseCode the cBaseCode to set
	 */
	public void setcBaseCode(String cBaseCode) {
		this.cBaseCode = cBaseCode;
	}
	/**
	 * @return the cMetaDataXML
	 */
	public String getcMetaDataXML() {
		return cMetaDataXML;
	}
	/**
	 * @param cMetaDataXML the cMetaDataXML to set
	 */
	public void setcMetaDataXML(String cMetaDataXML) {
		this.cMetaDataXML = cMetaDataXML;
	}
	/**
	 * @return the cFactTableColumn
	 */
	public String getcFactTableColumn() {
		return cFactTableColumn;
	}
	/**
	 * @param cFactTableColumn the cFactTableColumn to set
	 */
	public void setcFactTableColumn(String cFactTableColumn) {
		this.cFactTableColumn = cFactTableColumn;
	}
	/**
	 * @return the cTableName
	 */
	public String getcTableName() {
		return cTableName;
	}
	/**
	 * @param cTableName the cTableName to set
	 */
	public void setcTableName(String cTableName) {
		this.cTableName = cTableName;
	}
	/**
	 * @return the cColumnName
	 */
	public String getcColumnName() {
		return cColumnName;
	}
	/**
	 * @param cColumnName the cColumnName to set
	 */
	public void setcColumnName(String cColumnName) {
		this.cColumnName = cColumnName;
	}
	/**
	 * @return the cColumnDataType
	 */
	public String getcColumnDataType() {
		return cColumnDataType;
	}
	/**
	 * @param cColumnDataType the cColumnDataType to set
	 */
	public void setcColumnDataType(String cColumnDataType) {
		this.cColumnDataType = cColumnDataType;
	}
	/**
	 * @return the cOperator
	 */
	public String getcOperator() {
		return cOperator;
	}
	/**
	 * @param cOperator the cOperator to set
	 */
	public void setcOperator(String cOperator) {
		this.cOperator = cOperator;
	}
	/**
	 * @return the cDimCode
	 */
	public String getcDimCode() {
		return cDimCode;
	}
	/**
	 * @param cDimCode the cDimCode to set
	 */
	public void setcDimCode(String cDimCode) {
		this.cDimCode = cDimCode;
	}
	/**
	 * @return the cComment
	 */
	public String getcComment() {
		return cComment;
	}
	/**
	 * @param cComment the cComment to set
	 */
	public void setcComment(String cComment) {
		this.cComment = cComment;
	}
	/**
	 * @return the cTooltip
	 */
	public String getcTooltip() {
		return cTooltip;
	}
	/**
	 * @param cTooltip the cTooltip to set
	 */
	public void setcTooltip(String cTooltip) {
		this.cTooltip = cTooltip;
	}
	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * @return the downloadDate
	 */
	public Date getDownloadDate() {
		return downloadDate;
	}
	/**
	 * @param downloadDate the downloadDate to set
	 */
	public void setDownloadDate(Date downloadDate) {
		this.downloadDate = downloadDate;
	}
	/**
	 * @return the importDate
	 */
	public Date getImportDate() {
		return importDate;
	}
	/**
	 * @param importDate the importDate to set
	 */
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	/**
	 * @return the sourceSystem
	 */
	public String getSourceSystem() {
		return sourceSystem;
	}
	/**
	 * @param sourceSystem the sourceSystem to set
	 */
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	/**
	 * @return the valueType
	 */
	public String getValueType() {
		return valueType;
	}
	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	/**
	 * @return the mAppliedPath
	 */
	public String getmAppliedPath() {
		return mAppliedPath;
	}
	/**
	 * @param mAppliedPath the mAppliedPath to set
	 */
	public void setmAppliedPath(String mAppliedPath) {
		this.mAppliedPath = mAppliedPath;
	}
	/**
	 * @return the mExclusion
	 */
	public String getmExclusion() {
		return mExclusion;
	}
	/**
	 * @param mExclusion the mExclusion to set
	 */
	public void setmExclusion(String mExclusion) {
		this.mExclusion = mExclusion;
	}
	/**
	 * @return the cPath
	 */
	public String getcPath() {
		return cPath;
	}
	/**
	 * @param cPath the cPath to set
	 */
	public void setcPath(String cPath) {
		this.cPath = cPath;
	}
	/**
	 * @return the cSymbol
	 */
	public String getcSymbol() {
		return cSymbol;
	}
	/**
	 * @param cSymbol the cSymbol to set
	 */
	public void setcSymbol(String cSymbol) {
		this.cSymbol = cSymbol;
	}
	
	
	
}

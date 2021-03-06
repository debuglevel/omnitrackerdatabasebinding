package de.debuglevel.omnitrackerdatabasebinding.layout

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import de.debuglevel.omnitrackerdatabasebinding.entity.ColumnType
import de.debuglevel.omnitrackerdatabasebinding.entity.EntityService
import de.debuglevel.omnitrackerdatabasebinding.folder.FolderService
import mu.KotlinLogging
import java.sql.ResultSet
import java.util.*
import javax.inject.Singleton

@Singleton
class LayoutService(
    private val databaseService: DatabaseService,
    private val folderService: FolderService
) : EntityService<Layout>(databaseService) {
    private val logger = KotlinLogging.logger {}

    override val name = "Layout"
    override val table = "Layout"
    override val columns = mapOf(
        "id" to ColumnType.Integer,
        "name" to ColumnType.String,
        "folder" to ColumnType.Integer,
        "report_data" to ColumnType.String,
        "type" to ColumnType.Integer,
        "version" to ColumnType.Integer,
        "output_type" to ColumnType.Integer,
        "mailmerge_doctype" to ColumnType.Integer,
        "mailmerge_sql" to ColumnType.String,
        "mailmerge_filetype" to ColumnType.Integer,
        "cr_replace_mdb" to ColumnType.Boolean,
        "cr_static_db_conn" to ColumnType.Integer
    )

    override fun build(resultSet: ResultSet): Layout {
        logger.trace { "Building layout for ResultSet $resultSet..." }

        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        val folderId = resultSet.getInt("folder")
        val reportDataBase64 = resultSet.getString("report_data")
        val typeId = resultSet.getInt("type")
        val version = resultSet.getInt("version")
        val outputTypeId = resultSet.getInt("output_type")
        val mailmergeDoctype = resultSet.getInt("mailmerge_doctype")
        val mailmergeSql = resultSet.getString("mailmerge_sql")
        val mailmergeFiletype = resultSet.getInt("mailmerge_filetype")
        val isCrystalreportsReplaceMdb = resultSet.getBoolean("cr_replace_mdb")
        val crystalreportsStaticDatabaseConnection = resultSet.getString("cr_static_db_conn")

        val folder = folderService.get(folderId)

        val layout = Layout(
            id,
            name,
            version,
            reportDataBase64,
            mailmergeSql,
            isCrystalreportsReplaceMdb,
            crystalreportsStaticDatabaseConnection,
            typeId,
            outputTypeId,
            mailmergeDoctype,
            mailmergeFiletype,
            folderId,
            folder
            //lazy { folderService.fetchFolders() }
        )

        logger.trace { "Built layout: $layout" }
        return layout
    }

    fun updateLayoutReportData(layout: Layout, reportData: ByteArray) {
        logger.debug("Updating report data for layout $layout...")

        logger.trace("Converting byte data into Base64...")
        val reportDataBase64 = Base64.getMimeEncoder().encodeToString(reportData)

        databaseService.getConnection().use { connection ->
            val preparedStatement = connection.prepareStatement("UPDATE [Layout] SET report_data = ? WHERE id = ?")
            preparedStatement.setString(1, reportDataBase64)
            preparedStatement.setInt(2, layout.id)

            logger.trace("Executing update...")
            val rowCount = preparedStatement.executeUpdate()
            logger.trace("Count of affected rows is $rowCount")

            if (rowCount == 1) {
                return
            } else {
                logger.error("Count of affected rows was not 1 but $rowCount")
                throw Exception("Row count on update was $rowCount although it should have been 1.")
            }
        }
    }
}
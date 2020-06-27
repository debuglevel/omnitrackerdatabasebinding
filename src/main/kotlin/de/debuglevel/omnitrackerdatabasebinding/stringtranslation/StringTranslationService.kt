package de.debuglevel.omnitrackerdatabasebinding.stringtranslation

import de.debuglevel.omnitrackerdatabasebinding.DatabaseService
import mu.KotlinLogging
import java.sql.Connection
import java.sql.ResultSet
import javax.inject.Singleton

@Singleton
class StringTranslationService(
    private val databaseService: DatabaseService
    //private val folderService: FolderService,
    //private val fieldService: FieldService
) {
    private val logger = KotlinLogging.logger {}

    private fun getStringTranslations(
        short: Boolean,
        connection: Connection/*, folders: Map<Int, Folder>, fields: Map<Int, Field>*/
    ): MutableList<StringTranslation> {
        val table = if (short) "StringTransShort" else "StringTranslations"
        val sqlStatement = connection.createStatement()
        val resultSet =
            sqlStatement.executeQuery("SELECT id, str_guid, type, ref, field, folder, langcode, txt, untranslated FROM [$table]")

        val stringTranslations = mutableListOf<StringTranslation>()

        while (resultSet.next()) {
            val stringTranslation = buildStringTranslation(resultSet, short)
            stringTranslations.add(stringTranslation)
        }

        return stringTranslations
    }

//    fun getStringTranslation(id: Int): StringTranslation
//    {
//        return getStringTranslations().getValue(id)
//    }

    private fun buildStringTranslation(
        resultSet: ResultSet,
        short: Boolean
    ): StringTranslation {
        val id = resultSet.getInt("id")
        val guid = resultSet.getString("str_guid")
        val type = resultSet.getInt("type")
        val fieldId = resultSet.getInt("ref")
        val folderId = resultSet.getInt("folder")
        val languageCode = resultSet.getString("langcode")
        val textRaw = resultSet.getString("txt") ?: null
        val text = if (short) {
            textRaw?.trimEnd()
        } else {
            textRaw
        }
        val untranslated = resultSet.getBoolean("untranslated")

        val stringTranslation = StringTranslation(
            id,
            guid,
            languageCode,
            text,
            untranslated,
            short,
            type,
            fieldId,
            folderId
            //                lazy { fields },
            //                lazy { folders }
        )
        return stringTranslation
    }

    fun getStringTranslations(/*folders: Map<Int, Folder>, fields: Map<Int, Field>*/): List<StringTranslation> {
        databaseService.getConnection().use { connection ->
            return getStringTranslations(true, connection/*, folders, fields*/)
                .plus(getStringTranslations(false, connection/*, folders, fields*/))
        }
    }
}
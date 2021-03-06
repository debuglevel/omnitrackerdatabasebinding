package de.debuglevel.omnitrackerdatabasebinding.folder

import de.debuglevel.omnitrackerdatabasebinding.entity.Entity

data class Folder(
    override val id: Int,
    val name: String,
    val alias: String? = null,
    val singularTerm: String? = null,
    val pluralTerm: String? = null,
    val parentFolderId: Number,
    val parentFolder: Folder?,
    val path: String,
    val description: String?
//    private val folderMap: Lazy<Map<Int, Folder>>,
//    private val fieldMap: Lazy<Map<Int, Field>>,
//    private val stringTranslationList: Lazy<List<StringTranslation>>
) : Entity {
//    val fields: Map<Int, Field>
//        get() = fieldMap.value.filter { it.value.folder == this }
//
//    val parentFolder: Folder?
//        get() = folderMap.value[this.parentFolderId]

//    val path: String
//        get() {
//            return if (parentFolder != null) {
//                "${parentFolder?.path}\\${getName(StringTranslationLanguage.German)?.text}"
//            } else {
//                "\\${getName(StringTranslationLanguage.German)?.text}"
//            }
//        }

//    private val stringTranslations: List<StringTranslation> by lazy {
//        stringTranslationList.value.filter { it.folder == this }
//    }

//    fun getName(language: StringTranslationLanguage) = stringTranslations
//        .singleOrNull { it.language == language && it.type == StringTranslationType.FolderName }
//
//    fun getDescription(language: StringTranslationLanguage) = stringTranslations
//        .singleOrNull { it.language == language && it.type == StringTranslationType.FolderDescription }

    override fun hashCode() = this.id

    override fun equals(other: Any?): Boolean {
        return when {
            other == null -> false
            this === other -> true
            other is Folder -> {
                val o = other
                this.id == o.id &&
                        this.name == o.name &&
                        this.parentFolderId == o.parentFolderId &&
                        this.singularTerm == o.singularTerm &&
                        this.pluralTerm == o.pluralTerm &&
                        this.alias == o.alias
            }
            else -> false
        }
    }

    override fun toString(): String {
        return "Folder(" +
                "id=$id, " +
                "name='$name', " +
                "alias=$alias, " +
                "singularTerm=$singularTerm, " +
                "pluralTerm=$pluralTerm, " +
                "parentFolderId=$parentFolderId, " +
                //"parentFolder=$parentFolder, " +
                "path='$path', " +
                "description=$description" +
                ")"
    }

}
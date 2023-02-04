package uz.gita.myqqeng.data.database

object MyStatics {
    val CREATE_DICTIONARY_TABLE = "CREATE TABLE \"Dictionary\" (\n" +
            "\t\"id\"\tINTEGER NOT NULL,\n" +
            "\t\"word\"\tTEXT NOT NULL,\n" +
            "\t\"translate\"\tINTEGER NOT NULL,\n" +
            "\t\"isFavourite\"\tINTEGER NOT NULL,\n" +
            "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
            ");"
}
package com.iiit.adb.emp.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class ColumnsFinder {

public static String[] readSql(String schema) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(schema)));
    String mysql = "";
    String line;
    while ((line = br.readLine()) != null) {
        mysql = mysql + line;
    }
    br.close();
    mysql = mysql.replaceAll("`", "");
    return mysql.split(";");
}

public static List<String> getColumnNames(String tableName, String schemaFile) throws JSQLParserException, IOException {

    CCJSqlParserManager pm = new CCJSqlParserManager();
    List<String> columnNames = new ArrayList<String>();

    String[] sqlStatements = readSql(schemaFile);

    for (String sqlStatement : sqlStatements) {

        Statement statement = pm.parse(new StringReader(sqlStatement));

        if (statement instanceof CreateTable) {

            CreateTable create = (CreateTable) statement;
            String name = create.getTable().getName();

            if (name.equalsIgnoreCase(tableName)) {
                List<ColumnDefinition> columns = create.getColumnDefinitions();
                for (ColumnDefinition def : columns) {
                    columnNames.add(def.getColumnName());
                }
                break;
            }
        }
    }

    return columnNames;
}


public static void main(String[] args) throws Exception {

    

}

}
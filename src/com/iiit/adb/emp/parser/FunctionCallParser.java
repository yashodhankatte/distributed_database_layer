package com.iiit.adb.emp.parser;

import java.io.StringReader;
import java.util.ArrayList;

import com.iiit.adb.emp.db.parser.globaldefinition.FunctionParam;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

/**
 * this class need to be implemented in visitor pattern
 * @author Jyothi
 *
 */
public class FunctionCallParser
{
   // 
    FunctionParam fparam;
	
    public FunctionCallParser(String sql,CCJSqlParserManager parserManager) throws JSQLParserException
    {
        String statement = "SELECT sum(salary) FROM db.table1";
        FunctionParam fp = new FunctionParam();
        PlainSelect plainSelect = (PlainSelect) ((Select) parserManager.parse(new StringReader(statement))).getSelectBody();        
       // fp.setColumnname( ((Function)((SelectExpressionItem) plainSelect.getSelectItems().get(0)).getExpression()).getName()).
     //   fp.setFunctionname((String)(plainSelect.getSelectItems().get(0)));
        String s = ((Function)((SelectExpressionItem) plainSelect.getSelectItems().get(0)).getExpression()).getName();
        String s1 = (String)((Function)((SelectExpressionItem) plainSelect.getSelectItems().get(0)).getExpression()).getParameters().getExpressions().get(0).toString();
        System.out.println(s1+ " " + s);
       // System.out.format("%s is function call? %s",
               // plainSelect.getSelectItems().get(0),
               // ((Function)((SelectExpressionItem) plainSelect.getSelectItems().get(0)).getExpression()).isAllColumns());
    }
    public static void main(String[] args) throws JSQLParserException
    {

    	CCJSqlParserManager parserManager = new CCJSqlParserManager();
        new FunctionCallParser("SELECT SUM(salary) FROM db.table1",parserManager);

    }

}
